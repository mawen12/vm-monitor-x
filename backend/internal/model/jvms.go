package model

import (
	"fmt"
	"log"
	"os"
	"os/user"
	"path"
	"path/filepath"
	"regexp"
	"runtime"
	"strconv"
	"strings"

	"github.com/mawen12/vm-monitor-x/internal/utils"
	"github.com/tokuhirom/go-hsperfdata/attach"
	hs "github.com/tokuhirom/go-hsperfdata/hsperfdata"
)

type Jvm struct {
	Pid      string
	ProcName string
	User     string
	Version  string
	socket   *attach.Socket
}

func (j *Jvm) ToRow() []string {
	return []string{j.Pid, j.Version, j.User, j.ProcName}
}

func (j *Jvm) AttachAndLoadAgent(jar string, args string) error {
	log.Println("Attaching to Pid:", j.Pid)
	if err := j.Attach(); err != nil {
		log.Println("Cannot attach ", err)
		return err
	}

	log.Println("Loading agent", jar, "args:", args)
	if err := j.LoadAgent(jar, args); err != nil {
		log.Println("Load agent error", err)
		return err
	}

	log.Println("Loaded agent")
	return j.Detach()
}

func (j *Jvm) Attach() error {
	if j.Attached() {
		return nil
	}

	pidNr, _ := strconv.Atoi(j.Pid)
	socketFile, _ := attach.GetSocketFile(pidNr)
	log.Println("Socket file:", socketFile)

	attachPid(j.Pid, j.User)

	sock, err := attach.New(pidNr)
	if err != nil {
		log.Println("Attach error ", err, "pid", pidNr)
		return err
	}

	log.Println("Attached to JVM")
	j.socket = sock
	return nil
}

func (j *Jvm) LoadAgent(agentJar string, args string) error {
	absolute := "false"
	agent := agentJar + "=" + args
	if err := j.socket.Execute("load", "instrument", absolute, agent); err != nil {
		log.Println("LoadAgent execute error", err)
		return err
	}

	out, err := j.socket.ReadString()
	if err != nil {
		log.Println("LoadAgent out error", err)
		return err
	}

	log.Println("LoadAgent out", out)
	return nil
}

func attachPid(pid string, pidUser string) {
	curUser := utils.GetCurUserFromEnv()

	if curUser != pidUser && curUser == "root" {
		pidDir := fmt.Sprintf("/proc/%s/cwd", pid)
		exists, _ := utils.Exists(pidDir)

		if exists {
			usr, _ := user.Lookup(pidUser)
			uid, _ := strconv.Atoi(usr.Uid)
			gid, _ := strconv.Atoi(usr.Gid)

			log.Println("Attaching to Jvm of user id: ", uid, gid)
			attachFile := fmt.Sprintf("/proc/%s/cwd/.attach_pid%s", pid, pid)
			f, err := os.Create(attachFile)
			if err != nil {
				log.Printf("Cannot create file %v %v", attachFile, err)
				return
			}
			err = os.Chown(attachFile, uid, gid)
			if err := f.Close(); err != nil {
				log.Println("Cannot close", err)
			}
		}
	}
}

func (j *Jvm) Attached() bool {
	return j.socket != nil
}

func (j *Jvm) Detach() error {
	sock := j.socket
	j.socket = nil
	return sock.Close()
}

type Jvms map[string]Jvm

func GetJvms() Jvms {
	users := GetJvmUsers()
	log.Println("JVM users count: ", len(users))

	jvms := make(map[string]Jvm, 0)
	for _, user := range users {
		log.Println("Found Jvm user: ", user)

		userJvms, err := GetJvmsByUser(user)
		if err != nil {
			log.Println("Found Jvm failed: ", err)
			continue
		}

		for pid, jvm := range userJvms {
			jvms[pid] = jvm
		}
	}

	return jvms
}

func GetJvmUsers() []string {
	pids, err := GetJvmPids()
	if err != nil {
		log.Fatal("Error finding Jvms: ", err)
		return nil
	}

	userMap := make(map[string]struct{}, 0)
	for _, user := range pids {
		userMap[user] = struct{}{}
	}

	users := make([]string, 0)
	for u, _ := range userMap {
		users = append(users, u)
	}
	return users
}

func GetJvmPids() (map[string]string, error) {
	numbers := regexp.MustCompile("[0-9]+")

	pids := make(map[string]string)

	fn := func(path string, info os.FileInfo, err error) error {
		if strings.Contains(path, "hsperfdata_") && info.Mode().IsRegular() {
			parts := strings.Split(path, string(os.PathSeparator))
			pidFile := parts[len(parts)-1]

			if numbers.MatchString(pidFile) {
				userDir := parts[len(parts)-2]
				user := strings.Split(userDir, "_")[1]
				pids[pidFile] = user
			}
		}
		return nil
	}

	err := filepath.Walk(os.TempDir(), fn)
	return pids, err
}

func GetJvmsByUser(user string) (Jvms, error) {
	repo, err := newRepository(user)
	if err != nil {
		log.Println("Cannot initialize", err)
		return nil, err
	}

	files, err := repo.GetFiles()
	if err != nil {
		log.Println("No Jvms found for user", user, err)
		return nil, err
	}

	jvms := make(map[string]Jvm)
	for _, f := range files {
		res, err := f.Read()
		var jvm Jvm
		if err == nil {
			procName := res.GetProcName()
			parts := strings.Split(procName, string(os.PathSeparator))
			version := res.GetMap()["java.property.java.vm.specification.version"].(string)

			jvm = Jvm{f.GetPid(), parts[len(parts)-1], user, version, nil}
		} else {
			procName := getCmdLine(f.GetPid())

			jvm = Jvm{f.GetPid(), procName, user, "", nil}
		}

		jvms[jvm.Pid] = jvm
	}
	return jvms, nil
}

func newRepository(user string) (*hs.Repository, error) {
	if user == "" {
		return hs.New()
	}
	return hs.NewUser(user)
}

func getCmdLine(pid string) string {
	if runtime.GOOS != "linux" {
		return ""
	}
	cmdLinePath := path.Join("/proc", pid, "cmdline")
	cmdLine, err := os.ReadFile(cmdLinePath)
	if err != nil {
		log.Println("Cannot read ", cmdLinePath, err)
		return ""
	}
	return strings.ReplaceAll(string(cmdLine), "\x00", " ")
}

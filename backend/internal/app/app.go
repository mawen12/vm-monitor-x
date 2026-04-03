package app

import (
	"context"
	_ "embed"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strconv"

	"github.com/asaskevich/EventBus"
	"github.com/mawen12/vm-monitor-x/internal/model"
	"github.com/mawen12/vm-monitor-x/internal/view"

	ui "github.com/gizak/termui/v3"
)

type App struct {
	EventBus.Bus
	logger *Logger
	server *Server

	jvms model.Jvms
	jar  string

	view *view.App
}

func NewApp() *App {
	app := &App{
		Bus:    EventBus.New(),
		logger: NewLogger(),
		server: NewServer(),
	}

	return app
}

func (app *App) Init(jarBytes []byte) error {
	ctx := context.WithValue(context.Background(), "app", app)

	// logger
	if err := app.logger.Init(ctx); err != nil {
		return fmt.Errorf("Error init logger: %v", err.Error())
	}

	// jvms
	app.jvms = model.GetJvms()
	log.Printf("Found %d Jvms\n", len(app.jvms))

	// server
	if err := app.server.Init(ctx); err != nil {
		return fmt.Errorf("Error init server: %v", err.Error())
	}

	// jar
	jar, err := loadJar(jarBytes)
	if err != nil {
		return fmt.Errorf("Error load jar: %v", err.Error())
	}
	app.jar = jar

	// ui
	if err := ui.Init(); err != nil {
		return fmt.Errorf("Cannot init UI: %v", err.Error())
	}

	// view
	app.view = view.NewApp(app.jvms, app.Bus)

	app.Background(app.receiveMetrics)
	app.Background(app.checkConnections)

	return nil
}

func (app *App) Run() error {
	app.Publish(model.PortEvent, app.server.Port)
	app.SubscribeAsync(model.JvmSelectedEvent, app.onJvmSelected, false)

	app.view.Run()
	return nil
}

func (app *App) Background(f func()) {
	go func() {
		if err := recover(); err != nil {
			log.Println("App background error, ", err)
		}

		f()
	}()
}

func (app *App) Close() error {
	ui.Close()
	return os.Remove(app.jar)
}

func loadJar(jarBytes []byte) (string, error) {
	log.Println("Found embedded jar file: ", len(jarBytes))
	tmpJarFile, err := os.CreateTemp(os.TempDir(), "vm-monitor-x.jar")
	if err != nil {
		log.Println("Failed to create temporary file", err)
		return "", err
	}

	if _, err = tmpJarFile.Write(jarBytes); err != nil {
		log.Println("Failed to write to temporary file", err)
		return "", err
	}

	tmpJarPath := tmpJarFile.Name()
	log.Println("Created temp file ", tmpJarPath)

	if err := tmpJarFile.Close(); err != nil {
		log.Println("Temporary file close", err)
		return "", err
	}

	if err = os.Chmod(tmpJarPath, 0644); err != nil {
		log.Println("Cannot chmod ", tmpJarPath, " ", err)
		return "", err
	}

	return tmpJarPath, nil
}

func (app *App) onJvmSelected(pid string) {
	log.Println("Monitoring pid: ", pid)
	jvm := app.jvms[pid]
	app.Background(func() {
		app.attachAgent(jvm, app.jar, app.server.Port)
	})
}

func (app *App) attachAgent(jvm model.Jvm, jar string, port int) {
	err := jvm.AttachAndLoadAgent(jar, strconv.Itoa(port))
	if err != nil {
		log.Println("Cannot attach to pid ", jvm.Pid)
		app.Publish(model.AttachErrEvent, jvm.Pid)
	}
	log.Printf("Attach agent to Jvm(%s) success\n", jvm.Pid)
}

func (app *App) receiveMetrics() {
	for msg := range app.server.Messages {

		bytes := []byte(msg)
		switch bytes[0] {
		case 'A':
			var abilities model.Abilities
			err := json.Unmarshal(bytes[2:], &abilities)
			if err != nil {
				log.Fatal("Cannot unmarshal: ", msg, "err: ", err)
				continue
			}

			app.Publish(model.AbilitiesEvent, abilities)
		case 'M':
			var metrics model.Metrics
			err := json.Unmarshal(bytes[2:], &metrics)
			if err != nil {
				log.Fatal("Cannot unmarshal: ", msg, "err: ", err)
				continue
			}

			app.Publish(model.MetricsEvent, metrics)
			app.Publish(model.MetricsThreadsEvent, metrics.Threads)
			app.Publish(model.MetricsMemoryEvent, metrics.Memory)
			app.Publish(model.MetricsTomcatEvent, metrics.Tomcat)

			for key, dataSource := range metrics.DataSources {
				app.Publish(model.MetricsDruidEvent+"_"+key, dataSource)
			}
		}
	}
}

func (app *App) checkConnections() {
	for addr := range app.server.Connections {
		log.Println("Jvm connected ", addr)
	}
}

package app

import (
	"context"
	"fmt"
	"log"
	"os"
	"path"

	"github.com/mawen12/vm-monitor-x/internal/utils"
)

type Logger struct {
	logPath string
}

func NewLogger() *Logger {
	// user
	user := utils.GetCurUserFromEnv()

	return &Logger{
		logPath: path.Join(os.TempDir(), fmt.Sprintf("vm-monitor-x-%s.log", user)),
	}
}

func (l *Logger) Init(ctx context.Context) error {
	logFile, logErr := os.OpenFile(l.logPath, os.O_CREATE|os.O_APPEND|os.O_WRONLY, 0666)
	if logErr != nil {
		return fmt.Errorf("Error opening log file %s: %v", l.logPath, logErr.Error())
	}

	log.SetOutput(logFile)
	log.Println("Logger init success")
	return nil
}

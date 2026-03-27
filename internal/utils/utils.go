package utils

import (
	"os"
	"runtime"
)

func GetCurUserFromEnv() string {
	userEnv := "USER"
	if runtime.GOOS == "windows" {
		userEnv = "USERNAME"
	}

	return os.Getenv(userEnv)
}

func Exists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err != nil && os.IsNotExist(err) {
		return false, nil
	}
	return true, nil
}

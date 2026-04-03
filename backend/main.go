package main

import (
	_ "embed"

	"github.com/mawen12/vm-monitor-x/internal/app"
)

//go:embed libs/vm-monitor-x.jar
var jarBytes []byte

func main() {
	a := app.NewApp()
	defer func() {
		if err := recover(); err != nil {
			panic(err)
		}
	}()

	if err := a.Init(jarBytes); err != nil {
		panic(err)
	}
	defer a.Close()

	if err := a.Run(); err != nil {
		panic(err)
	}
}

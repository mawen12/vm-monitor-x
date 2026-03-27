package main

import (
	_ "embed"

	"github.com/mawen12/vm-monitor-x/internal/app"
)

//go:embed build/libs/vm-monitor-x.jar
var jarBytes []byte

func main() {
	a := app.NewApp()

	if err := a.Init(jarBytes); err != nil {
		panic(err)
	}
	defer a.Close()

	if err := a.Run(); err != nil {
		panic(err)
	}
}

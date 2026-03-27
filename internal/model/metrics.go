package model

import (
	"strconv"
	"time"
)

type Metrics struct {
	Used    float64
	Max     float64
	Load    float64
	Threads Threads
	Time    time.Time
}

type Threads struct {
	Count   int
	Threads []Thread
}

type Thread struct {
	Id      int64
	Name    string
	State   string
	CpuTime int64
}

func (t Thread) ToRow() []string {
	cpuTime := strconv.FormatInt(t.CpuTime/1000, 10)
	if t.CpuTime == 0 {
		cpuTime = ""
	}

	return []string{strconv.FormatInt(t.Id, 10), t.State, cpuTime, t.Name}
}

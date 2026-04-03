package model

import (
	"strconv"
)

type Abilities struct {
	DataSources []string `json:"DataSources"`
	Tomcat      string   `json:"Tomcat"`
}

type Metrics struct {
	Memory      Memory                `json:"memory"`
	Load        float64               `json:"Load"`
	Threads     Threads               `json:"Threads"`
	DataSources map[string]DataSource `json:"DataSources"`
	Tomcat      Tomcat                `json:"Tomcat"`
	Timestamp   int64                 `json:"Time"`
}

type Memory struct {
	Used float64 `json:"Used"`
	Max  float64 `json:"Max"`
}

type Threads struct {
	Count   int      `json:"Count"`
	Threads []Thread `json:"Threads"`
}

type Thread struct {
	Id      int64  `json:"Id"`
	Name    string `json:"Name"`
	State   string `json:"State"`
	CpuTime int64  `json:"CpuTime"`
}

func (t Thread) ToRow() []string {
	cpuTime := strconv.FormatInt(t.CpuTime/1000, 10)
	if t.CpuTime == 0 {
		cpuTime = ""
	}

	return []string{strconv.FormatInt(t.Id, 10), t.State, cpuTime, t.Name}
}

type DataSource struct {
	ActiveCount     int `json:"ActiveCount"`
	PoolingCount    int `json:"PoolingCount"`
	MaxActive       int `json:"MaxActive"`
	WaitThreadCount int `json:"WaitThreadCount"`
}

type Tomcat struct {
	CurrentThreadsBusy int `json:"CurrentThreadsBusy"`
	CurrentThreadCount int `json:"CurrentThreadCount"`
	MaxThreads         int `json:"MaxThreads"`
}

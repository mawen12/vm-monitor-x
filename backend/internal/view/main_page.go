package view

import (
	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type MainPage struct {
	*ui.Grid
	EventBus.Bus

	JvmTable    *JvmTable
	memChart    *MemChart
	cpuChart    *CpuChart
	threadTable *ThreadTable
	tomcatChart *TomcatChart
	druidsChart []*DruidChart
}

func NewMainPage(eb EventBus.Bus) *MainPage {
	mp := &MainPage{
		Grid:        ui.NewGrid(),
		Bus:         eb,
		JvmTable:    NewJvmTable(eb),
		memChart:    NewMemChart(eb),
		cpuChart:    NewCpuChart(eb),
		threadTable: NewThreadTable(eb),
		tomcatChart: NewTomcatChart(eb),
		druidsChart: make([]*DruidChart, 0),
	}

	width, height := ui.TerminalDimensions()
	mp.SetRect(0, 0, width, height)

	mp.Set(ui.NewRow(1, ui.NewCol(1, mp.JvmTable)))

	mp.render()

	mp.SubscribeAsync(model.AbilitiesEvent, mp.onAbilitiesEvent, false)

	return mp
}

func (mp *MainPage) onAbilitiesEvent(abilities model.Abilities) {
	rows := make([]interface{}, 0)
	cols := make([]interface{}, 0)

	rows = append(rows, ui.NewRow(0.2,
		ui.NewCol(0.5, mp.JvmTable),
		ui.NewCol(0.5, mp.cpuChart)))

	rows = append(rows, ui.NewRow(0.2,
		ui.NewCol(0.5, mp.threadTable),
		ui.NewCol(0.5, mp.memChart)))

	count := 0
	if abilities.Tomcat != "" {
		count++
		cols = append(cols, ui.NewCol(0.5, mp.tomcatChart))
	}

	for _, dataSource := range abilities.DataSources {
		count++

		druidChart := NewDruidChart(dataSource, mp.Bus)
		mp.druidsChart = append(mp.druidsChart, druidChart)
		cols = append(cols, ui.NewCol(0.5, druidChart))

		if count%2 == 0 {
			rows = append(rows, ui.NewRow(0.2, cols...))
			cols = make([]interface{}, 0)
		}
	}

	if len(cols) != 0 {
		rows = append(rows, ui.NewRow(0.2, cols...))
	}

	mp.Set(rows...)

	ui.Clear()

	mp.render()
}

func (mp *MainPage) render() {
	ui.Render(mp)
}

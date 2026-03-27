package view

import (
	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
)

type MainPage struct {
	*ui.Grid

	JvmTable    *JvmTable
	memChart    *MemChart
	cpuChart    *CpuChart
	threadTable *ThreadTable
}

func NewMainPage(eb EventBus.Bus) *MainPage {
	mp := &MainPage{
		Grid:        ui.NewGrid(),
		JvmTable:    NewJvmTable(eb),
		memChart:    NewMemChart(eb),
		cpuChart:    NewCpuChart(eb),
		threadTable: NewThreadTable(eb),
	}

	width, height := ui.TerminalDimensions()
	mp.SetRect(0, 0, width, height)

	half := 1.0 / 2
	mp.Set(ui.NewRow(half, ui.NewCol(half, mp.JvmTable), ui.NewCol(half, mp.cpuChart)),
		ui.NewRow(half, ui.NewCol(half, mp.threadTable), ui.NewCol(half, mp.memChart)))

	mp.render()

	return mp
}

func (mp *MainPage) render() {
	ui.Render(mp)
}

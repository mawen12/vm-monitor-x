package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type CpuChart struct {
	*widgets.Plot
	EventBus.Bus
}

func NewCpuChart(eb EventBus.Bus) *CpuChart {
	cc := &CpuChart{
		Plot: widgets.NewPlot(),
		Bus:  eb,
	}

	cc.Data = make([][]float64, 1)
	cc.Data[0] = []float64{0, 0}
	cc.LineColors[0] = ui.ColorYellow
	cc.TitleStyle.Fg = ui.ColorWhite
	cc.AxesColor = ui.ColorWhite
	cc.PlotType = widgets.LineChart
	cc.Title = "CPU"

	cc.Subscribe(model.JvmSelectedEvent, cc.onJvmSelected)

	cc.Subscribe(model.MetricsEvent, cc.onMetrics)

	return cc
}

func (cc *CpuChart) onJvmSelected(pid string) {
	cc.Data[0] = []float64{0, 0}
	cc.Title = "CPU %"

	cc.render()
}

func (cc *CpuChart) onMetrics(metrics model.Metrics) {
	maxX := cc.Bounds().Max.X
	data := append(cc.Data[0], metrics.Load)
	if len(data) > maxX/2 {
		data = data[1:]
	}
	cc.Data[0] = data

	cc.Title = fmt.Sprintf("CPU: %d%", int(metrics.Load))
	cc.MaxVal = metrics.Max

	cc.render()
}

func (cc *CpuChart) render() {
	ui.Render(cc)
}

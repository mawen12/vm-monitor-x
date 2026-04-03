package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type TomcatChart struct {
	*widgets.Plot
	EventBus.Bus
}

func NewTomcatChart(eb EventBus.Bus) *TomcatChart {
	tc := &TomcatChart{
		Plot: widgets.NewPlot(),
		Bus:  eb,
	}

	tc.Data = make([][]float64, 2)

	tc.Data[0] = []float64{0, 0}
	tc.LineColors[0] = ui.ColorBlue

	tc.Data[1] = []float64{0, 0}
	tc.LineColors[1] = ui.ColorYellow

	tc.TitleStyle.Fg = ui.ColorWhite
	tc.AxesColor = ui.ColorWhite
	tc.PlotType = widgets.LineChart
	tc.Title = "Tomcat"

	tc.Subscribe(model.JvmSelectedEvent, tc.onJvmSelected)

	tc.Subscribe(model.MetricsTomcatEvent, tc.onMetricsTomcat)

	return tc
}

func (tc *TomcatChart) onJvmSelected(pid string) {
	tc.Data[0] = []float64{0, 0}
	tc.Title = "Tomcat"

	tc.render()
}

func (tc *TomcatChart) onMetricsTomcat(tomcat model.Tomcat) {
	tc.setCurrentThreadsBusy(tomcat.CurrentThreadsBusy)
	tc.setCurrentThreadCount(tomcat.CurrentThreadCount)

	tc.MaxVal = float64(tomcat.MaxThreads)
	tc.Title = fmt.Sprintf("Tomcat: Busy: %d, Count: %d, Max: %d",
		tomcat.CurrentThreadsBusy, tomcat.CurrentThreadCount, tomcat.MaxThreads)

	tc.render()
}

func (tc *TomcatChart) setCurrentThreadsBusy(currentThreadsBusy int) {
	data := append(tc.Data[0], float64(currentThreadsBusy))
	maxX := tc.Bounds().Max.X
	if len(data) > maxX/2 {
		data = data[1:]
	}
	tc.Data[0] = data
}

func (tc *TomcatChart) setCurrentThreadCount(currentThreadCount int) {
	data := append(tc.Data[1], float64(currentThreadCount))
	maxX := tc.Bounds().Max.X
	if len(data) > maxX/2 {
		data = data[1:]
	}
	tc.Data[1] = data
}

func (tc *TomcatChart) render() {
	ui.Render(tc)
}

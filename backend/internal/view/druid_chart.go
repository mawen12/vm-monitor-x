package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type DruidChart struct {
	*widgets.Plot
	EventBus.Bus
}

func NewDruidChart(id string, eb EventBus.Bus) *DruidChart {
	dc := &DruidChart{
		Plot: widgets.NewPlot(),
		Bus:  eb,
	}

	dc.Data = make([][]float64, 3)

	dc.Data[0] = []float64{0, 0}
	dc.LineColors[0] = ui.ColorBlue

	dc.Data[1] = []float64{0, 0}
	dc.LineColors[1] = ui.ColorYellow

	dc.Data[2] = []float64{0, 0}
	dc.LineColors[2] = ui.ColorRed

	dc.Title = "Druid"
	dc.AxesColor = ui.ColorWhite
	dc.PlotType = widgets.ScatterPlot

	dc.Subscribe(model.MetricsDruidEvent+"_"+id, dc.onMetricsDruid)

	return dc
}

func (dc *DruidChart) onMetricsDruid(dataSource model.DataSource) {
	dc.setActiveCount(dataSource.ActiveCount)
	dc.setPoolingCount(dataSource.PoolingCount)
	dc.setWaitThreadCount(dataSource.WaitThreadCount)

	dc.Title = fmt.Sprintf("Druid: active: %d, pooling: %d, max: %d, wait: %d",
		dataSource.ActiveCount, dataSource.PoolingCount, dataSource.MaxActive, dataSource.WaitThreadCount)

	dc.render()
}

func (dc *DruidChart) setActiveCount(activeCount int) {
	data := append(dc.Data[0], float64(activeCount))
	maxX := dc.Bounds().Max.X
	if len(data) > maxX/2 {
		data = data[1:]
	}
	dc.Data[0] = data
}

func (dc *DruidChart) setPoolingCount(poolingCount int) {
	data := append(dc.Data[1], float64(poolingCount))
	maxX := dc.Bounds().Max.X
	if len(data) > maxX/2 {
		data = data[1:]
	}
	dc.Data[1] = data
}

func (dc *DruidChart) setWaitThreadCount(waitThreadCount int) {
	data := append(dc.Data[2], float64(waitThreadCount))
	maxX := dc.Bounds().Max.X
	if len(data) > maxX/2 {
		data = data[1:]
	}
	dc.Data[2] = data
}

func (dc *DruidChart) render() {
	ui.Render(dc)
}

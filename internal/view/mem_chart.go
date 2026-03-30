package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type MemChart struct {
	*widgets.SparklineGroup
	UsedLine *widgets.Sparkline
	EventBus.Bus
}

func NewMemChart(eb EventBus.Bus) *MemChart {
	mc := &MemChart{
		SparklineGroup: widgets.NewSparklineGroup(),
		UsedLine:       widgets.NewSparkline(),
		Bus:            eb,
	}

	mc.Title = "Memory"
	mc.UsedLine.LineColor = ui.ColorGreen
	mc.UsedLine.TitleStyle.Bg = ui.ColorWhite
	mc.Sparklines = append(mc.Sparklines, mc.UsedLine)

	mc.Subscribe(model.JvmSelectedEvent, mc.onJvmSelected)

	mc.Subscribe(model.MetricsEvent, mc.onMetrics)

	return mc
}

func (mc *MemChart) onJvmSelected(pid string) {
	mc.UsedLine.Data = []float64{}
	mc.Title = "Memory"

	mc.render()
}

func (mc *MemChart) onMetrics(metrics model.Metrics) {
	maxX := mc.Bounds().Max.X
	// record
	data := append(mc.UsedLine.Data, metrics.Memory.Used)
	if len(data) > maxX/2 {
		data = data[1:]
	}
	mc.UsedLine.Data = data
	// max value
	mc.UsedLine.MaxVal = metrics.Memory.Max

	mc.Title = fmt.Sprintf("Used: %d, Max: %d MB", int(metrics.Memory.Used), int(metrics.Memory.Max))

	mc.render()
}

func (mc *MemChart) render() {
	ui.Render(mc)
}

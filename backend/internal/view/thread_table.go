package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type ThreadTable struct {
	*widgets.Table
	EventBus.Bus

	headers  [][]string
	dataRows [][]string
}

func NewThreadTable(eb EventBus.Bus) *ThreadTable {
	tt := &ThreadTable{
		Table: widgets.NewTable(),
		Bus:   eb,
	}

	labels := []string{"Id", "State", "CpuTime", "Name"}
	tt.headers = [][]string{labels}

	tt.Title = "Threads"
	tt.Border = true
	tt.RowSeparator = false
	tt.ColumnWidths = []int{6, 15, 10, -1}
	tt.TextStyle = ui.NewStyle(ui.ColorWhite)

	tt.Subscribe(model.JvmSelectedEvent, tt.onJvmSelected)

	tt.Subscribe(model.MetricsThreadsEvent, tt.onThreads)

	return tt
}

func (tt *ThreadTable) onJvmSelected(pid string) {
	tt.Rows = [][]string{}
	tt.Title = "Threads"

	tt.render()
}

func (tt *ThreadTable) onThreads(threads model.Threads) {
	tt.Title = fmt.Sprintf("Threads (%d)", threads.Count)

	tt.Rows = tt.headers
	for _, t := range threads.Threads {
		tt.Rows = append(tt.Rows, t.ToRow())
	}

	tt.render()
}

func (tt *ThreadTable) render() {
	ui.Render(tt)
}

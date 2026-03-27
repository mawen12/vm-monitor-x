package view

import (
	"fmt"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/gizak/termui/v3/widgets"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type JvmTable struct {
	*widgets.Table
	EventBus.Bus

	headers  [][]string
	dataRows [][]string
	selected int
}

func NewJvmTable(eb EventBus.Bus) *JvmTable {
	jt := &JvmTable{
		Table:    widgets.NewTable(),
		Bus:      eb,
		selected: -1,
	}

	labels := []string{"PID", "Ver.", "User", "Main"}
	jt.headers = [][]string{labels}

	jt.Title = "Jvms"
	jt.TextStyle = ui.NewStyle(ui.ColorWhite)
	jt.Border = true
	jt.TextAlignment = ui.AlignLeft
	jt.ColumnWidths = []int{6, 5, 10, -1}
	jt.RowSeparator = false

	jt.Subscribe(model.PortEvent, jt.onPort)
	jt.SubscribeAsync(model.KeyboardEventsEvent, jt.onEvent, false)

	return jt
}

func (jt *JvmTable) SetData(data model.Jvms) {
	if len(data) > 0 {
		jt.selected = 1
	}
	for _, jvm := range data {
		jt.dataRows = append(jt.dataRows, jvm.ToRow())
	}

	jt.RowStyles[jt.selected] = ui.NewStyle(ui.ColorYellow)

	rows := make([][]string, 0)
	rows = append(rows, jt.headers...)
	rows = append(rows, jt.dataRows...)

	jt.Rows = rows

	jt.render()
}

func (jt *JvmTable) onPort(port int) {
	jt.Title = fmt.Sprintf("Jvms :%d", port)

	jt.render()
}

func (jt *JvmTable) onEvent(e string) {
	switch e {
	case "<Up>":
		jt.onPrevious()
	case "<Down>":
		jt.onNext()
	case "<Enter>":
		jt.onEnter()
	}
}

func (jt *JvmTable) onPrevious() {
	if jt.selected > 1 {
		jt.RowStyles[jt.selected] = ui.NewStyle(ui.ColorWhite)
		jt.selected -= 1
		jt.RowStyles[jt.selected] = ui.NewStyle(ui.ColorYellow)

		jt.render()
	}
}

func (jt *JvmTable) onNext() {
	if jt.selected < len(jt.dataRows) {
		jt.RowStyles[jt.selected] = ui.NewStyle(ui.ColorWhite)
		jt.selected += 1
		jt.RowStyles[jt.selected] = ui.NewStyle(ui.ColorYellow)

		jt.render()
	}
}

func (jt *JvmTable) onEnter() {
	pid := jt.SelectedRow()[0]
	jt.Publish(model.JvmSelectedEvent, pid)
}

func (jt *JvmTable) SelectedRow() []string {
	return jt.Rows[jt.selected]
}

func (jt *JvmTable) render() {
	ui.Render(jt)
}

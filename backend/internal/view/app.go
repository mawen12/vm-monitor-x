package view

import (
	"context"

	"github.com/asaskevich/EventBus"
	ui "github.com/gizak/termui/v3"
	"github.com/mawen12/vm-monitor-x/internal/model"
)

type App struct {
	mp *MainPage
	EventBus.Bus

	data       model.Jvms
	cancelFunc context.CancelFunc
}

func NewApp(data model.Jvms, eb EventBus.Bus) *App {
	app := &App{
		mp:   NewMainPage(eb),
		Bus:  eb,
		data: data,
	}

	app.mp.JvmTable.SetData(data)

	return app
}

func (app *App) Run() {
	ctx, cancel := context.WithCancel(context.Background())
	app.cancelFunc = cancel

	for {
		select {
		case <-ctx.Done():
			return

		case e := <-ui.PollEvents():
			if e.Type == ui.KeyboardEvent {
				app.Publish(model.KeyboardEventsEvent, e.ID)
			}

			switch e.ID {
			case "q", "<C-c>", "<Escape>":
				return
			case "<Resize>":
				payload := e.Payload.(ui.Resize)
				app.mp.SetRect(0, 0, payload.Width, payload.Height)
				ui.Clear()
				ui.Render(app.mp)
			}
		}
	}
}

func (app *App) Close() {
	if app.cancelFunc != nil {
		app.cancelFunc()
	}
	ui.Close()
}

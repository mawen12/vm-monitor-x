package app

import (
	"context"
	"fmt"
	"log"
	"net"

	"github.com/mawen12/vm-monitor-x/internal/model"
)

type Server struct {
	Port     int
	Messages chan string

	listener   net.Listener
	connChan   chan net.Conn
	curClient  *Client
	cancelFunc context.CancelFunc

	Connections chan net.Addr
}

func NewServer() *Server {
	return &Server{
		Messages:    make(chan string),
		connChan:    make(chan net.Conn),
		Connections: make(chan net.Addr),
	}
}

func (server *Server) Init(ctx context.Context) error {
	app := ctx.Value("app").(*App)

	listener, err := net.Listen("tcp", ":0")
	if err != nil {
		return fmt.Errorf("Server listen tcp: %v", err.Error())
	}
	server.listener = listener

	server.Port = listener.Addr().(*net.TCPAddr).Port
	log.Println("Server listening on port:", server.Port)

	ctx, cancelFunc := context.WithCancel(context.Background())
	server.cancelFunc = cancelFunc

	go server.accept(ctx)

	go server.handle(ctx)

	app.Subscribe(model.JvmSelectedEvent, func(pid string) {
		server.closeClient()
	})

	log.Println("Server init success")
	return nil
}

func (server *Server) accept(ctx context.Context) {
	for {
		select {
		case <-ctx.Done():
			log.Println("Client context cancel")
			return
		default:
			client, err := server.listener.Accept()

			if err != nil {
				log.Fatal("Could not accept", err)
				continue
			}

			log.Println("Accepcted connection", client.RemoteAddr())
			server.Connections <- client.RemoteAddr()
			server.connChan <- client
		}
	}
}

func (server *Server) handle(ctx context.Context) {
	for {
		select {
		case <-ctx.Done():
			log.Println("Server context cancel")
			return
		case conn := <-server.connChan:
			server.closeClient()
			server.curClient = NewClient(conn, func(message string) {
				server.Messages <- message
			})
		}
	}
}

func (server *Server) closeClient() {
	if server.curClient != nil {
		log.Println("Closing existing connection")

		if err := server.curClient.Close(); err != nil {
			log.Println("Error closing client", err)
		}
		server.curClient = nil
		log.Println("Closed connection")
	}
}

func (server *Server) Close() {
	server.cancelFunc()
	server.closeClient()
	if err := server.listener.Close(); err != nil {
		log.Println("Error closing listener", err)
	}
	close(server.connChan)
	close(server.Connections)
	close(server.Messages)
}

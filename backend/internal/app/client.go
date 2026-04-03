package app

import (
	"bufio"
	"context"
	"log"
	"net"
)

type Client struct {
	net.Conn
	addr       net.Addr
	reader     *bufio.Reader
	cancelFunc context.CancelFunc
	callback   func(message string)
}

func NewClient(conn net.Conn, app *App, callback func(message string)) *Client {
	ctx, cancel := context.WithCancel(context.Background())

	c := &Client{
		Conn:       conn,
		addr:       conn.RemoteAddr(),
		reader:     bufio.NewReader(conn),
		cancelFunc: cancel,
		callback:   callback,
	}

	app.Background(func() {
		c.handle(ctx)
	})

	return c
}

func (c *Client) handle(ctx context.Context) {
	for {
		select {
		case <-ctx.Done():
			log.Println("Client context cancel")
			return
		default:
			message, err := c.readLine()
			if err != nil {
				log.Println("Client read error", c.addr, " ", err)
				return
			}
			log.Println("Receive message from client:", message)
			c.callback(message)
		}
	}
}

func (c *Client) readLine() (message string, err error) {
	var lineBytes []byte
	var isPrefix bool
	for {
		lineBytes, isPrefix, err = c.reader.ReadLine()
		if err != nil {
			return
		}

		line := string(lineBytes)
		message += line
		if !isPrefix {
			return
		}
	}
}

func (c *Client) Close() error {
	log.Println("Client disconnected ", c.addr)
	c.cancelFunc()
	return c.Conn.Close()
}

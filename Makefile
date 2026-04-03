# ==================================================================================== #
# HELPERS
# ==================================================================================== #
## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

.PHONY: confirm
confirm:
	@echo -n 'Are you sure? [y/N] ' && read ans && [ $${ans:-N} = y ]


# ==================================================================================== #
# DEVELOPMENT
# ==================================================================================== #
## run: run the cmd application
.PHONY: run
run:
	go run main.go


# ==================================================================================== #
# BUILD
# ==================================================================================== #
## build/amd64: build the amd64 application
.PHONY: build/amd64
build/amd64:
	cd backend
	@echo 'Building cmd...'
	cd backend && go build -o=../bin/web .
	cd backend && GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build  -o=../bin/linux_amd64/web .

## build/arm64: build the arm64 application
.PHONY: build/arm64
build/arm64:
	cd backend
	@echo 'Building cmd...'
	cd backend && go build -o=../bin/web .
	cd backend && GOOS=linux GOARCH=arm64 CGO_ENABLED=0 go build  -o=../bin/linux_arm64/web .


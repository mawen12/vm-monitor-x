#!/bin/bash

set -euo pipefail

sh make-agent.sh

export GOOS=linux
export GOARCH=amd64
DIR="${GOOS}_${GOARCH}"
rm -rf build/${DIR}/*
mkdir -p build/${DIR}
echo "Building $DIR"
go build -o build/${DIR}/vm-monitor-x
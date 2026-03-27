#!/bin/bash

set -euo pipefail

echo "Build and run from Go source"

echo > log
DIR=`pwd`
echo "Dir: $DIR"

go build -o build/ ./cmd && echo Built && ./bin/vm-monitor-x $*

echo log:
tail -n 5 log
#!/bin/bash
#
# docker-entrypoint for service

set -e
echo "Executing java"
java "$@"


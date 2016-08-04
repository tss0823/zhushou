#!/bin/bash
#
git pull
mvn clean package -Dmaven.test.skip=true -P prod

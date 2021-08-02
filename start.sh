#!/bin/sh

git add .
git commit -m "backup"
git push
screen -m
cd spigot
./start.sh

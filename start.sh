#!/bin/sh

git add .
git commit -m "backup"
git push
screen
cd spigot
./start.sh

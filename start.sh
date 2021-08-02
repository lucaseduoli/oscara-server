#!/bin/sh

git add .
git commit -m "backup"
git push
cd spigot
./start.sh

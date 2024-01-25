#!/bin/bash

cd /usr/share/nginx/travelroad_spring
sudo git add .
sudo git commit -m "Changes"
sudo git push

ssh dimas@dimas.arkania.es "
  cd /home/dimas/travelroad_spring
  git pull
  systemctl --user restart travelroad.service
"

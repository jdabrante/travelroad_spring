#!/bin/bash

cd usr/share/nginx/travelroad_spring
sudo git add .
sudo git commit -m "Changes"
sudo git push

#./mvnw package  # el empaquetado ya incluye la compilación
# ↓ Último fichero JAR generado
#JAR=`ls target/*.jar -t | head -1`
#/usr/bin/java -jar $JAR

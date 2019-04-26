#!/bin/bash

sleep 10

until python /home/pi/Documents/AttentionReceiver/server.py; do
   echo "Restarting server...\n"
   sleep 1
done
import socket
import time
from twilio.rest import Client
import threading
import datetime
from string import printable
from yeelight import Bulb

t_end = 0 # Global to extend light blinking 

def activate_light():
	global t_end
	bulb = Bulb('[BULB IP ADDRESS]', effect='smooth', duration=1500)
	bulb.start_music()
	bulb.set_rgb(255, 0, 0)
	bulb.set_brightness(10)
	bulb.turn_on()
	t_end = time.time() + 60
	# Fade light
	while time.time() < t_end:
		bulb.set_brightness(75)
		time.sleep(2)
		bulb.set_brightness(10)
		time.sleep(2)
	bulb.turn_off()
	
def send_message():
	SID = '[TWILIO SID]' 
	TOKEN = '[TWILIO TOKEN]' 
	client = Client(SID, TOKEN)
	FROM = '[TWILIO PHONE NUMBER]'
	BODY = 'Attention Requested!'
	TO = '[RECIPIENT PHONE NUMBER]'
	message = client.messages.create(from_=FROM, body=BODY, to=TO)
	
def handle_request(conn):
	# Recieve message from connection
	msg = ''
	while True:
		data = conn.recv(16)
		if data:
			msg += data
		else:
			break # No more data
	msg = ''.join(char for char in msg if char in printable) # Fix string
	
	# Log request
	curr_time = datetime.datetime.now()
	log = 'From: %s at %s:\n%s\n\n' % (str(client_addr), str(curr_time), msg)
	with open('/home/pi/Documents/AttentionReceiver/logs.txt', 'a') as f: 
		f.write(log)
		
	conn.close()
	return msg

# Server
ADDR = '[RASPBERRY PI IP ADDRESS]'
PORT = [APPLICATION PORT NUMBER]
print('Creating socket...')
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_addr = (ADDR, PORT) # Address, Port
print('Binding...')
sock.bind(server_addr) 

sock.listen(1) # listen for incoming requests

while True:
	print("Waiting for a request...")
	conn, client_addr = sock.accept()
	print('Request recieved!\n')
	
	msg = handle_request(conn)
	if msg != 'gimme attention D:<':
		continue # Different request
		
	if 'light_thread' in locals() and light_thread.isAlive(): # Extend duration if already activated
		t_end = time.time() + 60
	else: # Start light thread
		light_thread = threading.Thread(target=activate_light)
		light_thread.start()
		send_message() # Send message on non extended requests

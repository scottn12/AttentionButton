# AttentionButton
This is an android application used to remotely notify myself if my girlfriend wants attention.

The AttentionButton part of this project contains the android application which sends a message to the receiver using TCP sockets if the button pressed. The background and icon of the app were replaced with stock images for privacy.

The AttentionReceiver part of this project runs on a Raspberry Pi, which starts a TCP server continuously waiting for requests. When a request is received a controllable YeeLight light bulb on my desk is activated. Additionally, a text message is sent to my phone using the Twilio API.

[Inspiration](https://github.com/jscd/Boyfriend-Alert)

# jOnix Client

jOnix Client application allows to control the way jOnix Service is used. It allows to generate a private key, which is needed in order to send messages to jOnix Service. Provides also a space for an organization name, which allows to filter the messages by organization name, to which the ONIX message is sent. Next, allows to specify one redirection address for jOnix Service, so that when a new message is received for the given organization name, it will be redirected further into the 3rd party http REST service. Provides a utility to send ONIX xml messages in a raw format to the local platform. Provides a way to browse the incoming messages for the locally specified organization name. 

# Running the app

In order to run it, you need to install java EE runtime supporting java EE7 (tested on glassfish v8). Next, the mongoDB should be installed on localhost on port 27017. 
jOnix Service must be installed before this application. After that, run the maven build script, build a war, and install it into your application server. 

# Notes

Absolutely requires jOnix Service installed before it in order to work, as long as this represents more a UI for the jOnix Service.

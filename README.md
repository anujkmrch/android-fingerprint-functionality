# android-fingerprint-functionality
## Base functionality
Download and use the android project, it comes with the login, logout, register functionality.

## Extended functionality
This project also has a functionality of reading sending fingerprint scan to the server.

##Explaination
Android devices can use internal fingerprint reader, but every devices fingerprint reader cannot not send images to the server.
To send images to the server you need to use external fingerprint reader.

And those fingerprint things are quite device specific if your vendor allows you to capture fingerprint and send it to server,
please go ahead and integrate it with the project

### Reasons why android internal fingerprint reader does not use send images to server
Acccording to my understanding, Android internal fingerprint reader, it is an internal security feature for phone only, allowing it to apply security features to the phone applications. Like in Apple X, there no fingerprint sensor, but for security feature it has face recognition. In future same thing may happen with the company, this is why I guess, google did not introduce the fingerprint sensor to send the fingerprint image to the server.

### Fingerprint functionality class
SetFingerPrint method allows you to send the fingerprint for the specific person and upload it to server

MatchFingerPrint method allows you to send the fingerprint for matching from the already set database.

Both the methods parse the json response and trigger the activity.

Thanks for VolleyMultipartReqeuest library which helps me to that.

Please go through with the licence for using this project in your academic project.

Please edit and use it in your project.

Enjoy.

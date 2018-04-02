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

###Fingerprint functionality class
SetFingerPrint method allows you to send the fingerprint for the specific person and upload it to server

MatchFingerPrint method allows you to send the fingerprint for matching from the already set database.

Both the methods parse the json response and trigger the activity.

Thanks for VolleyMultipartReqeuest library which helps me to that.

Please go through with the licence for using this project in your academic project.

Enjoy.

OpenFireMessenger - PHP Server Back-End
========

##Maintainers (Alphabetically)
* Alexander Martinz
* John Hoder

##Requirements
* A Webserver with PHP, MySQL and CURL Support

##Setup
To setup, first create a database.
Then enter your configuration in system/config.php
next call setup.php from your webbrowser.


##Testing
After setup, open test.html .
You can upload files with the first form.
With the second form you can send messages.
With the third form you can get the messages.


##Status Codes

#####General
* -1 ERROR
* 0 SUCCESS

#####Login
* 101 Authentication Error
* 102 Login Error

#####Registration
* 201 Username taken
* 202 Password weak
* 203 Email taken

#####Messaging
* 301 Contacts Error

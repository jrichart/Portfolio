Timeserver

Author: Joel Richart 2015
With help from: Ryan Warren, Morris Bernstein, and Allon Kim

This project was done to help understand the software engineering process, understand some
basic server functionality, and learn a new programming language (in this case Go). The project 
creates a web page which shows the current time in the user's time zone.
The user is able to login to show their user name with the time as well as a greeting
on the homepage. They are able to return before their cookie expires to continue to see
these messages, or to log out. 

There is functionality to monitor the requests made to both
the time and authorization servers. There is also functionality to start the authorization
server and multiple time servers on separate ports.

For testing purposes, there is also load generator to make a large number of requests to
an individual server.

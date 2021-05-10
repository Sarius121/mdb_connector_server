# mdb_connector_server
This server provides three POST-endpoints to connect to a mdb-file and execute queries.

# Used libraries
- Spring Boot and Security
- UCanAccess 5.0.1 and all its dependencies (http://ucanaccess.sourceforge.net/site.html)
- JSON-java (https://github.com/stleary/JSON-java)

# Installation notices
- this application is configured to be used with Jetty 9.4
- if this application is not on the same machine as the application connecting to it, the SecurityConfig has to be adjusted
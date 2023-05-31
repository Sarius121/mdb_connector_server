# mdb_connector_server
This server provides three POST-endpoints to connect to a mdb-file and execute queries.

# Notices
This is a very basic server which has only very few security mechanisms. Hence, it should not in any case be used in an unsafe environment and in the best case only one other program should have access to this server. Furthermore, I don't take any responsibility for security flaws and data loss or leakage. This app was only written for schild_webenm (https://github.com/Sarius121/schild_webenm) and it works quite safe and without errors together but I haven't written any tests for this program.

I have updated one dependability because it was vulnerable. However, I have only tested the most important functions afterwards. If something doesn't work, please open an issue and use the commit a1e159e until it is fixed. The vulnerability probably doesn't influence the security of this program much.

# Used libraries
- Spring Boot and Security
- UCanAccess 5.0.1 and all its dependencies (http://ucanaccess.sourceforge.net/site.html)
- JSON-java (https://github.com/stleary/JSON-java)

# Installation notices
- this application is configured to be used with Jetty 9.4
- if this application is not on the same machine as the application connecting to it, the SecurityConfig has to be adjusted
# Overview
A Java project with the purpose of recording my JDBC learnings.

# Dependencies
* MySQL 9.0.1 ([Link](https://dev.mysql.com/downloads/mysql/))
* MySQL ConnectorJ 9.0.0 ([Link](https://dev.mysql.com/downloads/connector/j/?os=26)) 

# Setting up the Java Connector lib
* Unzip the jar file in a folder (Default path: C:\Program Files\Java\java-libs\jdbc-connectors)
* Open Eclipse and create a new user lib (Window -> Preferences -> Java -> Build Path -> User Libraries -> New)
* Bind the Connector jar to the created lib (Add External JARs)
* Bind the created user lib to the project (Right click on project -> Properties -> Java Build Path -> Libraries -> Add Library -> User Library -> Next and choose the lib)

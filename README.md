# JavaAssemblyWithDocker
FUNCTIONALITY:

This project takes as input a file. The file should consist of one or more MV statements and only one "ADD"/"SHOW REG" operations.
Whenever SHOW REG statement is invoked, the program returns with the value of addition.

DEPLOYMENT INSTRUCTIONS:

Provide values for "MYSQL_ROOT_PASSWORD" and "MYSQL_PASSWORD" variables in the .env file.
Run command: "docker compose --env-file .env up"
The application can then be accessed at http:localhost:8080/addition.

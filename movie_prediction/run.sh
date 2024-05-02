#!/bin/bash

# move to milestone2
cd milestone2

# project bulid using Maven
mvn package

#run mongodb
mongod --fork --logpath /var/log/mongodb.log

# execute created JAR file
java -jar ./target/cse364-project-1.0-SNAPSHOT-with-dependencies.jar

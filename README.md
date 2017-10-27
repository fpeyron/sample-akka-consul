# Objective
===================

This project demonstrates a working example of cluster singleton with consul registry. it ensures exactly one actor of a certain type is running somewhere in the cluster. 


````
java -jar  \
 -Dconfig.file=src/main/resources/application.conf \
 -Dakka.remote.netty.tcp.port=2551 \
 -Dakka.cluster.http.management.port=5011 \
 -Dhttp.port=5001 \
 -Dconsul.hostname=localhost \
 -Dconsul.port=8501 \
 -Dconsul.agent-name=sample-consul1 
 ````

## Referencies
 
 An Sample application that leverages [Akka-cluster](akka.io) and uses [Constructr](https://github.com/hseeberger/constructr) to manage bootstrapping or joining in [Consul](consul.io) dataset.     
 
 
## Running

### Dependencies
Consul agent is necessary to test application. Docker image is a good choice to execute single locally :
 ```
 $ docker run -d -p 8500:8500 --name consul -e 'CONSUL_LOCAL_CONFIG={"skip_leave_on_interrupt": true}' consul agent -server -client=0.0.0.0 -bootstrap-expect=1 -data-dir=/tmp/consul -ui
 ```
 
### Commands
Clone to your computer and run via sbt:
 ```
 $ sbt run
 ```
 
 
Generates a directory with the Dockerfile and environment prepared for creating a Docker image.
 ```
 $ sbt docker:stage
 ```
 
Builds an image using the local Docker server.
 ```
 $ sbt docker:publishLocal
 ```
 
 Builds an image using the local Docker server, and pushes it to the configured remote repository.
 ```
 $ sbt docker:publish
 ```
 
## Deployment cluster as docker machine
 2 nodes Consul
 4 nodes Application
 ````
 $ docker-compose up
 ````
 
 
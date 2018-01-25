# Hazelcast Kubernetes Client

- [Introduction](#introduction)
- [Build](#build)
- [Deployment](#deployment)

# Introduction
A sample client for the [Hazelcast Kubernetes](https://github.com/hazelcast/hazelcast-docker/tree/master/hazelcast-kubernetes) project

# Build

## Prerequisites
- [Java JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-docker)
- A [Docker Hub](https://hub.docker.com/) account

Add a server definition for Docker Hub to maven settings.xml file like the one below:

```xml
  <server>
    <id>docker.io</id>
    <username>DOCKER_HUB_USERNAME</username>
    <password>DOCKER_HUB_PASSWORD</password>
  </server>
```

## Build Steps
Build the Spring Boot project

```shell
mvn clean install -Ddocker.io.user=<docker hub username>
```

Build Docker Image

```shell
mvn dockerfile:build -Ddocker.io.user=<docker hub username>
```

Push the Docker Image to Docker hub

```shell
mvn dockerfile:push -Ddocker.io.user=<docker hub username>
```
# Deployment
## Prerequisites
- Running [Kubernetes](https://kubernetes.io/) instance
    - For development and testing [minikube](https://github.com/kubernetes/minikube) can be used
- Kubernetes command line tool [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

## Deployment Steps
- Deploy a [Hazelcast Kubernetes](https://github.com/hazelcast/hazelcast-docker/tree/master/hazelcast-kubernetes) cluster to Kubernetes
- Modify the file named deployment.yaml under src/main/k8s and replace the prefix of the image with your docker hub username
- Deploy the Hazelcast Kubernetes Client image built in the [Build Steps](#buildSteps) of this project using the K8S files from src/main/k8s
  - `kubectl apply -f deployment.yaml`
  - `kubectl apply -f service.yaml`

## Testing

Once the project has been deployed to Kubernetes and it is running you can find the host and port where the project was deployed by executing

```shell
minikube service <SERVICE_NAME> --url
```

To send a sample payload to the client service:

 ```shell
 curl -XGET -H "Content-type: application/json" 'http://<hostname>:<port>/service?id=test'
 ```

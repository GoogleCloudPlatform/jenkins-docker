# <a name="about"></a>About

This image contains an installation Jenkins 2.x.

For more information, see the
[Official Image Launcher Page](https://console.cloud.google.com/launcher/details/google/jenkins2).

Pull command:

```shell
gcloud docker -- pull launcher.gcr.io/google/jenkins2
```

Dockerfile for this image can be found [here](https://github.com/GoogleCloudPlatform/jenkins-docker/tree/master/2).

# <a name="table-of-contents"></a>Table of Contents
* [Using Kubernetes](#using-kubernetes)
  * [Running Jenkins server](#running-jenkins-server-kubernetes)
    * [Starting a Jenkins instance](#starting-a-jenkins-instance-kubernetes)
  * [Configurations](#configurations-kubernetes)
    * [First log in](#first-log-in-kubernetes)
    * [Passing JVM arguments](#passing-jvm-arguments-kubernetes)
  * [Maintenance](#maintenance-kubernetes)
    * [Creating a Jenkins backup](#creating-a-jenkins-backup-kubernetes)
* [Using Docker](#using-docker)
  * [Running Jenkins server](#running-jenkins-server-docker)
    * [Starting a Jenkins instance](#starting-a-jenkins-instance-docker)
    * [Adding persistence](#adding-persistence-docker)
  * [Configurations](#configurations-docker)
    * [First log in](#first-log-in-docker)
    * [Passing JVM arguments](#passing-jvm-arguments-docker)
  * [Maintenance](#maintenance-docker)
    * [Creating a Jenkins backup](#creating-a-jenkins-backup-docker)
* [References](#references)
  * [Ports](#references-ports)
  * [Volumes](#references-volumes)

# <a name="using-kubernetes"></a>Using Kubernetes

## <a name="running-jenkins-server-kubernetes"></a>Running Jenkins server

This section describes how to spin up a Jenkins service using this image.

### <a name="starting-a-jenkins-instance-kubernetes"></a>Starting a Jenkins instance

Copy the following content to `pod.yaml` file, and run `kubectl create -f pod.yaml`.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: some-jenkins
  labels:
    name: some-jenkins
spec:
  containers:
    - image: launcher.gcr.io/google/jenkins2
      name: jenkins
```

Run the following to expose the ports:

```shell
kubectl expose pod some-jenkins --name some-jenkins-8080 \
  --type LoadBalancer --port 8080 --protocol TCP
kubectl expose pod some-jenkins --name some-jenkins-50000 \
  --type LoadBalancer --port 50000 --protocol TCP
```

To retain Jenkins data across container restarts, see [Adding persistence](#adding-persistence-kubernetes).

See [Configurations](#configurations-kubernetes) for how to customize your Jenkins service instance.

## <a name="configurations-kubernetes"></a>Configurations

### <a name="first-log-in-kubernetes"></a>First log in

View the generated administrator password to log in.

```shell
kubectl exec some-jenkins -- cat /var/jenkins_home/secrets/initialAdminPassword
```

### <a name="passing-jvm-arguments-kubernetes"></a>Passing JVM arguments

JVM arguments can be passed via environment variable `JAVA_OPTS`. For example, the following increases heap size to 2G and PermGen size to 128M.

Copy the following content to `pod.yaml` file, and run `kubectl create -f pod.yaml`.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: some-jenkins
  labels:
    name: some-jenkins
spec:
  containers:
    - image: launcher.gcr.io/google/jenkins2
      name: jenkins
      env:
        - name: "JAVA_OPTS"
          value: "-Xmx2G -XX:MaxPermSize=128m"
```

Run the following to expose the ports:

```shell
kubectl expose pod some-jenkins --name some-jenkins-8080 \
  --type LoadBalancer --port 8080 --protocol TCP
kubectl expose pod some-jenkins --name some-jenkins-50000 \
  --type LoadBalancer --port 50000 --protocol TCP
```

## <a name="maintenance-kubernetes"></a>Maintenance

### <a name="creating-a-jenkins-backup-kubernetes"></a>Creating a Jenkins backup

You can simply copy `/var/jenkins_home` directory on the container to `/path/to/your/jenkins/home` directory on your host.

```shell
kubectl cp some-jenkins:/var/jenkins_home /path/to/your/jenkins/home
```

# <a name="using-docker"></a>Using Docker

## <a name="running-jenkins-server-docker"></a>Running Jenkins server

This section describes how to spin up a Jenkins service using this image.

### <a name="starting-a-jenkins-instance-docker"></a>Starting a Jenkins instance

Use the following content for the `docker-compose.yml` file, then run `docker-compose up`.

```yaml
version: '2'
services:
  jenkins:
    container_name: some-jenkins
    image: launcher.gcr.io/google/jenkins2
    ports:
      - '8080:8080'
      - '50000:50000'
```

Or you can use `docker run` directly:

```shell
docker run \
  --name some-jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -d \
  launcher.gcr.io/google/jenkins2
```

Jenkins server is accessible on port 8080.

To retain Jenkins data across container restarts, see [Adding persistence](#adding-persistence-docker).

See [Configurations](#configurations-docker) for how to customize your Jenkins service instance.

### <a name="adding-persistence-docker"></a>Adding persistence

All Jenkins data lives in `/var/jenkins_home`, including plugins and configurations. This directory should be mounted on a persistent volume to survive container restarts.

Assume `/path/to/jenkins/home` is the persistent directory on your host.

Use the following content for the `docker-compose.yml` file, then run `docker-compose up`.

```yaml
version: '2'
services:
  jenkins:
    container_name: some-jenkins
    image: launcher.gcr.io/google/jenkins2
    ports:
      - '8080:8080'
      - '50000:50000'
    volumes:
      - /path/to/jenkins/home:/var/jenkins_home
```

Or you can use `docker run` directly:

```shell
docker run \
  --name some-jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v /path/to/jenkins/home:/var/jenkins_home \
  -d \
  launcher.gcr.io/google/jenkins2
```

## <a name="configurations-docker"></a>Configurations

### <a name="first-log-in-docker"></a>First log in

View the generated administrator password to log in.

```shell
docker exec some-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### <a name="passing-jvm-arguments-docker"></a>Passing JVM arguments

JVM arguments can be passed via environment variable `JAVA_OPTS`. For example, the following increases heap size to 2G and PermGen size to 128M.

Use the following content for the `docker-compose.yml` file, then run `docker-compose up`.

```yaml
version: '2'
services:
  jenkins:
    container_name: some-jenkins
    image: launcher.gcr.io/google/jenkins2
    environment:
      "JAVA_OPTS": "-Xmx2G -XX:MaxPermSize=128m"
    ports:
      - '8080:8080'
      - '50000:50000'
```

Or you can use `docker run` directly:

```shell
docker run \
  --name some-jenkins \
  -e "JAVA_OPTS=-Xmx2G -XX:MaxPermSize=128m" \
  -p 8080:8080 \
  -p 50000:50000 \
  -d \
  launcher.gcr.io/google/jenkins2
```

## <a name="maintenance-docker"></a>Maintenance

### <a name="creating-a-jenkins-backup-docker"></a>Creating a Jenkins backup

You can simply copy `/var/jenkins_home` directory on the container to `/path/to/your/jenkins/home` directory on your host.

```shell
docker cp some-jenkins:/var/jenkins_home /path/to/your/jenkins/home
```

# <a name="references"></a>References

## <a name="references-ports"></a>Ports

These are the ports exposed by the container image.

| **Port** | **Description** |
|:---------|:----------------|
| TCP 8080 | Jenkins console port. |
| TCP 50000 | Slave agent communication port. |

## <a name="references-volumes"></a>Volumes

These are the filesystem paths used by the container image.

| **Path** | **Description** |
|:---------|:----------------|
| /var/jenkins_home | Stores all of Jenkins plugins and configurations. |

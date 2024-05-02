# Use Ubuntu 22.04 as base image
FROM ubuntu:22.04

# Install prerequisites
RUN apt-get update \
    && apt-get install -y wget gnupg2 \
    && rm -rf /var/lib/apt/lists/*


# Import MongoDB public GPG key
RUN wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | apt-key add -

# Add MongoDB repository to sources list
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-6.0.list

# Update package list and install MongoDB
RUN apt-get update \
    && apt-get install -y mongodb-org \
    && rm -rf /var/lib/apt/lists/*

# Create necessary directories
RUN mkdir -p /data/db /data/configdb

# Change ownership of directories
RUN chown -R mongodb:mongodb /data/db /data/configdb

# Add your stuff below:

EXPOSE 8080

CMD ["mongod", "--fork", "--logpath", "/var/log/mongodb.log"]

RUN apt-get update && apt-get install -y \
    vim \
    default-jre \
    default-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*


RUN apt-get update && apt-get install -y openssh-server
RUN apt-get install -y vim git openjdk-17-jdk maven
RUN apt-get install -y sudo
RUN apt-get install -y systemctl
RUN apt-get install -y curl
RUN apt-get install -y git
RUN apt-get install -y python3.10

RUN apt-get update && apt-get install -y python3-pip

RUN update-alternatives --install /usr/bin/python python /usr/bin/python3.10 1

RUN pip install pandas
RUN pip install pandas joblib
RUN pip install pandas joblib scikit-learn

#work directory setting
WORKDIR /root/project

#Maven project and run.sh
COPY milestone2 /root/project/milestone2
COPY run.sh /root/project/run.sh

#run bash shell
CMD ["/bin/bash"]
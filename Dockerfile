FROM centos:latest 

ENV INTELLIJ_VERSION 2018.2.4

RUN yum update -y && \
    yum install -y curl java-1.8.0-openjdk java-1.8.0-openjdk-devel which git vim && \
    yum clean all -y
RUN useradd intellij  
RUN curl http://download-cf.jetbrains.com/idea/ideaIC-${INTELLIJ_VERSION}.tar.gz > /opt/ideaIC-${INTELLIJ_VERSION}.tar.gz
RUN cd /opt && \
    tar xvzf ideaIC-${INTELLIJ_VERSION}.tar.gz && \
    rm -f ideaIC-${INTELLIJ_VERSION}.tar.gz && \
    mv /opt/idea* /opt/idea && \
    chown -R intellij:intellij /opt/idea
ENV IDEA_JDK /usr/lib/jvm/java

USER intellij

ENTRYPOINT [ "/opt/idea/bin/idea.sh" ]

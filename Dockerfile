FROM openjdk:8

MAINTAINER xiaoke-kube@mail.com

RUN cp /etc/hosts /tmp/hosts

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

RUN mkdir -p /xiaoke-kube

WORKDIR /xiaoke-kube

EXPOSE 8089

ADD ./xiaoke-kube-main/target/xiaoke-kube-main-1.0.0.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar xiaoke-kube-main-1.0.0.jar
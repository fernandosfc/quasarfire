FROM openjdk:oraclelinux7
RUN yum -y check-update || { rc=$?; [ "$rc" -eq 100 ] && exit 0; exit "$rc"; }
#RUN yum -y install tomcat
RUN yum -y install git
RUN yum -y install wget
RUN mkdir -p /files/maven-files
WORKDIR /files/maven-files
RUN wget https://apache.dattatec.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar xvf apache-maven-3.6.3-bin.tar.gz
RUN mv apache-maven-3.6.3  /files/apache-maven
ENV M2_HOME=/files/apache-maven
ENV M2=$M2_HOME/bin
ENV OLD_PATH=$PATH
ENV PATH=$M2:$PATH
RUN mkdir /files/quasarfire
WORKDIR /files/quasarfire
RUN git clone http://fernandosfc:5hyJcF8Sc2@github.com/fernandosfc/quasarfire.git
WORKDIR /files/quasarfire/quasarfire
RUN mvn clean install
#RUN cp target/quasarfire-0.0.1-SNAPSHOT.war /var/lib/tomcat/webapps/quasarfire.war
RUN mkdir /quasarfire
RUN cp target/quasarfire-0.0.1-SNAPSHOT.war /quasarfire/quasarfire.war
EXPOSE 8080

#purge
RUN yum -y remove git
RUN yum -y remove wget
RUN rm -r /files
RUN rm -r /root/.m2
ENV PATH=$OLD_PATH
RUN unset OLD_PATH;
RUN unset M2; unset M2_HOME;
#RUN yum -y autoremove
RUN yum clean all

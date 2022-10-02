FROM registry.access.redhat.com/ubi8/openjdk-11
COPY target/*.jar /opt/rates1-ver1.jar
COPY ocp/deployments/certs/root.crt /opt/root.crt
CMD java -jar /opt/rates1-ver1.jar
EXPOSE 9080

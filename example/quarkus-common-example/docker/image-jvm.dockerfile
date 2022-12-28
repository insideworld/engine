FROM registry.access.redhat.com/ubi9/ubi-minimal:9.1.0

ARG JAVA_PACKAGE=java-17-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' TZ="Europe/Moscow"

RUN microdnf install ca-certificates ${JAVA_PACKAGE} -y \
    && microdnf update -y  \
    && microdnf clean all -y  \
    && mkdir /deployments \
    && chown 1001 /deployments \
    && chmod "g+rwX" /deployments \
    && chown 1001:root /deployments \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh \
    && chown 1001 /deployments/run-java.sh \
    && chmod 540 /deployments/run-java.sh \
    && echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Xmx512m -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

COPY /build/quarkus-app/lib /deployments/lib/
COPY /build/quarkus-app/app /deployments/app/
COPY /build/quarkus-app/quarkus /deployments/quarkus
COPY /build/quarkus-app/quarkus-run.jar /deployments/app.jar

USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]
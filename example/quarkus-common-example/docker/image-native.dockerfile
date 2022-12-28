FROM registry.access.redhat.com/ubi9/ubi-minimal:9.1.0

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' TZ="Europe/Moscow"

WORKDIR /work/
COPY /build/*-runner /work/application
COPY /build/classes/localhost.p12 ./localhost.p12

RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work



CMD ["./application", "-Xmx512m"]

#The Citadel application image
FROM openjdk:17-alpine
COPY ../build/distributions/citadel.zip citadel.zip
RUN unzip citadel.zip
EXPOSE 8080
ENTRYPOINT citadel/bin/citadel
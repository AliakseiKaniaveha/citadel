########################################
# The Citadel application image        #
########################################

# OS and JDK
FROM openjdk:17-alpine

# Graphviz
RUN apk add --update --no-cache graphviz

# The Citadel app
COPY ../build/distributions/citadel.zip citadel.zip
RUN unzip citadel.zip
EXPOSE 8080
ENTRYPOINT citadel/bin/citadel
########################################
# The Citadel application image        #
########################################
#docker build -f containers/citadel-app.dockerfile -t citadel-app .

#
# OS and JDK
#
FROM openjdk:17-alpine

#
# Graphviz
#
RUN apk add --update --no-cache graphviz

#
# The Citadel app
#
# deploy
COPY ./build/distributions/citadel.zip citadel.zip
RUN unzip citadel.zip
# expose application port
EXPOSE 8080
# expose debug port
EXPOSE 5050
# allows to set app options:
ENV CITADEL_OPTS ''
# run app
ENTRYPOINT citadel/bin/citadel
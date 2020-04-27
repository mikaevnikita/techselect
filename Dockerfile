FROM adoptopenjdk/openjdk11:alpine-jre

ADD target/techselect-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT exec java $JAVA_OPTS -Dvaadin.productionMode -jar /app.jar
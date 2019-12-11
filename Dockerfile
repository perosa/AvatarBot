FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /software/avatarBot

ADD src/main/resources/avatars /software/avatarBot/avatars
ADD target/avatarBot.jar /software/avatarBot/avatarBot.jar

CMD java -Dserver.port=$PORT $JAVA_OPTS -jar /software/avatarBot/avatarBot.jar


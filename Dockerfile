FROM bellsoft/liberica-openjdk-alpine:11
RUN apk add tzdata
WORKDIR /usr/src/catyuriibot/
COPY ./target/catyuriibot.jar .
COPY ./img ./img
CMD java -Dbot.token=${BOT_TOKEN} -Dbot.chatid=${CHAT_ID} -Dbot.lastTime=${LAST_TIME} -jar catyuriibot.jar

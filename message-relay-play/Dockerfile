FROM openjdk:8-jre

ADD ./target/universal/*.tgz /opt/app
RUN mv /opt/app/* /opt/app/app

EXPOSE 9000
CMD /opt/app/app/bin/message-relay-play -Dplay.crypto.secret=asdlfahjsdfansdjkgnalkdsgjalsdkfjalsdkfjnfgngnbmbvzkdhoije909849wiugflg

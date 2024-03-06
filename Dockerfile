FROM openjdk:17-alpine
# 复制生成的 jar 文件到容器中
COPY target/*.jar /app/app.jar
# 设置工作目录
WORKDIR /app
# 暴露端口
EXPOSE 8888
# 设置时区为 Asia/Shanghai
ENV TZ=Asia/Shanghai
#设置字体库和时区
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk add -U fontconfig && rm -rf /var/cache/apk/*  && \
  ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JVM="-Xmx212m -XX:+HeapDumpOnOutOfMemoryError"

ENV CE="prod"


# 定义启动命令
ENTRYPOINT ["sh","-c","java $JVM -Dspring.profiles.active=$CE -jar /app/app.jar"]

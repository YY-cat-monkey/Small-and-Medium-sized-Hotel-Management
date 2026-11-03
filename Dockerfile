# ---- 构建阶段 ----
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /build

# 先拉依赖，利用 Docker 层缓存
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- 运行阶段 ----
FROM eclipse-temurin:8-jre-jammy
WORKDIR /app

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-XX:MaxRAMPercentage=60 -XX:+UseSerialGC -Xss512k -Dfile.encoding=UTF-8"

COPY --from=build /build/target/hotel-management-system-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]

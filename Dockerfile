FROM alpine:latest AS build
RUN apk add --no-cache --update openjdk17-jdk
USER 1000
WORKDIR /source
COPY . .
RUN ./gradlew build assembleDist

FROM alpine:latest
RUN apk add --no-cache --update inkscape openjdk17-jre
USER 1000
WORKDIR /app
ADD --from=build /source/build/distributions/badge-server.tar .
CMD ["/app/badge-server/bin/badge-server"]

FROM docker.io/archlinux/archlinux:base AS build
RUN pacman -Syu --noconfirm jdk17-openjdk && mkdir -p /gradle && rm -rf /var/cache/pacman/pkg
WORKDIR /source
COPY . .
RUN chown -R 1000 /source /gradle
USER 1000
ENV GRADLE_USER_HOME=/gradle JAVA_HOME=/usr/lib/jvm/default
RUN --mount=type=cache,destination=/gradle,sharing=locked,uid=1000 ./gradlew build assembleDist
WORKDIR /dist
RUN tar xvf /source/build/distributions/badge-server.tar

FROM docker.io/archlinux/archlinux:base
RUN pacman -Syu --noconfirm inkscape inter-font jre17-openjdk && rm -rf /var/cache/pacman/pkg
WORKDIR /app
COPY --from=build /dist .
RUN chown -R 1000 /app
USER 1000
CMD ["/app/badge-server/bin/badge-server"]

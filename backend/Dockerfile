FROM openjdk:25-jdk-slim AS builder

RUN apt-get update && apt-get install -y curl bash rlwrap && \
    curl -fsSL https://download.clojure.org/install/linux-install.sh | bash && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY backend/deps.edn ./deps.edn
RUN clojure -P

COPY backend/src ./src
COPY backend/resources ./resources
COPY common ./common
COPY backend/build.clj ./build.clj

RUN clojure -T:build uberjar

FROM openjdk:25-jdk-slim

WORKDIR /app
COPY --from=builder /app/target/backend.jar ./backend.jar

EXPOSE 8080

CMD ["java", "-cp", "backend.jar", "clojure.main", "-m", "yappinator.core"]

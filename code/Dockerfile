# ---------- ขั้นที่ 1: build ไฟล์ jar ด้วย Maven + JDK 17 ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy pom.xml ก่อน เพื่อให้ Docker จำ (cache) การโหลด dependency ไว้ แก้โค้ดแล้ว build ซ้ำไม่ต้องโหลดใหม่
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
# ข้าม test ตอน build image เพราะ MembershipApplicationTests ต้องต่อฐานข้อมูลจริง (ซึ่งยังไม่มีในขั้นตอน build)
RUN mvn -B package -DskipTests

# ---------- ขั้นที่ 2: รันแอปด้วย JRE 17 (image เล็กกว่า ไม่มี Maven/ซอร์สโค้ด) ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

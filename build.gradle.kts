plugins {
    id("java")
    id("war")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 1. Servlet & JSP API - Dùng compileOnly để IntelliJ lấy làm tài liệu gợi ý
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    compileOnly("javax.servlet.jsp:javax.servlet.jsp-api:2.3.3")

    // 2. JSTL cho Java EE 8 trở xuống
    implementation("javax.servlet:jstl:1.2")

    // 3. MySQL & Testing (Giữ nguyên của bạn)
    implementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // 4. Tomcat Embed cho việc chạy trực tiếp (Nếu bạn dùng Spring Boot hoặc chạy main)
    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.89")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:9.0.89")

    // Thay vì dùng jakarta.el, hãy dùng javax.el đồng bộ với Tomcat 9
    implementation("org.glassfish:javax.el:3.0.1-b12")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

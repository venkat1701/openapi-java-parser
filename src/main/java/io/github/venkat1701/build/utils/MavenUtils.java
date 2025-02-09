package io.github.venkat1701.build.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for generating the pom.xml file. Contains the map of dependencies that we can load within the generated project.
 * @author Venkat
 */
public class MavenUtils {
    private static final Map<String, String> DEPENDENCIES = new LinkedHashMap<>();

    // Required Dependencies for the Application. Moving to v1, we will enhance this process.
    static {
        DEPENDENCIES.put("retrofit", """
                <dependency>
                    <groupId>com.squareup.retrofit2</groupId>
                    <artifactId>retrofit</artifactId>
                    <version>2.9.0</version>
                </dependency>
                """);
        DEPENDENCIES.put("jackson", """
                <dependency>
                    <groupId>com.fasterxml.jackson.core</groupId>
                    <artifactId>jackson-databind</artifactId>
                    <version>2.15.0</version>
                </dependency>
                """);
        DEPENDENCIES.put("okhttp", """
                <dependency>
                    <groupId>com.squareup.okhttp3</groupId>
                    <artifactId>okhttp</artifactId>
                    <version>4.9.3</version>
                </dependency>
                """);
        DEPENDENCIES.put("webclient", """
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-webflux</artifactId>
                    <version>3.1.1</version>
                </dependency>
                """);

        DEPENDENCIES.put("slf4j", """
                <dependency>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                    <version>2.0.7</version>
                </dependency>
                """);

        DEPENDENCIES.put("junit", """
                <dependency>
                    <groupId>org.junit.jupiter</groupId>
                    <artifactId>junit-jupiter-api</artifactId>
                    <version>5.9.1</version>
                    <scope>test</scope>
                </dependency>
                """);

    }

    /**
     * Generates a Maven pom.xml file with the selected dependencies.
     *
     * @param selectedDeps The list of dependencies chosen by the user.
     * @return A string containing the formatted pom.xml content.
     */
    public static String generatePomXml(List<String> selectedDeps) {
        return """
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>generated-api</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <dependencies>
                        %s
                    </dependencies>
                </project>
                """.formatted(generateMavenDependencyList(selectedDeps));
    }

    /**
     * Builds the dependencies list in XML format for inclusion in pom.xml.
     *
     * @param selectedDeps The list of dependencies chosen by the user.
     * @return A string containing the formatted dependency list.
     */
    public static String generateMavenDependencyList(List<String> selectedDeps) {
        StringBuilder chosenDependencies = new StringBuilder();
        for (String dep : selectedDeps) {
            if (DEPENDENCIES.containsKey(dep.trim())) {
                chosenDependencies.append(DEPENDENCIES.get(dep.trim())).append("\n");
            } else {
                System.out.println("Warning: Unknown dependency '" + dep + "'");
            }
        }
        return chosenDependencies.toString();
    }
}

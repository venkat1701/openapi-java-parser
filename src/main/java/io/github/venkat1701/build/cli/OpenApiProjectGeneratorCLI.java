package io.github.venkat1701.build.cli;

import io.github.venkat1701.build.utils.okhttp.OkHttpApiClientGenerator;
import io.github.venkat1701.build.utils.FileUtils;
import io.github.venkat1701.build.utils.MavenUtils;
import io.github.venkat1701.build.utils.retrofit.RetrofitApiClientGenerator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * CLI Tool to generate the OpenAPI Client SDK
 *
 * @author Venkat
 */
public class OpenApiProjectGeneratorCLI {

    public static void main(String[] args) {
        startGenerator(args);
    }

    /**
     * Driver method to start generating the project file
     * @param args Array of arguments to trigger the method.
     */
    public static void startGenerator(String[] args) {
        Options options = new Options();
        options.addOption("s", "spec", true, "Path to OpenAPI YAML file");
        options.addOption("o", "output", true, "Output Project Directory");
        options.addOption("d", "dependencies", true, "Comma-separated list of dependencies (retrofit, okhttp, jackson, webclient, slf4j, junit)");
        options.addOption("t", "type", true, "API Wrapper Type (retrofit/okhttp)");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            System.err.println("Error parsing CLI arguments: " + e.getMessage());
            return;
        }

        String specPath = cmd.getOptionValue("spec");
        String outputDirectory = cmd.getOptionValue("output", "api-wrapper");
        String selectedDeps = cmd.getOptionValue("dependencies", "retrofit,jackson,slf4j,junit");
        String wrapperType = cmd.getOptionValue("type", "retrofit"); // Default to Retrofit

        if (specPath == null || outputDirectory == null || selectedDeps == null || wrapperType == null) {
            System.out.println("Incomplete details specified. Use -h for help.");
            return;
        }
        try {
            performUtils(specPath, outputDirectory, selectedDeps, wrapperType);
        } catch (IOException e) {
            System.err.println("Error generating project: " + e.getMessage());
        }
    }


    private static void performUtils(String specPath, String outputDirectory, String selectedDeps, String wrapperType) throws IOException {
        OpenAPI openAPI = new OpenAPIV3Parser().read(specPath);
        if (Objects.isNull(openAPI)) {
            System.out.println("Invalid OpenAPI File");
            return;
        }
        List<String> dependenciesSelected = Arrays.asList(selectedDeps.split(","));
        FileUtils.createProjectStructure(outputDirectory);
        FileUtils.writeFile(outputDirectory + "/pom.xml", MavenUtils.generatePomXml(dependenciesSelected));
        FileUtils.writeFile(outputDirectory + "/src/main/java/com/example/MainApplication.java", generateMainClass());
        if ("retrofit".equalsIgnoreCase(wrapperType)) {
            generateRetrofitApiWrapper(openAPI, outputDirectory);
        } else if ("okhttp".equalsIgnoreCase(wrapperType)) {
            generateOkHttpApiWrapper(openAPI, outputDirectory);
        } else {
            System.out.println("Invalid API Wrapper type specified. Use 'retrofit' or 'okhttp'.");
        }

        System.out.println("Project generated successfully in: " + outputDirectory);
    }

    private static String generateMainClass() {
        return """
            package com.example;
            
            public class MainApplication {
                public static void main(String[] args) {
                    System.out.println("API Client Generated!");
                }
            }
            """;
    }

    /**
     * Generates a Retrofit-based API Wrapper
     */
    private static void generateRetrofitApiWrapper(OpenAPI openAPI, String outputDir) throws IOException {
        RetrofitApiClientGenerator.generateRetrofitApiWrapper(openAPI, outputDir);
    }

    /**
     * Generates an OkHttp-based API Wrapper
     */
    private static void generateOkHttpApiWrapper(OpenAPI openAPI, String outputDir) throws IOException {
        OkHttpApiClientGenerator.generateApiWrapper(openAPI, outputDir + "/src/main/java");
    }
}

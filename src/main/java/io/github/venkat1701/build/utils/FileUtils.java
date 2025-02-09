package io.github.venkat1701.build.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void createProjectStructure(String outputDirectory) {
        new File(outputDirectory+"/src/main/java/com/example").mkdirs();
        new File(outputDirectory+"/src/main/java/com/example/api").mkdirs();
    }

    public static void writeFile(String path, String content) throws IOException {
        var writer = new FileWriter(path);
        writer.write(content);
        writer.close();
    }
}

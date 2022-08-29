package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtil {

    private FileUtil() {
    }

    public static String readFromFile(String pathToFile) {
        if (pathToFile == null || pathToFile.isEmpty()) {
            return null;
        }
        try {
            return Files.lines(Paths.get(pathToFile)).collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
package com.teame.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for simple CSV read/write operations.
 */
public class CsvUtils {

    /**
     * Read all lines from a CSV file.
     * Returns a list of strings (each representing a row).
     */

    public static List<String> readAllLines(String filePath) {
        try {
            Path path = Path.of(filePath);

            // If the file doesn't exist, just return empty – DO NOT create anything here
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }
    }


    public static List<String> readAllLines(String filePath, String headerRow) {
        try {
            Path path = Path.of(filePath);

            // If file does NOT exist → create it with the header
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());

                List<String> initContent = new ArrayList<>();
                initContent.add(headerRow);
                Files.write(path, initContent);

                return new ArrayList<>(); // no data rows yet
            }

            // File exists → read normally
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }
    }



    /**
     * Write a list of lines to a CSV file, overwriting any existing content.
     */
    public static void writeAllLines(String filePath, List<String> lines) {
        try {
            Files.write(Path.of(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file: " + filePath, e);
        }
    }

    /**
     * Append a single line (representing one CSV row) to a CSV file.
     */
    public static void appendLine(String filePath, String line) {
        try {
            List<String> singleLine = new ArrayList<>();
            singleLine.add(line);

            Files.write(Path.of(filePath), singleLine, java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException("Error appending row to CSV file: " + filePath, e);
        }
    }
}

package com.gromoks.filestatistic;

import com.gromoks.filestatistic.dao.io.FileParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Console {
    public static void main(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter file path:");
            Path path = Paths.get(bufferedReader.readLine());

            if (Files.exists(path)) {
                File file = new File(path.toFile().getAbsolutePath());
                FileParser fileParser = new FileParser();
                String wordDelimiter = "\\s+";
                fileParser.parse(file, wordDelimiter);
            } else {
                System.out.println("Directory doesn't exists");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

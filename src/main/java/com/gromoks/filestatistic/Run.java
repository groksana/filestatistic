package com.gromoks.filestatistic;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.dao.io.InputData;
import com.gromoks.filestatistic.service.FileProcessing;
import com.gromoks.filestatistic.dao.jdbc.JdbcStatisticDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Run {

    private static FileProcessing fileProcessing;

    public static void main(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter file path:");
            Path path = Paths.get(bufferedReader.readLine());

            if (Files.exists(path)) {
                init();
                String wordDelimiter = "\\s+";
                fileProcessing.process(path, wordDelimiter);
            } else {
                System.out.println("Directory doesn't exists");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        InputData inputData = new InputData();
        JdbcConnection jdbcConnection = new JdbcConnection();
        JdbcStatisticDao jdbcStatisticDao = new JdbcStatisticDao(jdbcConnection);
        fileProcessing = new FileProcessing(inputData, jdbcStatisticDao);
    }
}

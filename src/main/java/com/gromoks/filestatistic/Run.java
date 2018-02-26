package com.gromoks.filestatistic;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.dao.io.InputData;
import com.gromoks.filestatistic.dao.jdbc.JdbcStatisticDao;
import com.gromoks.filestatistic.service.FileProcessing;
import com.gromoks.filestatistic.service.statistic.FileAnalyzer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Run {

    private static final Logger log = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        CommandLine commandLine;
        Option option_p = Option.builder("p").argName("option").hasArg().desc("The p option").build();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        options.addOption(option_p);

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("p")) {
                Path path = Paths.get(commandLine.getOptionValue("p"));

                if (Files.exists(path)) {
                    FileProcessing fileProcessing = initFileProcessing();
                    String wordDelimiter = "\\s+";
                    fileProcessing.process(path, wordDelimiter);
                } else {
                    log.error("Directory doesn't exists: {}", path.toString());
                    throw new IllegalArgumentException("Directory doesn't exists: " + path.toString());
                }
            } else {
                log.error("Input line is incorrect");
                throw new IllegalArgumentException("Input line is incorrect");
            }
        } catch (ParseException e) {
            log.error("Parse error: " + e.getMessage());
            throw new IllegalArgumentException("Parse error: " + e.getMessage());
        }
    }

    private static FileProcessing initFileProcessing() {
        InputData inputData = new InputData();
        FileAnalyzer fileAnalyzer = new FileAnalyzer();
        JdbcConnection jdbcConnection = new JdbcConnection();
        JdbcStatisticDao jdbcStatisticDao = new JdbcStatisticDao(jdbcConnection);
        FileProcessing fileProcessing = new FileProcessing(inputData, fileAnalyzer, jdbcStatisticDao);

        return fileProcessing;
    }
}

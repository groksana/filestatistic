package com.gromoks.filestatistic.dao.io;

import com.gromoks.filestatistic.dao.jdbc.JdbcFileStatisticDao;
import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;
import com.gromoks.filestatistic.service.statistic.FileStatistic;
import com.gromoks.filestatistic.service.statistic.LineStatistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FileParser {

    public void parse(File file, String wordDelimiter) {
        BufferedReader bufferedReader = null;

        try {
            Path path = Paths.get(file.getAbsolutePath());
            bufferedReader = Files.newBufferedReader(path);
        } catch (IOException exception) {
            System.out.println("Error occurred while trying to read the file");
            System.exit(0);
        }

        Stream<String> lines = bufferedReader.lines();
        AtomicInteger i = new AtomicInteger(0);

        List<LineInfo> lineInfos = lines.map(line -> {
            i.getAndIncrement();
            LineInfo lineInfo = LineStatistic.getLineStatistic(line, i.get(), wordDelimiter);
            System.out.println(lineInfo);
            return lineInfo;
        }).collect(toList());

        FileInfo fileInfo = FileStatistic.getByLineStatistic(file.getName(), lineInfos);
        System.out.println(fileInfo);

        JdbcFileStatisticDao jdbcFileStatisticDao = new JdbcFileStatisticDao();
        try {
            jdbcFileStatisticDao.addStatistic(fileInfo, lineInfos);
        } catch (SQLException e) {
            System.out.println("Statistic can't be added for file: " + fileInfo.getFileName());
        }
    }

}


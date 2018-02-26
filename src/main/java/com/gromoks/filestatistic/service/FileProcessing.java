package com.gromoks.filestatistic.service;

import com.gromoks.filestatistic.dao.io.InputData;
import com.gromoks.filestatistic.dao.jdbc.JdbcStatisticDao;
import com.gromoks.filestatistic.entity.FileStatistic;
import com.gromoks.filestatistic.service.statistic.FileAnalyzer;

import java.io.InputStream;
import java.nio.file.Path;

public class FileProcessing {

    private JdbcStatisticDao jdbcStatisticDao;

    private InputData inputData;

    private FileAnalyzer fileAnalyzer;

    public FileProcessing(InputData inputData, FileAnalyzer fileAnalyzer, JdbcStatisticDao jdbcStatisticDao) {
        this.inputData = inputData;
        this.fileAnalyzer = fileAnalyzer;
        this.jdbcStatisticDao = jdbcStatisticDao;
    }

    public void process(Path path, String wordDelimiter) {
        InputStream lines = inputData.get(path);
        FileStatistic fileStatistic = fileAnalyzer.getFileStatistic(path.getFileName().toString(), lines, wordDelimiter);
        jdbcStatisticDao.addStatistic(fileStatistic);
        fileStatistic.print();
    }

}


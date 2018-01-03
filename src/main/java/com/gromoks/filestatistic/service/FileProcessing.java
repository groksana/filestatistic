package com.gromoks.filestatistic.service;

import com.gromoks.filestatistic.dao.io.InputData;
import com.gromoks.filestatistic.dao.jdbc.JdbcStatisticDao;
import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;
import com.gromoks.filestatistic.service.statistic.FileStatistic;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public class FileProcessing {

    private JdbcStatisticDao jdbcStatisticDao;

    private InputData inputData;

    public FileProcessing(InputData inputData, JdbcStatisticDao jdbcStatisticDao) {
        this.inputData = inputData;
        this.jdbcStatisticDao = jdbcStatisticDao;
    }

    public void process(Path path, String wordDelimiter) {
        InputStream lines = inputData.get(path);
        List<LineInfo> lineInfos = FileStatistic.getLinesStatistic(lines, wordDelimiter);
        FileInfo fileInfo = FileStatistic.getAggregateLineStatistic(path.getFileName().toString(), lineInfos);
        jdbcStatisticDao.addStatistic(fileInfo, lineInfos);
    }

}


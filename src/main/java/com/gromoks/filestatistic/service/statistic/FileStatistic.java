package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FileStatistic {
    public static FileInfo getByLineStatistic(String fileName, List<LineInfo> lineInfos) {
        FileInfo fileInfo = new FileInfo();

        int length = lineInfos.stream().mapToInt(LineInfo::getRowLength).sum();
        double average = lineInfos.stream().mapToDouble(LineInfo::getAverageWordLength).average().orElseGet(null);

        List<String> longestWord = new ArrayList<>(lineInfos.stream().map(LineInfo::getLongestWords)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(String::length, TreeMap::new, toList()))
                .lastEntry()
                .getValue());

        List<String> shortestWord = new ArrayList<>(lineInfos.stream().map(LineInfo::getShortestWords)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(String::length, TreeMap::new, toList()))
                .firstEntry()
                .getValue());

        fileInfo.setFileName(fileName);
        fileInfo.setLongestWords(longestWord);
        fileInfo.setShortestWords(shortestWord);
        fileInfo.setLength(length);
        fileInfo.setAverageWordLength(average);

        return fileInfo;
    }
}


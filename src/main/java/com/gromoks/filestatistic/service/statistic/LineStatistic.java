package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.LineInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineStatistic {
    public static LineInfo getLineStatistic(String line, int rowNumber, String wordDelimiter) {
        List<String> list = new ArrayList<>(Arrays.asList(line.split(wordDelimiter)));
        LineInfo lineInfo = new LineInfo();

        List<String> longestWords = new ArrayList<>();
        List<String> shortestWords = new ArrayList<>();

        int index = 0;
        String longestWord = "";
        String shortestWord = "";
        int length = 0;
        for (String row : list) {
            index++;
            if (index == 1) {
                longestWord = row;
                shortestWord = row;
                longestWords.add(row);
                shortestWords.add(row);
            } else {
                if (row.length() > longestWord.length()) {
                    longestWord = row;
                    longestWords.clear();
                    longestWords.add(row);
                } else if (row.length() == longestWord.length()) {
                    longestWords.add(row);
                } else if (row.length() < shortestWord.length()) {
                    shortestWord = row;
                    shortestWords.clear();
                    shortestWords.add(row);
                } else if (row.length() == shortestWord.length()) {
                    shortestWords.add(row);
                }
            }
            length = length + row.length();
        }

        lineInfo.setRowNumber(rowNumber);
        lineInfo.setLongestWords(longestWords);
        lineInfo.setShortestWords(shortestWords);
        lineInfo.setRowLength(line.length());
        lineInfo.setAverageWordLength(new BigDecimal((double) length / index).setScale(2, RoundingMode.UP).doubleValue());

        return lineInfo;
    }
}


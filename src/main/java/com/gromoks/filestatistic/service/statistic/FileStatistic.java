package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileStatistic {
    public static List<LineInfo> getLinesStatistic(InputStream lines, String wordDelimiter) {
        List<LineInfo> lineInfos = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(lines))) {
            String line;
            int lineNumber = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                LineInfo lineInfo = FileStatistic.getLineStatistic(line, lineNumber, wordDelimiter);
                lineInfos.add(lineInfo);
                System.out.println(lineInfo);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while trying to read the file");
        }

        return lineInfos;
    }

    public static FileInfo getAggregateLineStatistic(String fileName, List<LineInfo> lineInfos) {
        FileInfo fileInfo = new FileInfo();
        int index = 0;
        int length = 0;
        double averageSum = 0;
        List<String> longestWords = new ArrayList<>();
        List<String> shortestWords = new ArrayList<>();

        for (LineInfo lineInfo : lineInfos) {
            length += lineInfo.getRowLength();
            averageSum += lineInfo.getAverageWordLength();
            index++;

            if (index == 1) {
                longestWords = lineInfo.getLongestWords();
                shortestWords = lineInfo.getShortestWords();
            } else {
                if (lineInfo.getLongestWords().get(0).length() > longestWords.get(0).length()) {
                    longestWords = lineInfo.getLongestWords();
                } else if (lineInfo.getLongestWords().get(0).length() == longestWords.get(0).length()) {
                    longestWords.addAll(lineInfo.getLongestWords());
                }

                if (lineInfo.getShortestWords().get(0).length() < shortestWords.get(0).length()) {
                    shortestWords = lineInfo.getShortestWords();
                } else if (lineInfo.getShortestWords().get(0).length() == shortestWords.get(0).length()) {
                    shortestWords.addAll(lineInfo.getShortestWords());
                }
            }
        }
        double average = new BigDecimal(averageSum / lineInfos.size()).setScale(2, RoundingMode.UP).doubleValue();

        fileInfo.setFileName(fileName);
        fileInfo.setLongestWords(longestWords);
        fileInfo.setShortestWords(shortestWords);
        fileInfo.setLength(length);
        fileInfo.setAverageWordLength(average);

        System.out.println(fileInfo);
        return fileInfo;
    }

    static LineInfo getLineStatistic(String line, int rowNumber, String wordDelimiter) {
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


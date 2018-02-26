package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileStatistic;
import com.gromoks.filestatistic.entity.LineStatistic;
import com.gromoks.filestatistic.handler.FileLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAnalyzer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public FileStatistic getFileStatistic(String fileName, InputStream inputStream, String wordDelimiter) {
        FileStatistic fileStatistic = new FileStatistic();
        List<LineStatistic> lineStatistics = getLinesStatistic(inputStream, wordDelimiter);
        fileStatistic.setLineStatistics(lineStatistics);

        int index = 0;
        int length = 0;
        double averageSum = 0;
        List<String> longestWords = new ArrayList<>();
        List<String> shortestWords = new ArrayList<>();

        for (LineStatistic lineStatistic : lineStatistics) {
            length += lineStatistic.getRowLength();
            averageSum += lineStatistic.getAverageWordLength();
            index++;

            if (index == 1) {
                longestWords = lineStatistic.getLongestWords();
                shortestWords = lineStatistic.getShortestWords();
            } else {
                if (lineStatistic.getLongestWords().get(0).length() > longestWords.get(0).length()) {
                    longestWords = lineStatistic.getLongestWords();
                } else if (lineStatistic.getLongestWords().get(0).length() == longestWords.get(0).length()) {
                    longestWords.addAll(lineStatistic.getLongestWords());
                }

                if (lineStatistic.getShortestWords().get(0).length() < shortestWords.get(0).length()) {
                    shortestWords = lineStatistic.getShortestWords();
                } else if (lineStatistic.getShortestWords().get(0).length() == shortestWords.get(0).length()) {
                    shortestWords.addAll(lineStatistic.getShortestWords());
                }
            }
        }
        double average = new BigDecimal(averageSum / lineStatistics.size()).setScale(2, RoundingMode.UP).doubleValue();

        fileStatistic.setFileName(fileName);
        fileStatistic.setLongestWords(longestWords);
        fileStatistic.setShortestWords(shortestWords);
        fileStatistic.setLength(length);
        fileStatistic.setAverageWordLength(average);

        return fileStatistic;
    }

    List<LineStatistic> getLinesStatistic(InputStream inputStream, String wordDelimiter) {
        List<LineStatistic> lineStatistics = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int lineNumber = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                LineStatistic lineStatistic = getLineStatistic(line, lineNumber, wordDelimiter);
                lineStatistics.add(lineStatistic);
            }
            return lineStatistics;
        } catch (IOException e) {
            log.error("Error occurred while trying to read the file", e);
            throw new FileLoadException("Error occurred while trying to read the file" + e.getMessage());
        }
    }

    LineStatistic getLineStatistic(String line, int rowNumber, String wordDelimiter) {
        List<String> list = new ArrayList<>(Arrays.asList(line.split(wordDelimiter)));
        LineStatistic lineStatistic = new LineStatistic();

        List<String> longestWords = new ArrayList<>();
        List<String> shortestWords = new ArrayList<>();

        int index = 0;
        String longestWord = "";
        String shortestWord = "";
        int length = 0;
        for (String word : list) {
            index++;
            if (index == 1) {
                longestWord = word;
                shortestWord = word;
                longestWords.add(word);
                shortestWords.add(word);
            } else {
                if (word.length() > longestWord.length()) {
                    longestWord = word;
                    longestWords.clear();
                    longestWords.add(word);
                } else if (word.length() == longestWord.length()) {
                    longestWords.add(word);
                } else if (word.length() < shortestWord.length()) {
                    shortestWord = word;
                    shortestWords.clear();
                    shortestWords.add(word);
                } else if (word.length() == shortestWord.length()) {
                    shortestWords.add(word);
                }
            }
            length += word.length();
        }

        lineStatistic.setRowNumber(rowNumber);
        lineStatistic.setLongestWords(longestWords);
        lineStatistic.setShortestWords(shortestWords);
        lineStatistic.setRowLength(line.length());
        lineStatistic.setAverageWordLength(new BigDecimal((double) length / index).setScale(2, RoundingMode.UP).doubleValue());

        return lineStatistic;
    }
}


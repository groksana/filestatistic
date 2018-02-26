package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileStatistic;
import com.gromoks.filestatistic.entity.LineStatistic;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileAnalyzerTest {

    private FileAnalyzer fileAnalyzer;

    @Before
    public void setup() {
        fileAnalyzer = new FileAnalyzer();
    }

    @Test
    public void testGetLineStatistic() {
        String line = "abc        fhfjhf  fjkdsjfk  abf";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineStatistic lineStatistic = fileAnalyzer.getLineStatistic(line, rowNumber, wordDelimiter);
        List<String> longestWords = new ArrayList<>();
        longestWords.add("fjkdsjfk");
        List<String> shortestWords = new ArrayList<>();
        shortestWords.add("abc");
        shortestWords.add("abf");

        assertEquals(1, lineStatistic.getRowNumber());
        assertEquals(longestWords, lineStatistic.getLongestWords());
        assertEquals(shortestWords, lineStatistic.getShortestWords());
        assertEquals(32, lineStatistic.getRowLength());
        assertEquals(5, lineStatistic.getAverageWordLength(), 0);
    }

    @Test
    public void testGetLineStatisticForEmptyRow() {
        String line = "";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineStatistic lineStatistic = fileAnalyzer.getLineStatistic(line, rowNumber, wordDelimiter);
        List<String> longestWords = new ArrayList<>();
        longestWords.add("");
        List<String> shortestWords = new ArrayList<>();
        shortestWords.add("");

        assertEquals(1, lineStatistic.getRowNumber());
        assertEquals(longestWords, lineStatistic.getLongestWords());
        assertEquals(shortestWords, lineStatistic.getShortestWords());
        assertEquals(0, lineStatistic.getRowLength());
        assertEquals(0, lineStatistic.getAverageWordLength(), 0);
    }

    @Test
    public void testGetLinesStatistic() {
        String wordDelimiter = "\\s+";
        String firstLine = "asdfg dfghjkl qwe\n";
        String secondLine = "asdfk qw fdf";
        List<InputStream> streams = Arrays.asList(
                new ByteArrayInputStream(firstLine.getBytes()),
                new ByteArrayInputStream(secondLine.getBytes()));
        InputStream stream = new SequenceInputStream(Collections.enumeration(streams));

        List<LineStatistic> lineStatistics = fileAnalyzer.getLinesStatistic(stream, wordDelimiter);

        assertEquals(new ArrayList<>(Collections.singletonList("dfghjkl")), lineStatistics.get(0).getLongestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("asdfk")), lineStatistics.get(1).getLongestWords());

        assertEquals(new ArrayList<>(Collections.singletonList("qwe")), lineStatistics.get(0).getShortestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("qw")), lineStatistics.get(1).getShortestWords());

        assertEquals(17, lineStatistics.get(0).getRowLength());
        assertEquals(12, lineStatistics.get(1).getRowLength());

        assertEquals(5, lineStatistics.get(0).getAverageWordLength(), 0);
        assertEquals(3.34, lineStatistics.get(1).getAverageWordLength(), 0);

    }

    @Test
    public void testGetFileStatistic() {
        String fileName = "test.txt";
        String wordDelimiter = "\\s+";
        String firstLine = "asdfg dfghjkl qwe ";
        String secondLine = "asdfk qw fdf";
        List<InputStream> streams = Arrays.asList(
                new ByteArrayInputStream(firstLine.getBytes()),
                new ByteArrayInputStream(secondLine.getBytes()));
        InputStream stream = new SequenceInputStream(Collections.enumeration(streams));

        FileStatistic fileStatistic = fileAnalyzer.getFileStatistic(fileName, stream, wordDelimiter);

        assertEquals(firstLine.length() + secondLine.length(), fileStatistic.getLength());
        assertEquals(fileName, fileStatistic.getFileName());
        assertEquals(new ArrayList<>(Collections.singletonList("dfghjkl")), fileStatistic.getLongestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("qw")), fileStatistic.getShortestWords());
        assertEquals(4.17, fileStatistic.getAverageWordLength(), 0);
    }
}


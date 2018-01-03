package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileStatisticTest {

    @Test
    public void testGetLineStatistic() {
        String line = "abc        fhfjhf  fjkdsjfk  abf";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineInfo lineInfo = FileStatistic.getLineStatistic(line, rowNumber, wordDelimiter);
        List<String> longestWords = new ArrayList<>();
        longestWords.add("fjkdsjfk");
        List<String> shortestWords = new ArrayList<>();
        shortestWords.add("abc");
        shortestWords.add("abf");

        assertEquals(1, lineInfo.getRowNumber());
        assertEquals(longestWords, lineInfo.getLongestWords());
        assertEquals(shortestWords, lineInfo.getShortestWords());
        assertEquals(32, lineInfo.getRowLength());
        assertEquals(5, lineInfo.getAverageWordLength(), 0);
    }

    @Test
    public void testGetLineStatisticForEmptyRow() {
        String line = "";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineInfo lineInfo = FileStatistic.getLineStatistic(line, rowNumber, wordDelimiter);
        List<String> longestWords = new ArrayList<>();
        longestWords.add("");
        List<String> shortestWords = new ArrayList<>();
        shortestWords.add("");

        assertEquals(1, lineInfo.getRowNumber());
        assertEquals(longestWords, lineInfo.getLongestWords());
        assertEquals(shortestWords, lineInfo.getShortestWords());
        assertEquals(0, lineInfo.getRowLength());
        assertEquals(0, lineInfo.getAverageWordLength(), 0);
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

        List<LineInfo> lineInfos = FileStatistic.getLinesStatistic(stream, wordDelimiter);

        assertEquals(new ArrayList<>(Collections.singletonList("dfghjkl")), lineInfos.get(0).getLongestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("asdfk")), lineInfos.get(1).getLongestWords());

        assertEquals(new ArrayList<>(Collections.singletonList("qwe")), lineInfos.get(0).getShortestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("qw")), lineInfos.get(1).getShortestWords());

        assertEquals(17, lineInfos.get(0).getRowLength());
        assertEquals(12, lineInfos.get(1).getRowLength());

        assertEquals(5, lineInfos.get(0).getAverageWordLength(), 0);
        assertEquals(3.34, lineInfos.get(1).getAverageWordLength(), 0);

    }

    @Test
    public void testGetByLineStatistic() {
        String fileName = "test.txt";
        List<LineInfo> lineInfos = new ArrayList<>();
        LineInfo lineFirst = new LineInfo();
        lineFirst.setRowNumber(1);
        lineFirst.setLongestWords(new ArrayList<>(Arrays.asList("asdfg", "dfghj")));
        lineFirst.setShortestWords(new ArrayList<>(Collections.singletonList("qwe")));
        lineFirst.setRowLength(40);
        lineFirst.setAverageWordLength(4);

        LineInfo lineSecond = new LineInfo();
        lineSecond.setRowNumber(1);
        lineSecond.setLongestWords(new ArrayList<>(Collections.singletonList("asdfk")));
        lineSecond.setShortestWords(new ArrayList<>(Collections.singletonList("qw")));
        lineSecond.setRowLength(40);
        lineSecond.setAverageWordLength(4);

        lineInfos.add(lineFirst);
        lineInfos.add(lineSecond);

        FileInfo fileInfo = FileStatistic.getAggregateLineStatistic(fileName, lineInfos);

        assertEquals(80, fileInfo.getLength());
        assertEquals(fileName, fileInfo.getFileName());
        assertEquals(new ArrayList<>(Arrays.asList("asdfg", "dfghj", "asdfk")), fileInfo.getLongestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("qw")), fileInfo.getShortestWords());
        assertEquals(4, fileInfo.getAverageWordLength(), 0);
    }
}


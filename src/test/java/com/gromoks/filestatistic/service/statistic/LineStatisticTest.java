package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.LineInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LineStatisticTest {
    @Test
    public void testGet() {
        String line = "abc        fhfjhf  fjkdsjfk  abf";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineInfo lineInfo = LineStatistic.getLineStatistic(line, rowNumber, wordDelimiter);
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
    public void testGetForEmptyRow() {
        String line = "";
        int rowNumber = 1;
        String wordDelimiter = "\\s+";

        LineInfo lineInfo = LineStatistic.getLineStatistic(line, rowNumber, wordDelimiter);
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

}


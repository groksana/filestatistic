package com.gromoks.filestatistic.service.statistic;

import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileStatisticTest {

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

        FileInfo fileInfo = FileStatistic.getByLineStatistic(fileName, lineInfos);

        assertEquals(80, fileInfo.getLength());
        assertEquals(fileName, fileInfo.getFileName());
        assertEquals(new ArrayList<>(Arrays.asList("asdfg", "dfghj", "asdfk")), fileInfo.getLongestWords());
        assertEquals(new ArrayList<>(Collections.singletonList("qw")), fileInfo.getShortestWords());
        assertEquals(4, fileInfo.getAverageWordLength(), 0);
    }
}


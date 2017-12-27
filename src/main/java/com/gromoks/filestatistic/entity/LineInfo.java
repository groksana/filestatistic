package com.gromoks.filestatistic.entity;

import java.util.List;

public class LineInfo {
    private int rowNumber;
    private List<String> longestWords;
    private List<String> shortestWords;
    private int rowLength;
    private double averageWordLength;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<String> getLongestWords() {
        return longestWords;
    }

    public void setLongestWords(List<String> longestWords) {
        this.longestWords = longestWords;
    }

    public List<String> getShortestWords() {
        return shortestWords;
    }

    public void setShortestWords(List<String> shortestWords) {
        this.shortestWords = shortestWords;
    }

    public int getRowLength() {
        return rowLength;
    }

    public void setRowLength(int rowLength) {
        this.rowLength = rowLength;
    }

    public double getAverageWordLength() {
        return averageWordLength;
    }

    public void setAverageWordLength(double averageWordLength) {
        this.averageWordLength = averageWordLength;
    }

    @Override
    public String toString() {
        return "entity.LineInfo{" +
                "rowNumber=" + rowNumber +
                ", longestWords=" + longestWords +
                ", shortestWords=" + shortestWords +
                ", rowLength=" + rowLength +
                ", averageWordLength=" + averageWordLength +
                '}';
    }
}



package com.gromoks.filestatistic.entity;

import java.util.List;

public class FileInfo {
    private String fileName;
    private List<String> longestWords;
    private List<String> shortestWords;
    private int length;
    private double averageWordLength;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getAverageWordLength() {
        return averageWordLength;
    }

    public void setAverageWordLength(double averageWordLength) {
        this.averageWordLength = averageWordLength;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", longestWords=" + longestWords +
                ", shortestWords=" + shortestWords +
                ", length=" + length +
                ", averageWordLength=" + averageWordLength +
                '}';
    }
}



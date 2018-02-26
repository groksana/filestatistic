package com.gromoks.filestatistic.entity;

import java.util.List;

public class FileStatistic {
    private int id;
    private String fileName;
    private List<String> longestWords;
    private List<String> shortestWords;
    private int length;
    private double averageWordLength;
    private List<LineStatistic> lineStatistics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public List<LineStatistic> getLineStatistics() {
        return lineStatistics;
    }

    public void setLineStatistics(List<LineStatistic> lineStatistics) {
        this.lineStatistics = lineStatistics;
    }

    @Override
    public String toString() {
        return "FileStatistic{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", longestWords=" + longestWords +
                ", shortestWords=" + shortestWords +
                ", length=" + length +
                ", averageWordLength=" + averageWordLength +
                ", lineStatistics=" + lineStatistics +
                '}';
    }

    public void print() {
        System.out.printf("\n%-20s %20s %20s", "File", "Length",
                "LineCount");
        System.out.printf("\n%-20.20s %20d %20d", fileName, length,
                lineStatistics.size());
    }
}



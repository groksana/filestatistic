package com.gromoks.filestatistic.dao.jdbc;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.entity.FileStatistic;
import com.gromoks.filestatistic.entity.LineStatistic;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JdbcStatisticDaoITest {

    @Test
    public void testAddStatistic() throws SQLException {

        FileStatistic fileStatistic = new FileStatistic();
        fileStatistic.setFileName("test_file.txt");
        fileStatistic.setLength(20);
        fileStatistic.setLongestWords(new ArrayList<>(Arrays.asList("asdfg", "dfghj")));
        fileStatistic.setShortestWords(new ArrayList<>(Collections.singletonList("qwe")));
        fileStatistic.setAverageWordLength(5);

        List<LineStatistic> lineStatistics = new ArrayList<>();
        LineStatistic lineStatistic = new LineStatistic();
        lineStatistic.setLongestWords(new ArrayList<>(Arrays.asList("asdfg", "dfghj")));
        lineStatistic.setShortestWords(new ArrayList<>(Collections.singletonList("qwe")));
        lineStatistic.setRowLength(20);
        lineStatistic.setAverageWordLength(5);
        lineStatistics.add(lineStatistic);

        fileStatistic.setLineStatistics(lineStatistics);

        String deleteFileStatisticSQL = "DELETE FROM FILE_INFO WHERE FILE_NAME = ?";

        String deleteLineStatisticSQL = "DELETE FROM LINE_INFO WHERE FILE_ID = " +
                "(SELECT ID FROM FILE_INFO WHERE FILE_NAME = ?)";

        String selectFileStatisticSQL = "SELECT COUNT(*) from FILE_INFO WHERE FILE_NAME = ?";

        String selectLineStatisticSQL = "SELECT COUNT(*) from FILE_INFO, LINE_INFO " +
                "WHERE FILE_INFO.ID = LINE_INFO.FILE_ID AND FILE_INFO.FILE_NAME = ?";

        JdbcConnection jdbcConnection = new JdbcConnection();
        JdbcStatisticDao jdbcStatisticDao = new JdbcStatisticDao(jdbcConnection);
        jdbcStatisticDao.addStatistic(fileStatistic);

        Connection connection = jdbcConnection.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(selectFileStatisticSQL);
            preparedStatement.setString(1, fileStatistic.getFileName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = resultSet.next() ? resultSet.getInt(1) : 0;
            assertEquals(1, count);

            preparedStatement = connection.prepareStatement(selectLineStatisticSQL);
            preparedStatement.setString(1, fileStatistic.getFileName());
            resultSet = preparedStatement.executeQuery();
            count = resultSet.next() ? resultSet.getInt(1) : 0;
            assertEquals(1, count);

            preparedStatement = connection.prepareStatement(deleteLineStatisticSQL);
            preparedStatement.setString(1, fileStatistic.getFileName());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(deleteFileStatisticSQL);
            preparedStatement.setString(1, fileStatistic.getFileName());
            preparedStatement.executeUpdate();

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    }
}

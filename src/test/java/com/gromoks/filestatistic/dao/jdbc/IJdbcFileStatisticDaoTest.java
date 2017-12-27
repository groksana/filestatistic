package com.gromoks.filestatistic.dao.jdbc;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;
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

public class IJdbcFileStatisticDaoTest {

    @Test
    public void testAddStatistic() throws SQLException {

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName("test_file.txt");
        fileInfo.setLength(20);
        fileInfo.setLongestWords(new ArrayList<>(Arrays.asList("asdfg", "dfghj")));
        fileInfo.setShortestWords(new ArrayList<>(Collections.singletonList("qwe")));
        fileInfo.setAverageWordLength(5);

        List<LineInfo> lineInfos = new ArrayList<>();
        LineInfo lineInfo = new LineInfo();
        lineInfo.setLongestWords(new ArrayList<>(Arrays.asList("asdfg", "dfghj")));
        lineInfo.setShortestWords(new ArrayList<>(Collections.singletonList("qwe")));
        lineInfo.setRowLength(20);
        lineInfo.setAverageWordLength(5);
        lineInfos.add(lineInfo);

        String deleteFileStatisticSQL = "DELETE FROM FILE_INFO WHERE FILE_NAME = ?";

        String deleteLineStatisticSQL = "DELETE FROM LINE_INFO WHERE FILE_ID = " +
                "(SELECT ID FROM FILE_INFO WHERE FILE_NAME = ?)";

        String selectFileStatisticSQL = "SELECT COUNT(*) from FILE_INFO WHERE FILE_NAME = ?";

        String selectLineStatisticSQL = "SELECT COUNT(*) from FILE_INFO, LINE_INFO " +
                "WHERE FILE_INFO.ID = LINE_INFO.FILE_ID AND FILE_INFO.FILE_NAME = ?";

        JdbcFileStatisticDao jdbcFileStatisticDao = new JdbcFileStatisticDao();
        jdbcFileStatisticDao.addStatistic(fileInfo, lineInfos);

        JdbcConnection jdbcConnection = new JdbcConnection();
        Connection connection = jdbcConnection.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(selectFileStatisticSQL);
            preparedStatement.setString(1, fileInfo.getFileName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int count  = resultSet.next() ? resultSet.getInt(1) : 0;
            assertEquals(1, count);

            preparedStatement = connection.prepareStatement(selectLineStatisticSQL);
            preparedStatement.setString(1, fileInfo.getFileName());
            resultSet = preparedStatement.executeQuery();
            count  = resultSet.next() ? resultSet.getInt(1) : 0;
            assertEquals(1, count);

            preparedStatement = connection.prepareStatement(deleteLineStatisticSQL);
            preparedStatement.setString(1, fileInfo.getFileName());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(deleteFileStatisticSQL);
            preparedStatement.setString(1, fileInfo.getFileName());
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

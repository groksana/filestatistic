package com.gromoks.filestatistic.dao.jdbc;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.entity.FileInfo;
import com.gromoks.filestatistic.entity.LineInfo;

import java.sql.*;
import java.util.List;

public class JdbcStatisticDao {

    private JdbcConnection jdbcConnection;

    private static final String ADD_FILE_STATISTIC_SQL = "INSERT INTO FILE_INFO"
            + "(FILE_NAME, LONGEST_WORDS, SHORTEST_WORDS, LENGTH, AVERAGE_WORD_LENGTH) VALUES"
            + "(?,?,?,?,?)";

    private static final String ADD_LINE_STATISTIC_SQL = "INSERT INTO LINE_INFO"
            + "(FILE_ID, ROW_NUMBER, LONGEST_WORDS, SHORTEST_WORDS, LENGTH, AVERAGE_WORD_LENGTH) VALUES"
            + "(?,?,?,?,?,?)";

    public JdbcStatisticDao(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public void addStatistic(FileInfo fileInfo, List<LineInfo> lineInfos) {
        try (Connection connection = jdbcConnection.getConnection()) {

            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_FILE_STATISTIC_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, fileInfo.getFileName());
                preparedStatement.setString(2, String.join(" ", fileInfo.getLongestWords()));
                preparedStatement.setString(3, String.join(" ", fileInfo.getShortestWords()));
                preparedStatement.setInt(4, fileInfo.getLength());
                preparedStatement.setDouble(5, fileInfo.getAverageWordLength());

                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                int key = resultSet.next() ? resultSet.getInt(1) : 0;
                resultSet.close();
                preparedStatement.close();

                preparedStatement = connection.prepareStatement(ADD_LINE_STATISTIC_SQL);
                for (LineInfo lineInfo : lineInfos) {
                    preparedStatement.setInt(1, key);
                    preparedStatement.setInt(2, lineInfo.getRowNumber());
                    preparedStatement.setString(3, String.join(" ", lineInfo.getLongestWords()));
                    preparedStatement.setString(4, String.join(" ", lineInfo.getShortestWords()));
                    preparedStatement.setInt(5, lineInfo.getRowLength());
                    preparedStatement.setDouble(6, lineInfo.getAverageWordLength());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                preparedStatement.close();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Statistic can't be added for file: " + fileInfo.getFileName() + " by reason: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Issue: " + e.getMessage());
        }
    }
}


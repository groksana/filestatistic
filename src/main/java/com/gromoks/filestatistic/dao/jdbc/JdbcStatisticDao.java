package com.gromoks.filestatistic.dao.jdbc;

import com.gromoks.filestatistic.dao.config.JdbcConnection;
import com.gromoks.filestatistic.entity.FileStatistic;
import com.gromoks.filestatistic.entity.LineStatistic;
import com.gromoks.filestatistic.handler.ConnectionException;
import com.gromoks.filestatistic.handler.SQLExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class JdbcStatisticDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

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

    public void addStatistic(FileStatistic fileStatistic) {
        try (Connection connection = jdbcConnection.getConnection()) {

            connection.setAutoCommit(false);
            try {
                addFileStatistic(connection, fileStatistic);
                addLineStatistic(connection, fileStatistic.getLineStatistics(), fileStatistic);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                log.error("Statistic can't be added for file: " + fileStatistic.getFileName(), e);
                throw new SQLExecutionException("Statistic can't be added for file: " + fileStatistic.getFileName() + e.getMessage());
            }
        } catch (SQLException e) {
            log.error("Connection can't be established ", e);
            throw new ConnectionException("Connection can't be established " + e.getMessage());
        }
    }

    private void addFileStatistic(Connection connection, FileStatistic fileStatistic) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_FILE_STATISTIC_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, fileStatistic.getFileName());
            preparedStatement.setString(2, String.join(" ", fileStatistic.getLongestWords()));
            preparedStatement.setString(3, String.join(" ", fileStatistic.getShortestWords()));
            preparedStatement.setInt(4, fileStatistic.getLength());
            preparedStatement.setDouble(5, fileStatistic.getAverageWordLength());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int key = resultSet.next() ? resultSet.getInt(1) : 0;
            resultSet.close();
            fileStatistic.setId(key);
        } catch (SQLException e) {
            log.error("Statistic can't be added for file: " + fileStatistic.getFileName(), e);
            throw new SQLExecutionException("Statistic can't be added for file: " + fileStatistic.getFileName() + e.getMessage());
        }
    }

    private void addLineStatistic(Connection connection, List<LineStatistic> lineStatistics, FileStatistic fileStatistic) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_LINE_STATISTIC_SQL)) {
            for (LineStatistic lineStatistic : lineStatistics) {
                preparedStatement.setInt(1, fileStatistic.getId());
                preparedStatement.setInt(2, lineStatistic.getRowNumber());
                preparedStatement.setString(3, String.join(" ", lineStatistic.getLongestWords()));
                preparedStatement.setString(4, String.join(" ", lineStatistic.getShortestWords()));
                preparedStatement.setInt(5, lineStatistic.getRowLength());
                preparedStatement.setDouble(6, lineStatistic.getAverageWordLength());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            log.error("Statistic by lines can't be added for file: " + fileStatistic.getFileName(), e);
            throw new SQLExecutionException("Statistic by lines can't be added for file: " + fileStatistic.getFileName() + e.getMessage());
        }
    }
}


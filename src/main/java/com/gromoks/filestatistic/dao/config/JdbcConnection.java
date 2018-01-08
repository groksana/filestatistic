package com.gromoks.filestatistic.dao.config;

import com.gromoks.filestatistic.handler.ConnectionException;
import com.gromoks.filestatistic.handler.FileLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnection {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public Connection getConnection() {
        Properties properties = new Properties();
        try (InputStream inputStream = JdbcConnection.class.getClassLoader().getResourceAsStream("db/database.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("File database.properties can't be loaded: ", e);
            throw new FileLoadException("File database.properties can't be loaded: " + e.getMessage());
        }

        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error("Connection can't be get", e);
            throw new ConnectionException("Connection can't be get" + e.getMessage());
        }

        return connection;
    }
}


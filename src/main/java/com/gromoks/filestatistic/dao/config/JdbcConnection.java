package com.gromoks.filestatistic.dao.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JdbcConnection {

    private Connection connection;

    public Connection getConnection() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src\\main\\resources\\db\\database.properties");
        } catch (FileNotFoundException e) {
            System.out.println("File database.properties is not found");
        }

        Properties properties = new Properties();
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("File database.properties can't be loaded");
        }

        String driverClassName = (String) properties.get("jdbc.driverClassName");
        String url = (String) properties.get("jdbc.url");
        String username = (String) properties.get("jdbc.username");
        String password = (String) properties.get("jdbc.password");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Class can't be loaded");
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Connection can't be get");
        }

        return connection;
    }
}


package com.gromoks.filestatistic.dao.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputData {
    public InputStream get(Path path) {
        InputStream inputStream = null;

        try {
            inputStream = Files.newInputStream(path);
        } catch (IOException e) {
            System.out.println("Error occurred while trying to read the file");
        }

        return inputStream;
    }
}

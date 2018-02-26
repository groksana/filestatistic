package com.gromoks.filestatistic.dao.io;

import com.gromoks.filestatistic.handler.FileLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputData {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public InputStream get(Path path) {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("Error occurred while trying to read the file: ", e);
            throw new FileLoadException("Error occurred while trying to read the file: " + e.getMessage());
        }
    }
}

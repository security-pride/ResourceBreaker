package com.userxxx;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;

public class Config {
    private String[] target;
    private String androidJarPath;

    public String[] getTarget() {
        return target;
    }

    public String getAndroidJarPath() {
        return androidJarPath;
    }

    public static Config loadFromFile(String jsonFilePath) {
        try (Reader reader = new FileReader(jsonFilePath)) {
            return new Gson().fromJson(reader, Config.class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration: " + jsonFilePath, e);
        }
    }
}
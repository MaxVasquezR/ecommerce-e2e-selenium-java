package com.idat.qa.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {
    }

    public static Path capture(WebDriver driver, String name) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            Path target = SCREENSHOT_DIR.resolve(name + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png");
            byte[] image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            return Files.write(target, image);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar la captura de pantalla", e);
        }
    }
}

package com.example;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

class SelenideFileTest {

// Добавляет тестам нестабильность, но позволяет скачивать файлы без href
//    static {
//        Configuration.fileDownload = FileDownloadMode.PROXY;
//    }

    @Test
    void selenideFileDownloadTest() throws IOException {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();

        String contents = FileUtils.readFileToString(downloadedFile, StandardCharsets.UTF_8);
        assertThat(contents).contains("This repository is the home of the next generation of JUnit, _JUnit 5_.");

// Для понимания работы под капотом
//        try (InputStream is = new FileInputStream(downloadedFile)) {
//            byte[] fileSource = is.readAllBytes();
//            String fileContent = new String(fileSource, StandardCharsets.UTF_8);
//            assertThat(fileContent).contains("This repository is the home of the next generation of JUnit, JUnit 5.");
//        }

// Не используется т.к. устарело
//        try {
//            byte[] fileSource = is.readAllBytes();
//            String fileContent = new String(fileSource, StandardCharsets.UTF_8);
//            assertThat(fileContent).contains("This repository is the home of the next generation of JUnit, JUnit 5.");
//        } finally {
//            is.close();
//        }

// Не используется в тестах т.к. ассерты тоже выкидывают исключения и могут вызвать вечнозеленое состояние.
//        try {
//            File downloadedFile = $("#raw-url").download();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Test
    void selenideFileUploadTest() {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("pictures/Java_logo.png");
        $("div.qq-file-info").shouldHave(text("Java_logo.png"));
    }
}

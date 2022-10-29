package com.example;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.example.model.Teacher;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class FileParseTest {

    ClassLoader cl = FileParseTest.class.getClassLoader();

    @Test
    void pdfTest() throws IOException {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedFile = $("a[href*='junit-user-guide-5.9.1.pdf']").download();
        PDF pdf = new PDF(downloadedFile);
        assertThat(pdf.author).contains("Sam Brannen");
    }

    @Test
    void xlsTest() throws IOException {
        try (InputStream is = cl.getResourceAsStream("TasklistExample.xlsx")) {
            assert is != null;
            XLS xls = new XLS(is);
            assertThat(
                    xls.excel.getSheetAt(0)
                            .getRow(0)
                            .getCell(2)
                            .getStringCellValue())
                    .isEqualTo(" Список дел");
        }
    }

    @Test
    void csvTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("qa_guru.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            List<String[]> content = reader.readAll();
            String[] row = content.get(1);
            assertThat(row[0]).isEqualTo("Tuchs");
            assertThat(row[1]).isEqualTo("Junit 5");
        }
    }

    @Test
    void zipTest() throws IOException {
        try (InputStream is = cl.getResourceAsStream("sample.zip")) {
            assert is != null;
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                //
            }
        }
    }

    @Test
    void jsonTest() throws IOException {
        try (InputStream is = cl.getResourceAsStream("teacher.json")) {
            assert is != null;
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("Vasya");
            assertThat(jsonObject.get("isGoodTeacher").getAsBoolean()).isTrue();
            assertThat(jsonObject.get("passport").getAsJsonObject().get("number").getAsInt()).isEqualTo(123456);
        }
    }

    @Test
    void jsonTestWithModel() throws IOException {
        try (InputStream is = cl.getResourceAsStream("teacher.json")) {
            Gson gson = new Gson();
            assert is != null;
            Teacher teacher = gson.fromJson(new InputStreamReader(is), Teacher.class);
            assertThat(teacher.name).isEqualTo("Vasya");
            assertThat(teacher.isGoodTeacher).isTrue();
            assertThat(teacher.passport.number).isEqualTo(123456);
        }
    }
}

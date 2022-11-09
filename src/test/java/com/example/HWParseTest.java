package com.example;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.example.model.Zoo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

class HWParseTest {

    ClassLoader cl = FileParseTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        try (ZipFile zipFile = new ZipFile("src/test/resources/hw_sample.zip");
             ZipInputStream zis = new ZipInputStream(requireNonNull(cl.getResourceAsStream("hw_sample.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    if (entryName.endsWith(".pdf")) {
                        PDF pdf = new PDF(inputStream);
                        assertThat(pdf.text).contains("JUnit 5 User Guide");
                    } else if (entryName.endsWith(".xlsx")) {
                        XLS xls = new XLS(inputStream);
                        assertThat(
                                xls.excel.getSheetAt(0)
                                        .getRow(0)
                                        .getCell(2)
                                        .getStringCellValue())
                                .isEqualTo(" Список дел");
                    } else if (entryName.endsWith(".csv")) {
                        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
                            List<String[]> content = csvReader.readAll();
                            String[] row = content.get(1);
                            assertThat(row[0]).isEqualTo("Tuchs");
                            assertThat(row[1]).isEqualTo("Junit 5");
                        }
                    }
                }
            }
        }
    }

    @Test
    void jsonTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("moscow_zoo.json")) {
            Zoo zoo = mapper.readValue(inputStream, Zoo.class);
            assertThat(zoo.name).isEqualTo("Moscow Zoo");
            assertThat(zoo.area).isEqualTo(53);
            assertThat(zoo.isOpenForVisit).isTrue();
            assertThat(zoo.animals.get(0)).isEqualTo("Asiatic lion");
            assertThat(zoo.ticket.price).isEqualTo(800);
        }
    }
}

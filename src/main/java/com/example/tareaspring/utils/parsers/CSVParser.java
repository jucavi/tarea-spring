package com.example.tareaspring.utils.parsers;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CSVParser {

    /**
     *
     * @param file
     * @param klass
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> List<T> parseToBeanList(MultipartFile file, Class klass) throws IOException {

        List<T> result;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder(reader)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withType(klass)
                    .build();

            result = csvToBean.parse();
        }

        return result;
    }
}
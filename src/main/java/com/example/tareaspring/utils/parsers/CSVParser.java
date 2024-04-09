package com.example.tareaspring.utils.parsers;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.List;

/**
 * Parser for csv files.
 *
 * <pre>
 *     Usage:
 *          Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()));
 *          List&#60;Pojo&#62; users = CSVParser(reader, Pojo.class);
 * </pre>
 */
public class CSVParser {

    /**
     * Returns a List after converts CSV data to objects.
     *
     * @param reader csv reader
     * @param mapToClass class to be mapped
     * @param <T> class to convert the objects to.
     * @return list of objects
     */
    public static <T> List<T> parse(Reader reader, Class<T> mapToClass) {

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withIgnoreLeadingWhiteSpace(true)
                .withThrowExceptions(true)
                .withType(mapToClass)
                .build();

        return csvToBean.parse();
    }
}
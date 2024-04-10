package com.example.tareaspring.utils;

import com.example.tareaspring.errors.DateFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    LocalDate localDate;

    @BeforeEach
    void setUp() {
        localDate = LocalDate.of(2023, 12, 21);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fromLocalDateToStringWithPatternNullTest() {
        String expected = "2023-12-21";
        String actual = DateUtils.fromLocalDateToString(localDate, null);

        assertEquals(expected, actual);
    }

    @Test
    void fromLocalDateToStringWithStringNullMustTrowDateFormatExceptionTestTest() {
        Exception exception = assertThrows(DateFormatException.class, () -> {
            DateUtils.fromLocalDateToString(null, null);
        });

        String expectedMessage = "Invalid date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void fromLocalDateToStringWithPatternNotNullTest() {
        String pattern = "dd/MM/yy";
        String actual = DateUtils.fromLocalDateToString(localDate, pattern);
        String expected = "21/12/23";

        assertNotEquals(null, actual);
        assertNotEquals("", actual);
        assertNotEquals("2023-12-21", actual);
        assertEquals(expected, actual);
    }


}
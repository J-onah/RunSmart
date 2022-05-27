package com.example.RunSmart;

import junit.framework.TestCase;

import org.junit.Test;

public class TimeConverterTest extends TestCase {

    @Test
    public void testConvertMinutesToHourMinutes() {
        int input = 80;
        String output;
        String expected = "1 hr 20 min";
        TimeConverter timeConverter = new TimeConverter();
        output = timeConverter.convertMinutesToHourMinutes(input);

        assertEquals(expected, output);
    }
}
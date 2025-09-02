package com.payroll.uk.payroll_processing.fps.builder.util;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.GregorianCalendar;

public class XmlHelper {
    public static XMLGregorianCalendar xmlDate(LocalDate date) {
        if (date == null) return null;
        GregorianCalendar gc = GregorianCalendar.from(date.atStartOfDay(java.time.ZoneOffset.UTC));
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
                    gc.get(GregorianCalendar.YEAR),
                    gc.get(GregorianCalendar.MONTH) + 1,
                    gc.get(GregorianCalendar.DAY_OF_MONTH),
                    javax.xml.datatype.DatatypeConstants.FIELD_UNDEFINED
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid LocalDate: " + date, e);
        }
    }

    private static BigDecimal money(BigDecimal v) {

        return v == null ? null : v.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}

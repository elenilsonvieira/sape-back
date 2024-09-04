package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.presentation.exception.TimeParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DateConverterService {

    public LocalDate stringToDate(String dateString) {
        LocalDate dateTime;

        try {
            dateTime = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new TimeParseException("Não foi possível converter " + dateString);
        }

        return dateTime;
    }

    public String dateToString(LocalDate date) {
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = date.format(formatoBrasileiro);
        return dataFormatada;
    }

    public LocalTime stringToTime(String timeString) {
        LocalTime time;

        try {
            time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new TimeParseException("Não foi possível converter " + timeString);
        }

        return time;
    }

    public String timeToString(LocalTime time) {
        return time.toString();
    }

    public LocalDateTime stringToDateTime(String dateTimeString) {
        LocalDateTime dateTime;

        try {
            dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            throw new TimeParseException("Não foi possível converter " + dateTimeString);
        }

        return dateTime;
    }

    public String dateTimeToString(LocalDateTime dateTime) {
        return dateTime.toString();
    }

    public LocalDateTime dateTimeNow() {
        return LocalDateTime.now();
    }

}

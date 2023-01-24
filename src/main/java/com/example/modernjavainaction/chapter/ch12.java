package com.example.modernjavainaction.chapter;

import java.time.*;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.TemporalAdjusters.*;

/* 새로운 날짜와 시간 API*/
public class ch12 {
    class introduction{
        public void date(){
            Date date = new Date(117, 8, 21);
            //2017년 9월 21일
        }
    }

    class LocalDateTimeInstantDurationPeriod{
        public void LocalDateAndTime(){
            LocalDate date = LocalDate.of(2017, 9, 21);
            int year = date.getYear();
            Month month = date.getMonth();
            int day = date.getDayOfMonth();
            DayOfWeek dow = date.getDayOfWeek();
            int len = date.lengthOfMonth();
            boolean leap = date.isLeapYear();

            int year2 = date.get(ChronoField.YEAR);
            int month2 = date.get(ChronoField.MONTH_OF_YEAR);
            int day2 = date.get(ChronoField.DAY_OF_MONTH);


            LocalTime time = LocalTime.of(13, 45, 20);
            int hour = time.getHour();
            int minute = time.getMinute();
            int second = time.getSecond();


            LocalDate date2 = LocalDate.parse("2017-09-21");
            LocalTime time2 = LocalTime.parse("13:45:20");

            LocalDateTime dt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
            LocalDateTime dt2 = LocalDateTime.of(date, time);
            LocalDateTime dt3 = date.atTime(13, 45, 20);
            LocalDateTime dt4 = date.atTime(time);
            LocalDateTime dt5 = time.atDate(date);

            LocalDate date3 = dt1.toLocalDate();
            LocalTime time3 = dt1.toLocalTime();


            Instant.ofEpochSecond(3);
            Instant.ofEpochSecond(3, 0);
            Instant.ofEpochSecond(2, 1_000_000_000);
            Instant.ofEpochSecond(4, -1_000_000_000);
            //위 네개는 모두 같은 인스턴스 (2초 이후의 1억 나노초, 4초 이후의 1억 나노초)

            int day3 = Instant.now().get(ChronoField.DAY_OF_MONTH); // 사람이 읽을수있는 시간정보 X

            Duration d1 = Duration.between(time, time2);
            Duration d2 = Duration.between(dt1, dt2);
            Duration d3 = Duration.between(Instant.EPOCH, Instant.now());

            Period tenDays = Period.between(LocalDate.of(2017, 9, 11),
                    LocalDate.of(2017, 9, 21));

            Duration threeMinutes = Duration.ofMinutes(3);
            Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);

            Period tenDays2 = Period.ofDays(10);
            Period threeWeeks = Period.ofWeeks(3);
            Period twoy6m1d = Period.of(2, 6, 1);
        }

        public void 날짜조정파싱포매팅(){
            LocalDate date1 = LocalDate.of(2017, 9, 21);
            LocalDate date2 = date1.withYear(2011);
            LocalDate date3 = date2.withDayOfMonth(25);
            LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2);
        }
        public void 날짜조정파싱포매팅2(){
            LocalDate date1 = LocalDate.of(2018, 9, 21);
            LocalDate date2 = date1.plusWeeks(1);
            LocalDate date3 = date2.minusYears(6);
            LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);
        }
        public void TemporalAdjusters(){
            LocalDate date1 = LocalDate.of(2014, 3, 18);
            LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY));
            LocalDate date3 = date2.with(lastDayOfMonth());
        }
    }

    public class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
        public TemporalAdjuster asLambda(){
            TemporalAdjuster nextWorkingDay = ofDateAdjuster(
                    temporal -> {
                        DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
                        int dayToAdd = 1;
                        if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
                        else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
                        return temporal.plus(dayToAdd, ChronoUnit.DAYS);
                    }
            );
            return nextWorkingDay;
        }
    }

    public void 날짜시간객체출력과파씽(){
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date3 = LocalDate.of(2014, 3, 18);
        String formattedDate = date3.format(formatter);
        LocalDate date4 = LocalDate.parse(formattedDate, formatter);

        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        LocalDate date5 = LocalDate.of(2014, 3, 18);
        String formattedDate2 = date4.format(italianFormatter);
        LocalDate date6 = LocalDate.parse(formattedDate2, italianFormatter);


        DateTimeFormatter italianFormatter3 = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
    }

    public void 다양한시간대와캘린더활용방법(){
        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneId zoneId = TimeZone.getDefault().toZoneId();

        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        ZonedDateTime zdt1 = date.atStartOfDay(romeZone);

        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        ZonedDateTime zdt2 = dateTime.atZone(romeZone);

        Instant instant = Instant.now();
        ZonedDateTime zdt3 = instant.atZone(romeZone);

        LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, romeZone);
    }
    public void UTC_Greenwich(){
        ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
        // 서머 타임 제대로 처리 못함.

        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        OffsetDateTime dateTImeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);

        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        JapaneseDate japaneseDate = JapaneseDate.from(date);

        Chronology japaneseChronology = Chronology.ofLocale(Locale.JAPAN);
        ChronoLocalDate now = japaneseChronology.dateNow();

    }

    public void 이슬람력(){
        HijrahDate ramadanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1)
                .with(ChronoField.MONTH_OF_YEAR, 9);
        System.out.println("Ramadan starts on " +
                IsoChronology.INSTANCE.date(ramadanDate) +
                " and ends on " +
                IsoChronology.INSTANCE.date(ramadanDate.with(
                        TemporalAdjusters.lastDayOfMonth()
                )));
    }
}

package cn.simastudio.snippets.date;

import sun.util.resources.TimeZoneNames;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by chenqk on 2017/5/4.
 */
public class DateDemo {

    public static void main(String[] args) {
        Arrays.stream(TimeZone.getAvailableIDs()).filter((zone -> zone.toLowerCase().contains("asia"))).forEach(System.out::println);

        System.out.println();

        Clock clock = Clock.system(TimeZone.getTimeZone("Asia/Shanghai").toZoneId());
        System.out.println(clock.getZone());
        System.out.println(clock.millis());

        Clock clock1 = Clock.system(ZoneId.of("Asia/Shanghai"));
        System.out.println(clock1.millis());

        Instant instant = clock.instant();
        System.out.println(instant.getNano());
        Date date = Date.from(instant);
        System.out.println(date.getTime());

        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        System.out.println(zoneId.getRules());
        System.out.println(zoneId.getId());

        ZoneId zoneId1 = ZoneId.of("Asia/Chita");

        LocalTime localTime = LocalTime.now(zoneId);
        System.out.println(localTime.getHour());

        LocalTime localTime2 = LocalTime.now(zoneId1);

        LocalTime localTime1 = LocalTime.of(23,59, 59);
        System.out.println(localTime1.getMinute());

        System.out.println(localTime.isBefore(localTime2));
        System.out.println(localTime2.isBefore(localTime));

        System.out.println(ChronoUnit.HOURS.between(localTime, localTime2));
        System.out.println(ChronoUnit.MINUTES.between(localTime, localTime2));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.parse("2017-05-04 14:35:59"));

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println(leetTime); // 13:37

        String input = "Sep 31 2013";
        LocalDate localDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("MMM d yyyy").withLocale(Locale.ENGLISH));
        System.out.println(localDate);

        String input1 = "09122017";
        LocalDate localDate2 = LocalDate.parse(input1, DateTimeFormatter.ofPattern("MMddyyyy").withLocale(Locale.ENGLISH));
        System.out.println(localDate2);

        System.out.println();

        // locale 会影响具体是格式
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm").withLocale(Locale.ENGLISH);
        LocalDate ld1 = LocalDate.of(2014, Month.JUNE, 21);
        LocalTime lt1 = LocalTime.of(17, 30, 20);
        LocalDateTime ldt1 = LocalDateTime.of(ld1, lt1);
        System.out.println("格式：" + formatter3.format(ldt1));

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter3);
        String string = formatter3.format(parsed);
        System.out.println(string); // Nov 03, 2014 - 07:13

        LocalDate localDate1 = LocalDate.now();
        System.out.println(localDate1.getDayOfMonth());
        System.out.println(localDate1.getDayOfWeek());
        System.out.println(localDate1.getDayOfYear());

        System.out.println();

        DateTimeFormatter germanFormatter1 =DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter1);
        System.out.println(xmas); // 2014-12-24
        DateTimeFormatter germanFormatter2 =DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.CHINA);
        LocalDate xmas2 = LocalDate.parse("2014-01-12", germanFormatter2);
        System.out.println(xmas2); // 2014-12-24

        LocalDate ld = LocalDate.of(2014, Month.JUNE, 21);
        LocalTime lt = LocalTime.of(17, 30, 20);
        LocalDateTime ldt = LocalDateTime.of(ld, lt);

        DateTimeFormatter fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        System.out.println("Formatter  Default Locale: " + fmt.getLocale());
        System.out.println("Short  Date: " + fmt.format(ld));

        fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        System.out.println("Medium Date: " + fmt.format(ld));

        fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        System.out.println("Long  Date: " + fmt.format(ld));

        fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        System.out.println("Full  Date: " + fmt.format(ld));

        fmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println("Short Time:  " + fmt.format(lt));

        fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        System.out.println("Short  Datetime: " + fmt.format(ldt));

        fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        System.out.println("Medium Datetime: " + fmt.format(ldt));

        // Use German locale to format the datetime in medius style
        fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
        System.out.println(fmt.format(ldt));

        // Use Indian(English) locale to format datetime in short style
        fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(new Locale("en", "IN"));
        System.out.println(fmt.format(ldt));

        // Use Indian(English) locale to format datetime in medium style
        fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(new Locale("en", "IN"));
        System.out.println(fmt.format(ldt));
    }

}

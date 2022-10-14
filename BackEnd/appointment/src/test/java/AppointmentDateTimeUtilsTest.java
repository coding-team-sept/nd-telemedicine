import com.github.coding_team_sept.nd_backend.appointment.utils.AppointmentDateTimeUtils;
import org.joda.time.IllegalFieldValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AppointmentDateTimeUtilsTest {
    private final List<Integer> datetime = List.of(2022, 6, 15, 12, 30);
    private final AppointmentDateTimeUtils datetimeUtils = new AppointmentDateTimeUtils();

    @Test
    public void testParseValidDateTime() {
        final var parsedDatetime = datetimeUtils.parseString(
                datetime.get(0).toString() + "-"
                        + datetime.get(1).toString() + "-"
                        + datetime.get(2) + "_"
                        + datetime.get(3) + ":"
                        + datetime.get(4)
        );

        Assertions.assertEquals(datetime.get(0), parsedDatetime.getYear());
        Assertions.assertEquals(datetime.get(1), parsedDatetime.getMonthOfYear());
        Assertions.assertEquals(datetime.get(2), parsedDatetime.getDayOfMonth());
        Assertions.assertEquals(datetime.get(3), parsedDatetime.getHourOfDay());
        Assertions.assertEquals(datetime.get(4), parsedDatetime.getMinuteOfHour());
    }

    @Test
    public void testParseInvalidDateTime() {
        final String wrongMonthDatetime = "2022-13-32_25:61";
        var thrown = Assertions.assertThrows(
                IllegalFieldValueException.class,
                () -> datetimeUtils.parseString(wrongMonthDatetime)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Cannot parse \"2022-13-32_25:61\": Value 13 for monthOfYear must be in the range [1,12]"));

        final String wrongDateDatetime = "2022-12-32_25:61";
        thrown = Assertions.assertThrows(
                IllegalFieldValueException.class,
                () -> datetimeUtils.parseString(wrongDateDatetime)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Cannot parse \"2022-12-32_25:61\": Value 32 for dayOfMonth must be in the range [1,31]"));

        final String wrongHourDatetime = "2022-12-31_24:61";
        thrown = Assertions.assertThrows(
                IllegalFieldValueException.class,
                () -> datetimeUtils.parseString(wrongHourDatetime)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Cannot parse \"2022-12-31_24:61\": Value 24 for hourOfDay must be in the range [0,23]"));

        final String wrongMinuteDatetime = "2022-12-31_23:60";
        thrown = Assertions.assertThrows(
                IllegalFieldValueException.class,
                () -> datetimeUtils.parseString(wrongMinuteDatetime)
        );
        Assertions.assertTrue(thrown.getMessage().contains("Cannot parse \"2022-12-31_23:60\": Value 60 for minuteOfHour must be in the range [0,59]"));
    }

    @Test
    public void testGetMinDate() {
        final var parsedDatetime = datetimeUtils.parseString("2022-06-15_12:30");
        final var expectedMinDatetime = parsedDatetime.minusMinutes(AppointmentDateTimeUtils.intervalInMinutes);
        final var minDatetime = datetimeUtils.getMin(parsedDatetime);
        Assertions.assertEquals(expectedMinDatetime.getYear(), minDatetime.getYear());
        Assertions.assertEquals(expectedMinDatetime.getMonthOfYear(), minDatetime.getMonthOfYear());
        Assertions.assertEquals(expectedMinDatetime.getDayOfMonth(), minDatetime.getDayOfMonth());
        Assertions.assertEquals(expectedMinDatetime.getHourOfDay(), minDatetime.getHourOfDay());
        Assertions.assertEquals(expectedMinDatetime.getMinuteOfHour(), minDatetime.getMinuteOfHour());
    }

    @Test
    public void testGetMaxDate() {
        final var parsedDatetime = datetimeUtils.parseString("2022-06-15_12:30");
        final var expectedMaxDatetime = parsedDatetime.plusMinutes(AppointmentDateTimeUtils.intervalInMinutes);
        final var maxDatetime = datetimeUtils.getMax(parsedDatetime);
        Assertions.assertEquals(expectedMaxDatetime.getYear(), maxDatetime.getYear());
        Assertions.assertEquals(expectedMaxDatetime.getMonthOfYear(), maxDatetime.getMonthOfYear());
        Assertions.assertEquals(expectedMaxDatetime.getDayOfMonth(), maxDatetime.getDayOfMonth());
        Assertions.assertEquals(expectedMaxDatetime.getHourOfDay(), maxDatetime.getHourOfDay());
        Assertions.assertEquals(expectedMaxDatetime.getMinuteOfHour(), maxDatetime.getMinuteOfHour());
    }
}

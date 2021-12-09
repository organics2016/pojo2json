package ink.organics.pojo2json.fake;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FakeYearMonth extends FakeTemporal implements JsonFakeValuesService {
    @Override
    public Object random() {
        return YearMonth.from(ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.random()), ZoneId.systemDefault()))
                .toString();
    }

    @Override
    public Object def() {
        return ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.def()), ZoneId.systemDefault())
                .toString();
    }
}

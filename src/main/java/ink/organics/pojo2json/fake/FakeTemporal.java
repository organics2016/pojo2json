package ink.organics.pojo2json.fake;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FakeTemporal implements JsonFakeValuesService {

    @Override
    public Object random() {
        LocalDateTime now = LocalDateTime.now();
        long begin = now.plusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = now.minusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return begin + (long) (Math.random() * (end - begin));
    }

    @Override
    public Object def() {
        return Instant.now().toEpochMilli();
    }
}

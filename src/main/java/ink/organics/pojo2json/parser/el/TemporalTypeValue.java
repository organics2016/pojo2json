package ink.organics.pojo2json.parser.el;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TemporalTypeValue implements RandomTypeValue {
    @Override
    public Object getRandomValue() {
        LocalDateTime now = LocalDateTime.now();
        long begin = now.plusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = now.minusYears(10).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return begin + (long) (Math.random() * (end - begin));
    }

    @Override
    public Object getValue() {
        return Instant.now().toEpochMilli();
    }
}

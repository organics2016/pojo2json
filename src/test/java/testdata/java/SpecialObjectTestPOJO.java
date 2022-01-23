package testdata.java;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.UUID;

public class SpecialObjectTestPOJO {


    private Byte aByte = 0;

    private Short aShort = 0;

    private Integer integer = 0;

    private Long aLong = 0L;

    private Float aFloat = 0F;

    private Double aDouble = 0D;

    private Boolean aBoolean = false;

    private Character character = 'c';

    // -----

    private String string = "";

    private BigDecimal bigDecimal = BigDecimal.ZERO;

    //-----


    private Date date = new Date();

    private Temporal temporal = Instant.now();

    private LocalDateTime localDateTime = LocalDateTime.now();

    private LocalDate localDate = LocalDate.now();

    private LocalTime localTime = LocalTime.now();

    private ZonedDateTime zonedDateTime = ZonedDateTime.now();

    private YearMonth yearMonth = YearMonth.now();

    //-----

    private UUID uuid = UUID.randomUUID();

}

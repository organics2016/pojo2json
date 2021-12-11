package testdata;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestPOJO2 {


    private byte aByte = 0;

    private byte[] bytes = new byte[]{0, 1};

    private String str = "ccc";

    private Byte bb = 0;

    private BigDecimal bigDecimal = BigDecimal.ZERO;

    private Double ODouble;

    private Float oFloat;


    private double aDouble;

    private float aFloat;

    private LocalDate localDate = LocalDate.now();

    private LocalDateTime localDateTime = LocalDateTime.now();

    private LocalTime localTime = LocalTime.now();


    private List<String> sss = new ArrayList<>();


    private boolean aBoolean;

    private Boolean dBoolean;
}

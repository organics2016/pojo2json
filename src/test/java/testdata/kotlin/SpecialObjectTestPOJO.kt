package testdata.kotlin

import java.math.BigDecimal
import java.time.*
import java.time.temporal.Temporal
import java.util.*

class SpecialObjectTestPOJO {
    private val aByte: Byte = 0
    private val aShort: Short = 0
    private val integer = 0
    private val aLong = 0L
    private val aFloat = 0f
    private val aDouble = 0.0
    private val aBoolean = false
    private val character = 'c'

    // -----
    private val string = ""
    private val bigDecimal = BigDecimal.ZERO

    //-----
    private val date = Date()
    private val temporal: Temporal = Instant.now()
    private val localDateTime = LocalDateTime.now()
    private val localDate = LocalDate.now()
    private val localTime = LocalTime.now()
    private val zonedDateTime = ZonedDateTime.now()
    private val yearMonth = YearMonth.now()
}
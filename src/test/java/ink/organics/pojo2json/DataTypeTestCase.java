package ink.organics.pojo2json;

import com.fasterxml.jackson.databind.JsonNode;
import testdata.EnumTestPOJO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertArrayEquals;

public class DataTypeTestCase extends POJO2JsonJavaTestCase {


    public void testPrimitiveTestPOJO() {

        JsonNode result = this.testAction("PrimitiveTestPOJO.java", new POJO2JsonDefaultAction());

        assertEquals(0, result.get("aByte").intValue());
        assertEquals(0, result.get("aShort").shortValue());
        assertEquals(0, result.get("anInt").intValue());
        assertEquals(0L, result.get("aLong").longValue());
        assertEquals(0.00F, result.get("aFloat").floatValue());
        assertEquals(0.00D, result.get("aDouble").doubleValue());
        assertEquals("c", result.get("aChar").textValue());
        assertFalse(result.get("aBoolean").booleanValue());
    }

    public void testPrimitiveArrayTestPOJO() {
        JsonNode result = this.testAction("PrimitiveArrayTestPOJO.java", new POJO2JsonDefaultAction());

        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("bytes").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
        assertArrayEquals(new Short[]{0},
                StreamSupport.stream(result.get("shorts").spliterator(), false).map(JsonNode::shortValue).toArray(Short[]::new));
        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("ints").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
        assertArrayEquals(new Long[]{0L},
                StreamSupport.stream(result.get("longs").spliterator(), false).map(JsonNode::longValue).toArray(Long[]::new));
        assertArrayEquals(new Float[]{0.00F},
                StreamSupport.stream(result.get("floats").spliterator(), false).map(JsonNode::floatValue).toArray(Float[]::new));
        assertArrayEquals(new Double[]{0.00D},
                StreamSupport.stream(result.get("doubles").spliterator(), false).map(JsonNode::doubleValue).toArray(Double[]::new));
        assertArrayEquals(new String[]{"c"},
                StreamSupport.stream(result.get("chars").spliterator(), false).map(JsonNode::textValue).toArray(String[]::new));
        assertArrayEquals(new Boolean[]{false},
                StreamSupport.stream(result.get("booleans").spliterator(), false).map(JsonNode::booleanValue).toArray(Boolean[]::new));
    }

    public void testEnumTestPOJO() {
        JsonNode result = this.testAction("EnumTestPOJO.java", new POJO2JsonDefaultAction());

        assertEquals(EnumTestPOJO.Type.TYPE_A.name(), result.get("type").textValue());
    }

    public void testIterableTestPOJO() {
        JsonNode result = this.testAction("IterableTestPOJO.java", new POJO2JsonDefaultAction());

        assertTrue(result.get("iterable").isArray());
        assertTrue(result.get("collection").isArray());
        assertTrue(result.get("list").isArray());
        assertTrue(result.get("arrayList").isArray());
        assertTrue(result.get("linkedList").isArray());
        assertTrue(result.get("set").isArray());
        assertTrue(result.get("hashSet").isArray());
        assertTrue(result.get("linkedHashSet").isArray());

        assertTrue(StreamSupport.stream(result.get("iterable").spliterator(), false)
                .map(JsonNode::intValue)
                .collect(Collectors.toList())
                .contains(0));
    }

    public void testGenericTestPOJO() {
        JsonNode result = this.testAction("GenericTestPOJO.java", new POJO2JsonDefaultAction());

        assertTrue(result.get("list").isArray());
        assertTrue(result.get("listArr").get(0).isArray());
        assertTrue(result.get("listListArr").get(0).get(0).isArray());
        assertTrue(result.get("listEnum").get(0).isTextual());
        assertTrue(result.get("listList").get(0).isArray());
        assertTrue(result.get("listListList").get(0).get(0).isArray());
        assertTrue(result.get("listObject").get(0).isObject());
        assertTrue(result.get("listGenericObject").get(0).isObject());
        assertTrue(result.get("objectGeneric").isObject());
    }


    public void testSpecialObjectTestPOJO() {
        JsonNode result = this.testAction("SpecialObjectTestPOJO.java", new POJO2JsonDefaultAction());

        assertEquals(0, result.get("aByte").intValue());
        assertEquals(0, result.get("aShort").shortValue());
        assertEquals(0, result.get("integer").intValue());
        assertEquals(0L, result.get("aLong").longValue());
        assertEquals(0.00F, result.get("aFloat").floatValue());
        assertEquals(0.00D, result.get("aDouble").doubleValue());
        assertFalse(result.get("aBoolean").booleanValue());
        assertEquals("c", result.get("character").textValue());
        assertTrue(result.get("string").isTextual());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.UNNECESSARY), result.get("bigDecimal").decimalValue());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter isof = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        assertEquals(result.get("date").textValue(), LocalDateTime.parse(result.get("date").textValue(), dtf).format(dtf));
        assertEquals(result.get("temporal").longValue(), Instant.ofEpochMilli(result.get("temporal").longValue()).toEpochMilli());
        assertEquals(result.get("localDateTime").textValue(), LocalDateTime.parse(result.get("localDateTime").textValue(), dtf).format(dtf));
        assertEquals(result.get("localDate").textValue(), LocalDate.parse(result.get("localDate").textValue(), df).format(df));
        assertEquals(result.get("localTime").textValue(), LocalTime.parse(result.get("localTime").textValue(), tf).format(tf));
        assertEquals(result.get("zonedDateTime").textValue(), ZonedDateTime.parse(result.get("zonedDateTime").textValue(), isof).format(isof));

    }
}

package testdata.java;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class VariableTestPOJO {

    private List<SimpleTestPOJO<String>> listField = new ArrayList<>();

    public VariableTestPOJO(SimpleTestPOJO<Integer> cParameter) {

    }

    private void pojoMethod(SimpleTestPOJO<String> mParameter) {

        SimpleTestPOJO<Data> localVariable = new SimpleTestPOJO<>();
    }
}

class SimpleTestPOJO<T> {

    private int anInt = 0;

    private String string = "";

    private BigDecimal bigDecimal = BigDecimal.ZERO;

    private int[] ints = {0};

    private Set<Integer> linkedHashSet = new LinkedHashSet<>();

    private T data;

}

class Data {

    @com.fasterxml.jackson.annotation.JsonIgnore
    private String username;
    private String password;
}
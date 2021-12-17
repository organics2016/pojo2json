package testdata.java;

import java.util.ArrayList;
import java.util.List;

public class GenericTestPOJO {


    private List<Integer> list = new ArrayList<>();

    private List<Integer[]> listArr = new ArrayList<>();

    private List<List<Integer>[]> listListArr = new ArrayList<>();

    private List<Type> listEnum = new ArrayList<>();

    private List<List<Integer>> listList = new ArrayList<>();

    private List<List<List<Integer>>> listListList = new ArrayList<>();

    private List<Object> listObject = new ArrayList<>();

    private List<?> listGenericObject = new ArrayList<>();

    private Generic<?> objectGeneric = new Generic();

    public class Generic<T> {

    }

    public enum Type {
        TYPE_A,
        TYPE_B,
        TYPE_C;
    }
}

package testdata.java;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonIgnoreTestPOJO {

    @JsonIgnore
    private String username;
    private String password;
}

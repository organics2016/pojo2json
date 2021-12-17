package testdata.java;


import java.util.List;

public class JsonIgnorePropertiesTestPOJO {

    private String username;
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"users", "aaa", "bbb"})
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesTestPOJO> users;
    }

}

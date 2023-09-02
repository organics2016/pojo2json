package testdata.java;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class JsonIgnorePropertiesTestPOJO {

    private String username;
    @JsonIgnoreProperties({"users", "aaa", "bbb"})
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesTestPOJO> users;
    }

}

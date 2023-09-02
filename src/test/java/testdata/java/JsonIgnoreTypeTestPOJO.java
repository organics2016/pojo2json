package testdata.java;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.util.List;

public class JsonIgnoreTypeTestPOJO {


    private String username;
    private List<Role> roles;

    @JsonIgnoreType
    public class Role {
        private String roleName;
        private List<JsonIgnoreTypeTestPOJO> users;
    }
}

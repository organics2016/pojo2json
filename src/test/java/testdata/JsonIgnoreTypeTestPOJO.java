package testdata;

import java.util.List;

public class JsonIgnoreTypeTestPOJO {


    private String username;
    private List<Role> roles;

    @com.fasterxml.jackson.annotation.JsonIgnoreType
    public class Role {
        private String roleName;
        private List<JsonIgnoreTypeTestPOJO> users;
    }
}

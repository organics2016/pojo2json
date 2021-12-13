package testdata;


import java.util.List;

public class JsonIgnorePropertiesDocTestPOJO {

    private String username;
    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<JsonIgnorePropertiesDocTestPOJO> users;
    }

}

package testdata.kotlin

class JsonIgnorePropertiesDocTestPOJO {
    private val username: String? = null

    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private val roles: List<Role>? = null

    class Role {
        private val roleName: String? = null
        private val users: List<JsonIgnorePropertiesDocTestPOJO>? = null
    }
}
package testdata.kotlin

class JsonIgnorePropertiesTestPOJO {
    private val username: String? = null

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("users", "aaa", "bbb")
    private val roles: List<Role>? = null

    class Role {
        private val roleName: String? = null
        private val users: List<JsonIgnorePropertiesTestPOJO>? = null
    }
}
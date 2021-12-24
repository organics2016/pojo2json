package testdata.kotlin

class JsonIgnoreTypeTestPOJO {
    private val username: String? = null
    private val roles: List<Role>? = null

    @com.fasterxml.jackson.annotation.JsonIgnoreType
    inner class Role {
        private val roleName: String? = null
        private val users: List<JsonIgnoreTypeTestPOJO>? = null
    }
}
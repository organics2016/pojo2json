package testdata.kotlin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class JsonIgnorePropertiesTestPOJO {
    private val username: String? = null

    @JsonIgnoreProperties("users", "aaa", "bbb")
    private val roles: List<Role>? = null

    inner class Role {
        private val roleName: String? = null
        private val users: List<JsonIgnorePropertiesTestPOJO>? = null
    }
}
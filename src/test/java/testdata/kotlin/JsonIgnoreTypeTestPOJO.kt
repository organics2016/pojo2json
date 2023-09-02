package testdata.kotlin

import com.fasterxml.jackson.annotation.JsonIgnoreType

class JsonIgnoreTypeTestPOJO {
    private val username: String? = null
    private val roles: List<Role>? = null

    @JsonIgnoreType
    inner class Role {
        private val roleName: String? = null
        private val users: List<JsonIgnoreTypeTestPOJO>? = null
    }
}
package testdata.kotlin

import com.fasterxml.jackson.annotation.JsonIgnore

class JsonIgnoreTestPOJO {
    @JsonIgnore
    private val username: String? = null
    private val password: String? = null
}
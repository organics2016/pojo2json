package testdata.kotlin

class JsonIgnoreTestPOJO {
    @com.fasterxml.jackson.annotation.JsonIgnore
    private val username: String? = null
    private val password: String? = null
}
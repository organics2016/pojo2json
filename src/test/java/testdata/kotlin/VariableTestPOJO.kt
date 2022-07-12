package testdata.kotlin

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal

class VariableTestPOJO(cParameter: SimpleTestPOJO<Int?>?) {
    private val listField: List<SimpleTestPOJO<String>> = ArrayList()
    private fun pojoMethod(mParameter: SimpleTestPOJO<String>) {
        val localVariable = SimpleTestPOJO<Data>()
    }
}

class SimpleTestPOJO<T> {
    private val anInt = 0
    private val string = ""
    private val bigDecimal = BigDecimal.ZERO
    private val ints = intArrayOf(0)
    private val linkedHashSet: Set<Int> = LinkedHashSet<Int>()
    private val data: T? = null
}

class Data {
    @JsonIgnore
    private val username: String? = null
    private val password: String? = null
}
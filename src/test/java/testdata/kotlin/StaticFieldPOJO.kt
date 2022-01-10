package testdata.kotlin

import lombok.Data
import java.io.Serializable

@Data
class StaticFieldPOJO : Serializable {
    private val thisFinal = 0

    @Transient
    private val thisTransient = 0

    @Transient
    private val thisFinalTransient = 0

    companion object {
        private const val serialVersionUID = 0L
        var thisStatic = 0
        private const val thisStaticFinal = 0

        @Transient
        private val thisStaticTransient = 0

        @Transient
        private val thisStaticFinalTransient = 0
    }
}
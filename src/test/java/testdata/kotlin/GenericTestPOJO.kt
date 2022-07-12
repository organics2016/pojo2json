package testdata.kotlin

class GenericTestPOJO {
    private val list: List<Int> = ArrayList()
    private val listArr: List<Array<Int>> = ArrayList()
    private val listListArr: List<Array<List<Int>>> = ArrayList()
    private val listEnum: List<Type> = ArrayList()
    private val listList: List<List<Int>> = ArrayList()
    private val listListList: List<List<List<Int>>> = ArrayList()
    private val listObject: List<Any> = ArrayList()
    private val listGenericObject: List<*> = ArrayList<Any>()

    // --
    private val generic: Generic<Int> = Generic<Int>()
    private val genericArr: Generic<Array<Int>> = Generic<Array<Int>>()
    private val genericListArr: Generic<Array<List<Int>>> = Generic<Array<List<Int>>>()
    private val genericEnum: Generic<Type> = Generic<Type>()
    private val genericGeneric: Generic<Generic<Int>> = Generic<Generic<Int>>()
    private val genericGenericGeneric: Generic<Generic<Generic<Int>>> = Generic<Generic<Generic<Int>>>()
    private val genericObject: Generic<Any> = Generic<Any>()
    private val genericGenericObject: Generic<*> = Generic<Any>()

    inner class Generic<T> {
        private val username: String? = null
        private val data: T? = null
    }

    enum class Type {
        TYPE_A, TYPE_B, TYPE_C
    }
}
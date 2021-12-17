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
    private val objectGeneric: Generic<*> = Generic<Any?>()

    inner class Generic<T>
    enum class Type {
        TYPE_A, TYPE_B, TYPE_C
    }
}
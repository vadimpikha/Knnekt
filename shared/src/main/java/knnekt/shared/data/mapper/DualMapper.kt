package knnekt.shared.data.mapper

interface DualMapper<X, Y>: Mapper<X, Y> {

    fun convert2(obj: Y): X

}
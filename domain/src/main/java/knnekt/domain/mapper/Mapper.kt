package knnekt.domain.mapper

interface Mapper<X, Y> {

    fun convert(obj: X): Y

}
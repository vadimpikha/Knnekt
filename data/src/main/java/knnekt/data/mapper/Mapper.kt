package knnekt.data.mapper

interface Mapper<X, Y> {

    fun convert(obj: X): Y

}
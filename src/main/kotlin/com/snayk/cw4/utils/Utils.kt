package com.snayk.cw4.utils

private fun <T> List<T>.flatMapTails(f: (List<T>) -> (List<List<T>>)): List<List<T>> =
        if (isEmpty()) emptyList()
        else f(this) + this.drop(1).flatMapTails(f)

fun <T> List<T>.combinations(length: Int): List<List<T>> =
        if (length == 0) listOf(emptyList())
        else this.flatMapTails { subList -> subList.drop(1).combinations(length - 1).map { (it + subList.first()) } }

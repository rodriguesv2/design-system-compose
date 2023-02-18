package br.com.dimensa.mask.components.utils.functions

fun min(a: String, b: String): String {
    return when(kotlin.math.min(a.length, b.length)) {
        a.length -> a
        b.length -> b
        else -> ""
    }
}

fun max(a: String, b: String): String {
    return when(kotlin.math.max(a.length, b.length)) {
        a.length -> a
        b.length -> b
        else -> ""
    }
}
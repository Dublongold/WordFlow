package com.word.flow.data.repository

fun String.toSqlLike(): String {
    if (isBlank()) return ""
    val sanitized = trim().replace('*', '%')
    return if ('%' in sanitized) sanitized else "%$sanitized%"
}

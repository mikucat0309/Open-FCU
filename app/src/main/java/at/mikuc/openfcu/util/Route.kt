package at.mikuc.openfcu.util

typealias Route = String

fun Route.parents() = split('/').dropLast(1)

fun Route.current() = substringBefore('/')

fun Route.next() = substringAfter('/')

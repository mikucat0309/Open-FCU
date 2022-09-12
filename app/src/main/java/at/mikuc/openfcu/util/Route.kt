package at.mikuc.openfcu.util

typealias Route = String

fun Route.parents() = split('/').dropLast(1)

fun Route.root() = substringBefore('/')

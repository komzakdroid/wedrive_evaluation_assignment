package com.komzak.wedriveevaluationassignment.utils

fun String.maskCardNumber(): String {
    if (this.length < 4) return this
    val lastFourDigits = this.takeLast(4)
    return "**** $lastFourDigits"
}
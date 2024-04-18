package com.shoppe.api.business.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

fun LocalDateTime.toDate(): Date {
    val instant = toInstant(ZoneOffset.UTC)
    return Date.from(instant)
}

fun LocalDateTime.hasElapsed(): Boolean {
    return LocalDateTime.now().isAfter(this)
}

fun Date.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochMilli(time)
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
}

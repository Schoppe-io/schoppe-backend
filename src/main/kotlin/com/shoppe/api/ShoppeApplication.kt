package com.shoppe.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoppeApplication

fun main(args: Array<String>) {
	runApplication<ShoppeApplication>(*args)
}

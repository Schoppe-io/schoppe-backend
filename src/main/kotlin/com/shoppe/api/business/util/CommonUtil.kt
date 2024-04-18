package com.shoppe.api.business.util

import com.shoppe.api.config.Env
import com.shoppe.api.config.GeneralConfig
import de.huxhorn.sulky.ulid.ULID
import org.springframework.stereotype.Component

@Component
class CommonUtil(val generalConfig: GeneralConfig) {
    private val ulid = ULID()

    fun getNextUlid(): String {
        return ulid.nextULID().toString()
    }

    fun getEnv(): Env {
        return generalConfig.env
    }

}

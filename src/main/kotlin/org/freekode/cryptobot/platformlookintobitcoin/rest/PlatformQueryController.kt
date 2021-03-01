package org.freekode.cryptobot.platformlookintobitcoin.rest

import org.freekode.cryptobot.platformlookintobitcoin.app.PlatformQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("platform/query")
class PlatformQueryController(
    private val platformQueryService: PlatformQueryService
) {
    @GetMapping("serverTime")
    fun getServerTime(): Long {
        return platformQueryService.getServerTime()
    }
}

package org.freekode.cryptobot.platformlookintobitcoin.app

import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformQuery
import org.springframework.stereotype.Service


@Service
class PlatformQueryService(
    private val platformQuery: PlatformQuery
) {
    fun getServerTime(): Long {
        return platformQuery.getServerTime()
    }
}

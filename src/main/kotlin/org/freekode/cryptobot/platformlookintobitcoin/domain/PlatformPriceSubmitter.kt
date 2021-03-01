package org.freekode.cryptobot.platformlookintobitcoin.domain


interface PlatformPriceSubmitter {
    fun submitPrice(platformValueEvent: PlatformValueEvent)
}

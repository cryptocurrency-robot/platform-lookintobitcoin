package org.freekode.cryptobot.platformlookintobitcoin.domain

interface PlatformIndicator {
    fun getIndicatorId(): IndicatorId

    fun openStream(pair: MarketPair, callback: (PlatformResponse) -> Unit)

    fun closeStream(pair: MarketPair)
}

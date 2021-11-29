package org.freekode.cryptobot.platformlookintobitcoin.domain

import org.freekode.cryptobot.genericplatformlibrary.domain.GenericMarketPair


enum class MarketPair(private val title: String) : GenericMarketPair {
    BTC_USD("BTCUSD");

    override fun getName(): String {
        return name
    }

    override fun getTitle(): String {
        return title
    }
}

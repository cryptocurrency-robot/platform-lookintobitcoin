package org.freekode.cryptobot.platformlookintobitcoin.domain

import com.binance.api.client.domain.market.TickerStatistics
import java.io.Closeable


interface PlatformQuery {
    fun getServerTime(): Long

    fun findPriceStream(indicatorName: IndicatorName): Closeable?

    fun openPriceStream(indicatorName: IndicatorName, callback: (PlatformValueEvent) -> Unit): Closeable

    fun getDayPriceStatistic(indicatorName: IndicatorName): TickerStatistics
}


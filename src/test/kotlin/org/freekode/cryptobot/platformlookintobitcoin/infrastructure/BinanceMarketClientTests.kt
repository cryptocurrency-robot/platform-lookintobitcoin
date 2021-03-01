package org.freekode.cryptobot.platformlookintobitcoin.infrastructure

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.BinanceApiWebSocketClient
import com.binance.api.client.domain.event.AggTradeEvent
import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.testng.Assert

@SpringBootTest
@PropertySources(
    PropertySource("classpath:application.properties"),
    PropertySource("file:\${user.home}/platform-binance.properties")
)
class BinanceMarketClientTests {

    @Value("\${binance.api.key}")
    private var apiKey: String? = null

    @Value("\${binance.api.secret}")
    private var apiSecret: String? = null

    private var binanceRestClient: BinanceApiRestClient? = null

    private var binanceWebSocketClient: BinanceApiWebSocketClient? = null

    init {
        val factory = BinanceApiClientFactory.newInstance(apiKey, apiSecret)
        binanceRestClient = factory.newRestClient()
        binanceWebSocketClient = factory.newWebSocketClient()
    }

    @Test
    fun get25hPriceStatisticsTest() {
        // when
        val tickerStatistics = binanceRestClient!!.get24HrPriceStatistics(IndicatorName.BTC_USDT.title.toUpperCase())

        // then
        Assert.assertNotNull(tickerStatistics)
        Assert.assertNotNull(tickerStatistics.lastPrice)
    }

    @Test
    fun getAggregatedTradeEventTest() {
        // given
        val list = ArrayList<AggTradeEvent>();
        val binanceApiCallback: BinanceApiCallback<AggTradeEvent> = object : BinanceApiCallback<AggTradeEvent> {
            override fun onFailure(cause: Throwable) {
                throw cause
            }

            override fun onResponse(event: AggTradeEvent) {
                list.add(event)
            }
        }

        // when
        binanceWebSocketClient!!.onAggTradeEvent(IndicatorName.BTC_USDT.title.toLowerCase(), binanceApiCallback)
        Thread.sleep(5000)

        // then
        Assert.assertFalse(list.isEmpty())
    }
}

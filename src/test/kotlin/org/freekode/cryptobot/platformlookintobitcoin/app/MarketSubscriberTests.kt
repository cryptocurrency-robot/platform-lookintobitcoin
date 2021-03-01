package org.freekode.cryptobot.platformlookintobitcoin.app

import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorName
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformQuery
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@SpringBootTest
@PropertySources(
    PropertySource("classpath:application.properties")
)
class MarketSubscriberTests {
    @MockBean
    private var platformQuery: PlatformQuery? = null

    @Autowired
    private var priceSubscriberService: PriceSubscriberService? = null

    @Test
    fun subscribeForPriceTest() {
        getMockMarketQuery()

        priceSubscriberService!!.subscribeForPrice(IndicatorName.BTC_USDT)
    }

    private fun getMockMarketQuery() {
        val mock = mock(PlatformQuery::class.java)
    }
}

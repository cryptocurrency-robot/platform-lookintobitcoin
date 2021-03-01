package org.freekode.cryptobot.platformlookintobitcoin.rest

import org.freekode.cryptobot.platformlookintobitcoin.app.PriceSubscriberService
import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorName
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("platform/stream")
class PlatformSubscriberController(
    private val priceSubscriberService: PriceSubscriberService,
) {
    @PostMapping("subscribe/{pair}")
    fun subscribeForPair(@PathVariable pair: IndicatorName) {
        priceSubscriberService.subscribeForPrice(pair)
    }

    @PostMapping("unsubscribe/{pair}")
    fun unsubscribeForPair(@PathVariable pair: IndicatorName) {
        priceSubscriberService.unsubscribeForPrice(pair)
    }
}

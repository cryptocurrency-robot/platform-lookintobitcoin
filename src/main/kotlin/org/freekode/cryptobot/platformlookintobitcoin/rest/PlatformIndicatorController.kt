package org.freekode.cryptobot.platformlookintobitcoin.rest

import org.freekode.cryptobot.genericplatformlibrary.domain.IndicatorId
import org.freekode.cryptobot.platformlookintobitcoin.app.IndicatorStreamService
import org.freekode.cryptobot.platformlookintobitcoin.domain.MarketPair
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("platform/indicator")
class PlatformIndicatorController(
    private val indicatorStreamService: IndicatorStreamService
) {
    @GetMapping("available")
    fun getAvailableIndicators(): Set<IndicatorId> {
        return indicatorStreamService.getAvailableIndicators()
    }

    @PostMapping("subscribe/{indicatorId}/{pair}")
    fun subscribeToIndicator(@PathVariable indicatorId: IndicatorId, @PathVariable pair: MarketPair) {
        indicatorStreamService.subscribeToIndicator(indicatorId, pair)
    }

    @PostMapping("unsubscribe/{indicatorId}/{pair}")
    fun unsubscribeFromIndicator(@PathVariable indicatorId: IndicatorId, @PathVariable pair: MarketPair) {
        indicatorStreamService.unsubscribeFromIndicator(indicatorId, pair)
    }
}

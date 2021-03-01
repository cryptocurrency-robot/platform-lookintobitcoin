package org.freekode.cryptobot.platformlookintobitcoin.app

import org.freekode.cryptobot.priceservice.domain.MarketPair
import org.freekode.cryptobot.priceservice.domain.alert.CheckAlertWithIndicatorRequest
import org.freekode.cryptobot.priceservice.domain.alert.AlertTriggeredEvent
import org.freekode.cryptobot.priceservice.domain.alert.AlertTriggeredEventSender
import org.freekode.cryptobot.priceservice.domain.indicator.IndicatorName
import org.freekode.cryptobot.priceservice.domain.indicator.Indicators
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledIndicatorChecker(
    private val alertService: AlertService,
    private val alertTriggeredEventSender: AlertTriggeredEventSender
) {

    private val log: Logger = LoggerFactory.getLogger(ScheduledIndicatorChecker::class.java)

    @Scheduled(cron = "0 */2 * * * *")
    fun scheduledCheck() {
        Indicators.DAILY_INDICATORS.forEach {
            getIndicatorValue(it)
        }
    }

    private fun getIndicatorValue(indicatorName: IndicatorName) {
        val pair = MarketPair.BTC
        alertService.checkAndRemoveAlerts(CheckAlertWithIndicatorRequest(indicatorName, pair))
            .forEach {
                log.info("Triggered! $it")
                alertTriggeredEventSender.send(AlertTriggeredEvent(it))
            }
    }
}

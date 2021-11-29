package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.indicators

import org.freekode.cryptobot.genericplatformlibrary.domain.IndicatorId
import org.freekode.cryptobot.genericplatformlibrary.domain.PlatformIndicator
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin.ChartPointDTO
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin.LookintobitcoinClient
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule.JobScheduler
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component


@Component
class PuellIndicator(
    @Value("\${indicators.puell.update-time-seconds}") private val updateTime: Int,
    private val lookintobitcoinClient: LookintobitcoinClient,
    jobScheduler: JobScheduler,
    cacheManager: CacheManager
) : PlatformIndicator, LookintobitcoinIndicator(updateTime, jobScheduler) {

    companion object {
        private val INDICATOR_ID = IndicatorId("puell")
    }

    init {
        cache = cacheManager.getCache("puell-stream")!!
    }

    override fun getIndicatorId(): IndicatorId = INDICATOR_ID

    override fun getLatestChartPoint(): ChartPointDTO =
        lookintobitcoinClient.getPuellMultiple()
            .maxByOrNull { it.date }!!

}

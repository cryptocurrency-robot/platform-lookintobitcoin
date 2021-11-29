package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.indicators

import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorId
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformIndicator
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin.ChartPointDTO
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin.LookintobitcoinClient
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule.JobScheduler
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component


@Component
class NuplIndicator(
    @Value("\${indicators.nupl.update-time-seconds}") private val updateTime: Int,
    private val lookintobitcoinClient: LookintobitcoinClient,
    jobScheduler: JobScheduler,
    cacheManager: CacheManager
) : PlatformIndicator, LookintobitcoinIndicator(updateTime, jobScheduler) {

    companion object {
        private val INDICATOR_ID = IndicatorId("nupl")
    }

    init {
        cache = cacheManager.getCache("nupl-stream")!!
    }

    override fun getIndicatorId(): IndicatorId = INDICATOR_ID

    override fun getLatestChartPoint(): ChartPointDTO =
        lookintobitcoinClient.getNUPL()
            .maxByOrNull { it.date }!!

}

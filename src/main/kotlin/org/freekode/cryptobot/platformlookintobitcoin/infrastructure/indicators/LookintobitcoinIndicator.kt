package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.indicators

import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorId
import org.freekode.cryptobot.platformlookintobitcoin.domain.MarketPair
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformResponse
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin.ChartPointDTO
import org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule.JobScheduler
import org.quartz.TriggerKey
import org.springframework.cache.Cache
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

abstract class LookintobitcoinIndicator(
    private val updateTime: Int,
    private val jobScheduler: JobScheduler,
) {

    protected lateinit var cache: Cache

    abstract fun getIndicatorId(): IndicatorId

    abstract fun getLatestChartPoint(): ChartPointDTO

    fun openStream(pair: MarketPair, callback: (PlatformResponse) -> Unit) {
        validateStream(pair)

        val triggerKey = TriggerKey(getIndicatorId().value + "-trigger")
        val platformCallback = getCallback(pair, callback)
        jobScheduler.scheduleSimpleCallbackJob(triggerKey, updateTime, platformCallback)

        cache.put(pair, triggerKey)
    }

    fun closeStream(pair: MarketPair) {
        val triggerKey = cache.get(pair)?.get() as TriggerKey
        jobScheduler.unscheduleSimpleCallbackJob(triggerKey)
    }

    private fun getCallback(marketPair: MarketPair, callback: (PlatformResponse) -> Unit): () -> Unit {
        return {
            val latestPoint = getLatestChartPoint();

            val platformResponse =
                PlatformResponse(marketPair, getIndicatorId(), latestPoint.value, getTimestamp(latestPoint.date))
            callback.invoke(platformResponse)
        }
    }

    private fun validateStream(marketPair: MarketPair) {
        val value = cache.get(marketPair)
        if (value != null) {
            throw IllegalStateException("Such stream is already opened")
        }
    }

    private fun getTimestamp(dateTime: LocalDateTime) =
        ZonedDateTime.of(dateTime, ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() / 1000
}
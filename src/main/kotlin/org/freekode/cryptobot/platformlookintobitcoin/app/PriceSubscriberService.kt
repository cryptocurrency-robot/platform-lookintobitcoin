package org.freekode.cryptobot.platformlookintobitcoin.app

import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorName
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformPriceSubmitter
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.Closeable


@Service
class PriceSubscriberService(
    private val platformQuery: PlatformQuery,
    private val platformPriceSubmitter: PlatformPriceSubmitter
) {
    private val logger: Logger = LoggerFactory.getLogger(PriceSubscriberService::class.java)

    fun subscribeForPrice(pair: IndicatorName): Closeable {
        logger.info("Subscribing for $pair")
        return platformQuery.openPriceStream(pair) {
            platformPriceSubmitter.submitPrice(it)
        }
    }

    fun unsubscribeForPrice(pair: IndicatorName) {
        logger.info("Unsubscribing for $pair")
        val priceStream = platformQuery.findPriceStream(pair)
        priceStream?.close()
    }
}

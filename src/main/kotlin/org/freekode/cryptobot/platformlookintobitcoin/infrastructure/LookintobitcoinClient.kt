package org.freekode.cryptobot.platformlookintobitcoin.infrastructure

import org.freekode.cryptobot.platformlookintobitcoin.domain.IndicatorName
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformIndicator
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import java.math.BigDecimal
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val NUPL_NAME = "Relative Unrealised Profit/Loss"
const val PUELL_NAME = "Puell Multiple"

// TODO must be extracted to external service

@Service
class LookintobitcoinClient {
    private final val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    private final val exchangeStrategies = ExchangeStrategies.builder()
        .codecs { it.defaultCodecs().maxInMemorySize(1024 * 1000) }
        .build()

    private val webClient = WebClient
        .builder()
        .exchangeStrategies(exchangeStrategies)
        .baseUrl("https://www.lookintobitcoin.com/")
        .build()

    fun getNUPL(platformIndicator: PlatformIndicator): List<IndicatorValue> {
        return webClient
            .get()
            .uri { buildUri(it, "unrealised_profit_loss") }
            .retrieve()
            .bodyToMono(LookintobitcoinResponseDTO::class.java)
            .map { getIndicatorValues(NUPL_NAME, it, indicatorName, Indicators.NUPL) }
            .block()!!
    }

    fun getPuellMultiple(platformIndicator: PlatformIndicator): List<IndicatorValue> {
        return webClient
            .get()
            .uri { buildUri(it, "puell_multiple") }
            .retrieve()
            .bodyToMono(LookintobitcoinResponseDTO::class.java)
            .map { getIndicatorValues(PUELL_NAME, it, indicatorName, Indicators.PUELL_MULTIPLE) }
            .block()!!
    }

    private fun buildUri(uriBuilder: UriBuilder, app: String): URI {
        return uriBuilder
            .path("django_plotly_dash/app/$app/_dash-layout")
            .build()
    }

    private fun getIndicatorValues(
        lineName: String,
        responseDTO: LookintobitcoinResponseDTO,
        pair: IndicatorName,
        indicatorName: IndicatorName
    ): List<IndicatorValue> {
        val line = responseDTO.getByName(lineName)
        val iterator = line.y.iterator()
        val indicatorList = ArrayList<IndicatorValue>()
        for ((index, value) in iterator.withIndex()) {
            val date = LocalDateTime.parse(line.x[index], dateTimeFormatter)
            val indicatorValue = BigDecimal(value)
            indicatorList.add(IndicatorValue(indicatorName, pair, date, indicatorValue))
        }
        return indicatorList
    }
}

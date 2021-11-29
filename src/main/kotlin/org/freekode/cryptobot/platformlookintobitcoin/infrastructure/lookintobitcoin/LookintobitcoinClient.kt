package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import java.math.BigDecimal
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class LookintobitcoinClient {
    companion object {
        private const val NUPL_COLUMN_NAME = "Relative Unrealised Profit/Loss"
        private const val PUELL_COLUMN_NAME = "Puell Multiple"
    }

    private final val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private final val exchangeStrategies = ExchangeStrategies.builder()
        .codecs { it.defaultCodecs().maxInMemorySize(1024 * 1000) }
        .build()

    private val webClient = WebClient
        .builder()
        .exchangeStrategies(exchangeStrategies)
        .baseUrl("https://www.lookintobitcoin.com/")
        .build()

    fun getNUPL(): List<ChartPointDTO> {
        return webClient
            .post()
            .uri { buildUri(it, "unrealised_profit_loss") }
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"output\":\"chart.figure\",\"changedPropIds\":[\"url.pathname\"],\"inputs\":[{\"id\":\"url\",\"property\":\"pathname\",\"value\":\"/charts/relative-unrealized-profit--loss/\"}]}"))
            .retrieve()
            .bodyToMono(LookintobitcoinResponseDTO::class.java)
            .map { getIndicatorValues(NUPL_COLUMN_NAME, it) }
            .block()!!
    }

    fun getPuellMultiple(): List<ChartPointDTO> {
        return webClient
            .post()
            .uri { buildUri(it, "puell_multiple") }
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"output\":\"chart.figure\",\"changedPropIds\":[\"url.pathname\"],\"inputs\":[{\"id\":\"url\",\"property\":\"pathname\",\"value\":\"/charts/puell-multiple/\"}]}"))
            .retrieve()
            .bodyToMono(LookintobitcoinResponseDTO::class.java)
            .map { getIndicatorValues(PUELL_COLUMN_NAME, it) }
            .block()!!
    }

    private fun buildUri(uriBuilder: UriBuilder, app: String): URI {
        return uriBuilder
            .path("django_plotly_dash/app/$app/_dash-update-component")
            .build()
    }

    private fun getIndicatorValues(
        lineName: String,
        responseDTO: LookintobitcoinResponseDTO
    ): List<ChartPointDTO> {
        val line = responseDTO.getByName(lineName)
        val iterator = line.y.iterator()
        val values = ArrayList<ChartPointDTO>()
        for ((index, value) in iterator.withIndex()) {
            val pointDate = LocalDate.parse(line.x[index], dateTimeFormatter).atStartOfDay()
            val pointValue = BigDecimal(value)
            values.add(ChartPointDTO(pointDate, pointValue))
        }
        return values
    }
}

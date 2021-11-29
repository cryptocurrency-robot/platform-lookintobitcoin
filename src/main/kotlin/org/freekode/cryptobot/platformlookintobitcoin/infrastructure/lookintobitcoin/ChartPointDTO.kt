package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin

import java.math.BigDecimal
import java.time.LocalDateTime

data class ChartPointDTO(
    val date: LocalDateTime,
    val value: BigDecimal
)

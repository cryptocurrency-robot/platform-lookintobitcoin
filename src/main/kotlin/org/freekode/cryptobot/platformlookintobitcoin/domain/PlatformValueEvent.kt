package org.freekode.cryptobot.platformlookintobitcoin.domain

import java.io.Serializable
import java.math.BigDecimal


data class PlatformValueEvent(
    val platformName: PlatformName,
    val indicatorName: IndicatorName,
    val value: BigDecimal,
    val timestamp: Long
) : Serializable

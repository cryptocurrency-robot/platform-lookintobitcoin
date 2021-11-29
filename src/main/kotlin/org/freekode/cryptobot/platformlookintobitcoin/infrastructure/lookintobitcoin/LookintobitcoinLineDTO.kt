package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.lookintobitcoin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class LookintobitcoinLineDTO(
    val name: String,
    var x: List<String>,
    var y: List<Double>
)

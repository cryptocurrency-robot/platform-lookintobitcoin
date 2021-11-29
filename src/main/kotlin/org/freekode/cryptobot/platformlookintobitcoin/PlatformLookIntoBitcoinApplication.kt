package org.freekode.cryptobot.platformlookintobitcoin

import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformId
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources


@SpringBootApplication
@PropertySources(
    PropertySource("classpath:application.yaml"),
    PropertySource(value = ["file:\${user.home}/platform-binance.properties"], ignoreResourceNotFound = true)
)
class PlatformLookIntoBitcoinApplication(
    @Value("\${platform.name}") private val platformId: String
) {

    @Bean
    fun platformId(): PlatformId {
        return PlatformId(platformId)
    }
}

fun main(args: Array<String>) {
    runApplication<PlatformLookIntoBitcoinApplication>(*args)
}

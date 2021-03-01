package org.freekode.cryptobot.platformlookintobitcoin.infrastructure

import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformPriceSubmitter
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformValueEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class JmsPlatformValueSubmitter(
    @Value("\${event.topic.platformValue}") private val platformValueTopic: String,
    private val jmsTemplate: JmsTemplate,
) : PlatformPriceSubmitter {
    override fun submitPrice(platformValueEvent: PlatformValueEvent) {
        jmsTemplate.convertAndSend(platformValueTopic, platformValueEvent)
    }
}

package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.jms

import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformEventSender
import org.freekode.cryptobot.platformlookintobitcoin.domain.event.PlatformEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class JmsPlatformEventSender(
    @Value("\${event.topic.platform}") private val platformTopic: String,
    private val jmsTemplate: JmsTemplate,
) : PlatformEventSender {
    override fun send(event: PlatformEvent) {
        jmsTemplate.convertAndSend(platformTopic, event)
    }
}

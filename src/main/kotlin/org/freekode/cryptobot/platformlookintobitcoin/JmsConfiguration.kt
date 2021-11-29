package org.freekode.cryptobot.platformlookintobitcoin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.activemq.ActiveMQConnectionFactory
import org.freekode.cryptobot.genericplatformlibrary.domain.event.PlatformEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.ConnectionFactory

@EnableJms
@Configuration
class JmsConfiguration(
    @Value("\${event.broker-url}") private val brokerUrl: String) {

    private val log: Logger = LoggerFactory.getLogger(JmsConfiguration::class.java)

    @Bean
    fun jmsTemplate(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
    ): JmsTemplate {
        val jmsTemplate = JmsTemplate()
        jmsTemplate.connectionFactory = connectionFactory
        jmsTemplate.messageConverter = messageConverter()
        jmsTemplate.isPubSubDomain = true
        return jmsTemplate
    }

    @Bean
    fun jmsListenerContainerFactory(
        connectionFactory: ConnectionFactory,
        configurer: DefaultJmsListenerContainerFactoryConfigurer
    ): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        factory.setPubSubDomain(true)
        return factory
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        log.info("ActiveMQ Broker URL $brokerUrl")

        val factory = ActiveMQConnectionFactory()
        factory.brokerURL = brokerUrl
        return factory
    }

    @Bean
    fun messageConverter(): MessageConverter {
        val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val converter = MappingJackson2MessageConverter()
        converter.setObjectMapper(mapper)
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        converter.setTypeIdMappings(getMessageConverterTypeMappings())
        return converter
    }

    private fun getMessageConverterTypeMappings(): Map<String?, Class<*>> {
        return mapOf(
            PlatformEvent::class.simpleName to PlatformEvent::class.java
        )
    }
}

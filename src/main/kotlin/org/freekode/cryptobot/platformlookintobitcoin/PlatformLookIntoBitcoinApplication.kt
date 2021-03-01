package org.freekode.cryptobot.platformlookintobitcoin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.activemq.ActiveMQConnectionFactory
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformName
import org.freekode.cryptobot.platformlookintobitcoin.domain.PlatformValueEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.scheduling.annotation.EnableScheduling
import javax.jms.ConnectionFactory


@SpringBootApplication
@EnableJms
@EnableCaching
@EnableScheduling
@PropertySources(
    PropertySource("classpath:application.properties"),
    PropertySource(value = ["file:\${user.home}/platform-binance.properties"], ignoreResourceNotFound = true)
)
class PlatformBinanceApplication(
    @Value("\${event.broker-url}") private val brokerUrl: String,
    @Value("\${platform.name}") private val platformName: String
) {

    private val log: Logger = LoggerFactory.getLogger(PlatformBinanceApplication::class.java)

    @Bean
    fun platformName(): PlatformName {
        return PlatformName(platformName)
    }

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
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val converter = MappingJackson2MessageConverter()
        converter.setObjectMapper(mapper)
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        converter.setTypeIdMappings(getMessageConverterTypeMappings())
        return converter
    }

    private fun getMessageConverterTypeMappings(): Map<String?, Class<*>> {
        return mapOf(
            PlatformValueEvent::class.simpleName to PlatformValueEvent::class.java
        )
    }
}

fun main(args: Array<String>) {
    runApplication<PlatformBinanceApplication>(*args)
}

package org.freekode.cryptobot.platformlookintobitcoin.domain

import org.freekode.cryptobot.platformlookintobitcoin.domain.event.PlatformEvent


interface PlatformEventSender {
    fun send(event: PlatformEvent)
}

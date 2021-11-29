package org.freekode.cryptobot.platformlookintobitcoin.domain

import org.freekode.cryptobot.genericplatformlibrary.domain.PlatformIndicator
import org.freekode.cryptobot.genericplatformlibrary.domain.PlatformIndicatorRegistry
import org.springframework.stereotype.Service

@Service
class LookintobitcoinRegistry(
    indicators: List<PlatformIndicator>
) : PlatformIndicatorRegistry(indicators) {
}

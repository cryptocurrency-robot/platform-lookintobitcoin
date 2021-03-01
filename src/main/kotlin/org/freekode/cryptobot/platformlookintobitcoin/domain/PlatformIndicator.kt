package org.freekode.cryptobot.platformlookintobitcoin.domain


enum class PlatformIndicator(
    val indicatorName: IndicatorName,
    val platformSpecificName: String
) {
    NUPL(IndicatorName("NUPL"), "Relative Unrealised Profit/Loss"),
    PUELL_MULTIPLE(IndicatorName("Puell Multiple"), "Puell Multiple");

    companion object {
        fun getByIndicatorName(indicatorName: IndicatorName): PlatformIndicator {
            return values().first { it.indicatorName == indicatorName }
        }
    }
}

package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule

import org.quartz.Job
import org.quartz.JobExecutionContext

class SimpleCallbackJob : Job {
    override fun execute(context: JobExecutionContext?) {
        (context!!.mergedJobDataMap["callback"] as () -> Unit) .invoke()
    }
}

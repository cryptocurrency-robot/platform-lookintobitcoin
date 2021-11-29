package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule

import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service

@Service
class JobScheduler(
    schedulerFactoryBean: SchedulerFactoryBean
) {

    private val scheduler: Scheduler

    init {
        scheduler = schedulerFactoryBean.scheduler
    }

    fun scheduleSimpleCallbackJob(triggerKey: TriggerKey, repeatEverySeconds: Int, callback: () -> Unit) {
        val jobDataMap = JobDataMap()
        jobDataMap["callback"] = callback

        val jobDetail = JobBuilder.newJob(SimpleCallbackJob::class.java)
            .withIdentity(triggerKey.name + "-simple-callback-job")
            .setJobData(jobDataMap)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(triggerKey)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(repeatEverySeconds))
            .build()
        scheduler.scheduleJob(jobDetail, trigger)
    }

    fun unscheduleSimpleCallbackJob(triggerKey: TriggerKey) {
        scheduler.unscheduleJob(triggerKey)
    }
}

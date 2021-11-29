package org.freekode.cryptobot.platformlookintobitcoin.infrastructure.schedule

import org.freekode.cryptobot.genericplatformlibrary.infrastructure.schedule.SimpleJobScheduler
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service

@Service
class JobScheduler(
    schedulerFactoryBean: SchedulerFactoryBean
) : SimpleJobScheduler(schedulerFactoryBean)

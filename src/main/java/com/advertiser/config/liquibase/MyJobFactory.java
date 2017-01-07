package com.advertiser.config.liquibase;

import com.advertiser.service.CampaignService;
import org.quartz.*;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Created by ws on 1/7/17.
 */
public class MyJobFactory extends SimpleJobFactory {
    private CampaignService cs;

    public MyJobFactory(CampaignService cs){
        this.cs = cs;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler Scheduler) throws SchedulerException {
        UpdateJob job = (UpdateJob) super.newJob(bundle, Scheduler);
        job.setCs(cs);
        return job;
    }
}


package com.advertiser.service;

import com.advertiser.config.liquibase.MyJobFactory;
import com.advertiser.config.liquibase.UpdateJob;
import com.advertiser.domain.Campaign;
import com.advertiser.domain.enumeration.CampaignState;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.web.rest.CampaignResource;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by ws on 1/7/17.
 */
@Service
@Transactional
public class CampaignService {
    @Inject private CampaignResource campaignResource;
    @Inject private CampaignMapper campaignMapper;

    public void updateCampaigns() throws URISyntaxException {
        List<Campaign> campaigns = campaignResource.getAllNotCompletedCampaigns();
        ZonedDateTime now = ZonedDateTime.now();
        for(Campaign campaign : campaigns){
            if(campaign.getStartDate().isBefore(now) && campaign.getCampaignState() != CampaignState.STARTED){
                campaign.setCampaignState(CampaignState.STARTED);
                campaignResource.updateCampaign(campaignMapper.campaignToCampaignDTO(campaign));
            }
            if(campaign.getEndDate().isBefore(now) && campaign.getCampaignState() != CampaignState.COMPLETED){
                campaign.setCampaignState(CampaignState.COMPLETED);
                campaignResource.updateCampaign(campaignMapper.campaignToCampaignDTO(campaign));
            }
        }
    }

    @PostConstruct
    public void go() throws SchedulerException {
        Scheduler scheduler = null;
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.setJobFactory(new MyJobFactory(this));

        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("update")
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever()
            )
            .build();

        //scheduler.getContext().put("context", (Object)this);

        JobDetail newJob = JobBuilder
            .newJob(UpdateJob.class)
            .withIdentity("update")
            .build();


        scheduler.scheduleJob(newJob, trigger);
        scheduler.start();
    }
}

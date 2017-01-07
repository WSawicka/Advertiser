package com.advertiser.config.liquibase;

import com.advertiser.service.CampaignService;
import lombok.Setter;
import org.quartz.*;

import javax.inject.Inject;
import java.net.URISyntaxException;

/**
 * Created by ws on 1/7/17.
 */
@Setter
public class UpdateJob implements Job{

    public CampaignService cs = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            cs.updateCampaigns();
            System.out.println("Database is succesfully updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


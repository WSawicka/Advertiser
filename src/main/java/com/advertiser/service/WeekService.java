package com.advertiser.service;

import com.advertiser.domain.Week;
import com.advertiser.repository.WeekRepository;
import io.swagger.models.auth.In;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by ws on 10/31/16.
 */
@Service
@Transactional(readOnly = true)
public class WeekService {

    @Inject
    private WeekRepository weekRepository;

    public Week findOne(Integer year, Integer weekNumber) {
      return null;
    }
}

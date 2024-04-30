package com.muzimin;

import com.muzimin.configuration.Config;
import com.muzimin.configuration.ConfigurationParse;
import com.muzimin.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 李煌民
 * @date: 2024-04-29 15:04
 **/
public class Application {
    public static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        log.info("start muzimin - parsing configuration");
        Config config = ConfigurationParse.parse(args);
        Job job = new Job(config);

    }
}

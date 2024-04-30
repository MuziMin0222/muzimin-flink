package com.muzimin;

import com.muzimin.configuration.Configuration;
import com.muzimin.configuration.ConfigurationParse;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 李煌民
 * @date: 2024-04-29 15:04
 **/
public class Application {
    public static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        log.info("start muzimin - parsing configuration");

        Configuration configuration = ConfigurationParse.parse(args);

        System.out.println(configuration);
    }
}

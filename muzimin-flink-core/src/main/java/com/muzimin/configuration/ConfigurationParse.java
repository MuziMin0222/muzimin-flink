package com.muzimin.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.apache.flink.api.java.utils.ParameterTool;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: 李煌民
 * @date: 2024-04-29 16:54
 **/
@Slf4j
public class ConfigurationParse {
    //将外部传入的-xxx参数，转为Map集合
    private static Map<String, String> processArgs(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        Properties properties = parameterTool.getProperties();
        for (String key : properties.stringPropertyNames()) {
            map.put(key, properties.getProperty(key));
        }

        log.info("MuZiMin Flink Log | 解析Flink任务传入的参数{}", map);
        return map;
    }

    public static Configuration parse(String[] args) {
        Map<String, String> argsMap = processArgs(args);
        String configFilePath = argsMap.get("config");
        String resultStr = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get(configFilePath)));
            resultStr = StringSubstitutor.replace(content, argsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Yaml().loadAs(resultStr, Configuration.class);
    }
}

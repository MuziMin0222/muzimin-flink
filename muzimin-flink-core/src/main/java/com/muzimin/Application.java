package com.muzimin;

import com.muzimin.configuration.Configuration;
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

        String content = new String(Files.readAllBytes(Paths.get("/Users/muzimin/code/IdeaProjects/muzimin-flink/muzimin-flink-core/example/config.yaml")));
        System.out.println(content);

        HashMap<String, Object> map = new HashMap<>();
        map.put("aaa","我的好大儿");
        map.put("vvvv","额滴神啊");

        String replace = StringSubstitutor.replace(content, map);
        System.out.println(replace);

        System.out.println(new Yaml().loadAs(replace, Configuration.class));

        FileReader fileReader = new FileReader("/Users/muzimin/code/IdeaProjects/muzimin-flink/muzimin-flink-core/example/config.yaml");
        Yaml yaml = new Yaml();
        Configuration obj = yaml.loadAs(fileReader, Configuration.class);

        System.out.println(obj);
    }
}

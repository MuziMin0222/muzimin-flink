package com.muzimin.configuration;

import com.muzimin.configuration.step.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 李煌民
 * @date: 2024-04-29 15:21
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private List<Step> steps;
    private Map<String, Object> env;
}

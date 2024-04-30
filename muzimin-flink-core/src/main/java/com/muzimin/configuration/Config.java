package com.muzimin.configuration;

import com.muzimin.configuration.env.Env;
import com.muzimin.configuration.step.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author: 李煌民
 * @date: 2024-04-29 15:21
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private List<Step> steps;
    private Env env;
}

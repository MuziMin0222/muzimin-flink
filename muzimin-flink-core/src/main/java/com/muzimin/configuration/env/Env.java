package com.muzimin.configuration.env;

import lombok.Data;

import java.util.Map;

/**
 * @author: 李煌民
 * @date: 2024-04-30 14:46
 **/
@Data
public class Env {
    private String jobMode;
    private Map<String,Object> params;
}

package com.muzimin.configuration.step;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 李煌民
 * @date: 2024-04-29 16:06
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Step {
    private String dataTableName;
    private String sql;
    private String file;
    private String classpath;
    private Map<String, Object> params;
    private boolean ignoreOnFailures;
}

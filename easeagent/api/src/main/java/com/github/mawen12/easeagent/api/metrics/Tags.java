package com.github.mawen12.easeagent.api.metrics;

import com.github.mawen12.easeagent.api.annotation.SharedToBootstrap;
import lombok.Data;

@Data
@SharedToBootstrap
public class Tags {
    private final String category;
    private final String type;
    private final String keyFieldName;
}
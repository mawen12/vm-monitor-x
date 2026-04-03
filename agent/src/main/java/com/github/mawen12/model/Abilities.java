package com.github.mawen12.model;

import com.github.mawen12.utils.Json;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Abilities implements Jsonable {

    public List<ObjectName> DataSources = new ArrayList<>();

    public ObjectName Tomcat;

    public String tomcatName;

    @Override
    public String toJson() {
        return Json.toJson(
                "DataSourceCount", DataSources.size(),
                "DataSources", DataSources.isEmpty() ? null : DataSources.stream().map(objName -> objName.getKeyProperty("id")).collect(Collectors.toList()),
                "Tomcat", Tomcat != null ? tomcatName.substring(1, tomcatName.length()-1) : null);
    }
}

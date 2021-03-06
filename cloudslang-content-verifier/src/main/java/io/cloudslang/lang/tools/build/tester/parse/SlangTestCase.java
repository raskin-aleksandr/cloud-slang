/*
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.lang.tools.build.tester.parse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by stoneo on 3/15/2015.
 **/
public class SlangTestCase {

    private String name;

    private String testFlowPath;

    private String description;

    private List<String> testSuites;

    private String systemPropertiesFile;

    private List<Map> inputs;

    private Boolean throwsException;

    private String result;

    public static final String BASE_TEST_SUITE = "base";

    //for jackson
    private SlangTestCase() {}

    public SlangTestCase(String name, String testFlowPath, String description, List<String> testSuites,
                         String systemPropertiesFile, List<Map> inputs,
                         Boolean throwsException, String result){
        this.name = name;
        this.testFlowPath = testFlowPath;
        this.description = description;
        this.systemPropertiesFile = systemPropertiesFile;
        this.testSuites = testSuites;
        this.inputs = inputs;
        this.throwsException = throwsException;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getTestFlowPath() {
        return testFlowPath;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTestSuites() {
        if(CollectionUtils.isEmpty(testSuites)) {
            this.testSuites = Arrays.asList(BASE_TEST_SUITE);
        }
        return testSuites;
    }

    public String getSystemPropertiesFile() {
        return systemPropertiesFile;
    }

    public List<Map> getInputs() {
        return inputs;
    }

    public Boolean getThrowsException() {
        return throwsException;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

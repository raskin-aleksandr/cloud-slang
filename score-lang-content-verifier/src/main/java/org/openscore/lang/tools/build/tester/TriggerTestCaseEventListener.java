package org.openscore.lang.tools.build.tester;
/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/

import org.apache.log4j.Logger;
import org.openscore.events.EventConstants;
import org.openscore.events.ScoreEvent;
import org.openscore.events.ScoreEventListener;
import org.openscore.lang.entities.ScoreLangConstants;
import org.openscore.lang.runtime.events.LanguageEventData;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.openscore.lang.entities.ScoreLangConstants.EVENT_EXECUTION_FINISHED;
import static org.openscore.lang.runtime.events.LanguageEventData.EXCEPTION;
import static org.openscore.lang.runtime.events.LanguageEventData.RESULT;

/**
 * User: stoneo
 * Date: 18/03/2015
 * Time: 14:13
 */
public class TriggerTestCaseEventListener implements ScoreEventListener {

    public static final String TEST_CASE_PASSED = "Passed test case: ";
    public static final String TEST_CASE_FAILED = "Failed running test case: ";

    private final static Logger log = Logger.getLogger(SlangTestRunner.class);

    private AtomicBoolean flowFinished = new AtomicBoolean(false);
    private AtomicReference<String> errorMessage = new AtomicReference<>("");
    private String testCaseName;

    public TriggerTestCaseEventListener(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public boolean isFlowFinished() {
        return flowFinished.get();
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    @Override
    public synchronized void onEvent(ScoreEvent scoreEvent) throws InterruptedException {
        @SuppressWarnings("unchecked") Map<String,Serializable> data = (Map<String,Serializable>)scoreEvent.getData();
        switch (scoreEvent.getEventType()){
            case EventConstants.SCORE_FINISHED_EVENT :
                flowFinished.set(true);
                break;
            case EventConstants.SCORE_ERROR_EVENT :
                errorMessage.set(TEST_CASE_FAILED + testCaseName + ". " + data.get(EventConstants.SCORE_ERROR_LOG_MSG) + " , " +
                        data.get(EventConstants.SCORE_ERROR_MSG));
                break;
            case ScoreLangConstants.SLANG_EXECUTION_EXCEPTION:
                errorMessage.set(TEST_CASE_FAILED + testCaseName + ". " + data.get(EXCEPTION));
                break;
            case EVENT_EXECUTION_FINISHED :
                flowFinished.set(true);
                printFinishEvent(data);
                break;
        }
    }

    private void printFinishEvent(Map<String, Serializable> data) {
        String flowResult = (String)data.get(RESULT);
        String flowName = (String)data.get(LanguageEventData.levelName.EXECUTABLE_NAME.toString());
        log.info(TEST_CASE_PASSED + testCaseName + ". Finished running: " + flowName + " with result: " + flowResult);
    }
}
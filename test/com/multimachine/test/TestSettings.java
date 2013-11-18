/* 
 * Copyright 2013 Muthukumaran (https://github.com/muthuishere/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.multimachine.test;

import com.multimachine.beans.Settings;
import com.multimachine.controller.SettingsController;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class TestSettings {

    private final SettingsController settingsController = new SettingsController();

    @Before
    public void setUp() {
        // helper.setUp();
    }

    @After
    public void tearDown() {
        //helper.tearDown();
    }

    @Test
    public void testWrite() {
        Settings settings = new Settings();
        settings.add(TestData.getTestConnectionInfo());
        settings.add(TestData.getTestConnectionInfo());
        settings.add(TestData.getTestConnectionInfo());
        settings.add(TestData.getTestConnectionInfo());
        settings.add(TestData.getTestConnectionInfo());
        boolean flgSuccess = settingsController.saveSettingsasXml(settings);
        assertTrue("Settings saved Successfully", flgSuccess);
    }

    @Test
    public void testRead() {

        Settings settings = settingsController.retrieveSettingsFromFile();
        assertNotNull("Host is not Null", settings.getConnectionInfo().get(0).getServerInfo().getHost());

    }

    public static void main(String[] args) {
        //testWrite();
        //testRead();
    }
}

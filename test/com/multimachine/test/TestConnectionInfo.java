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

import com.sshutils.beans.ConnectionInfo;
import com.sshutils.beans.Settings;
import com.sshutils.controller.SettingsController;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class TestConnectionInfo {

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
    public void testConnectionProfileName() {
        
        ConnectionInfo connectionInfo=TestData.getTestConnectionInfo();
        connectionInfo.setProfileName("test/testconnectionProfile");
         assertEquals("only Profile with Folder","testconnectionProfile", connectionInfo.getOnlyProfileName());
         assertEquals("Get Folder","test/", connectionInfo.getFolderName());
         
         
         connectionInfo.setProfileName("emptyconnectionProfile");
         assertEquals("Profilename without folder","emptyconnectionProfile", connectionInfo.getOnlyProfileName());
         assertEquals("Profilename with empty folder","", connectionInfo.getFolderName());
         
    }
     
    
    public static void main(String[] args) {
        //testWrite();
        //testRead();
    }
}

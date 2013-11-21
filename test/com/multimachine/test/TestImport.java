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

import com.multimachine.beans.ConnectionInfo;
import com.multimachine.utils.CryptHelper;
import com.multimachine.utils.ImportHelper;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class TestImport {

    
    private static final Logger log = Logger.getLogger(TestImport.class);
    
    @Before
    public void setUp() {
        // helper.setUp();
    }

    @After
    public void tearDown() {
        //helper.tearDown();
    }

    @Test
    public void testImportSettings() {
        String filename = "C:\\muthu\\official2013\\work\\winscp416.ini";
        
        ArrayList<ConnectionInfo> lstConnections;
        try {
            lstConnections = ImportHelper.importWinscp(filename);
            log.info(lstConnections.toString());
        } catch (Exception ex) {
          log.error(ex);
          fail("Exception thrown while importing");
        }
        
         
       
        

      

    }
}

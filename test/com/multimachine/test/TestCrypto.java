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

import com.multimachine.utils.CryptHelper;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class TestCrypto {

    @Before
    public void setUp() {
        // helper.setUp();
    }

    @After
    public void tearDown() {
        //helper.tearDown();
    }

    @Test
    public void testEncryptDecrypt() {
        String cipherText = "Encrypt String";

        String encryptStr = CryptHelper.encrypt(cipherText);
        assertNotNull("Decrypted String not empty", encryptStr);

        String decryptStr = CryptHelper.decrypt(encryptStr);
        assertEquals("Encryption - Decryption Process Success", decryptStr, cipherText);

    }
}

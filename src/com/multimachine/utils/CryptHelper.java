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
package com.multimachine.utils;

import java.security.Provider;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class CryptHelper {

    private static final Logger log = Logger.getLogger(CryptHelper.class);

    static final String  ENCRYPT_TYPE="AES/ECB/PKCS7Padding";
      //static final String  ENCRYPT_TYPE="AES/CBC/PKCS5Padding";
    private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };//"thisIsASecretKey";

    
    static{
     Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());    
    }
    
    public static String encrypt(String strToEncrypt) {
        try {
            

            
            
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE,"BC");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (Exception e) {
            log.error("Error while encrypting", e);
        }
        return null;

    }

    public static String decrypt(String strToDecrypt) {
        try {
            
//                for (Provider provider: Security.getProviders()) {
//  log.info(provider.getName());
//  for (String key: provider.stringPropertyNames())
//   log.info("\t" + key + "\t" + provider.getProperty(key));
//}
                
                
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE,"BC");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        } catch (Exception e) {
            log.error("Error while decrypting", e);

        }
        return null;
    }

}

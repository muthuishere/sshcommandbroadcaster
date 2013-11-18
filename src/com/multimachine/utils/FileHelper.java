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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class FileHelper {

    private static final Logger log = Logger.getLogger(FileHelper.class);

    public static String readFileAsString(String filePath) throws java.io.IOException {

        File file = new File(filePath);
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream f = null;
        String contents = null;
        try {
            log.info("Attempting to read file" + file.getAbsolutePath() + "//" + file.getName());
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
            contents = new String(buffer);

        } catch (IOException e) {
            log.error(e);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ignored) {
                }
            }
        }

        // log.info("contents "+ contents);
        return contents;
    }

    public static void writetoFile(String filepath, String source) throws java.io.IOException {

        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
        FileWriter fileWriter = null;
        try {
            log.info("Attempting to write file" + file.getAbsolutePath() + "//" + file.getName());
            fileWriter = new FileWriter(file, false);
            fileWriter.write(source);
            fileWriter.close();

        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}

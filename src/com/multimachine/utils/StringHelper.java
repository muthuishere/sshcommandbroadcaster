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

import java.lang.reflect.Field;

/**
 *
 * @author hutchuk
 */
public class StringHelper {

    public static String NEW_LINE = System.getProperty("line.separator");

    public static String removeUnicodeAndEscapeChars(String input) {
        StringBuilder buffer = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            if ((int) input.charAt(i) > 256) {
                buffer.append("\\u").append(Integer.toHexString((int) input.charAt(i)));
            } else {
                if (input.charAt(i) == '\n') {
                    buffer.append("\\n");
                } else if (input.charAt(i) == '\t') {
                    buffer.append("\\t");
                } else if (input.charAt(i) == '\r') {
                    buffer.append("\\r");
                } else if (input.charAt(i) == '\b') {
                    buffer.append("\\b");
                } else if (input.charAt(i) == '\f') {
                    buffer.append("\\f");
                } else if (input.charAt(i) == '\'') {
                    buffer.append("\\'");
                } else if (input.charAt(i) == '\"') {
                    buffer.append("\\");
                } else if (input.charAt(i) == '\\') {
                    buffer.append("\\\\");
                } else {
                    buffer.append(input.charAt(i));
                }
            }
        }
        return buffer.toString();
    }

    public static String defaultString(String str) {

        if (null != str) {
            return str;
        }

        return "";
    }

    public static String toString(Object obj) {

        if (null != obj) {
            return ToStringUtil.getText(obj);
        }

        return "";
    }

    public static boolean isEmpty(String s) {
        return (null == s || s.trim().length() <= 0);

    }
}

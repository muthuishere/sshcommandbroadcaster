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

import java.lang.reflect.Array;

import java.util.logging.*;
import java.util.regex.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

public final class ToStringUtil {

    
    public static String getText(Object aObject) {
        return getTextAvoidCyclicRefs(aObject, null, null);
    }

    static String getTextAvoidCyclicRefs(Object aObject, Class aSpecialClass, String aMethodName) {
        StringBuilder result = new StringBuilder();
        addStartLine(aObject, result);

        Method[] methods = aObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (isContributingMethod(method, aObject.getClass())) {
                addLineForGetXXXMethod(aObject, method, result, aSpecialClass, aMethodName);
            }
        }

        addEndLine(result);
        return result.toString();
    }

  
    private static final String fGET_CLASS = "getClass";
    private static final String fCLONE = "clone";
    private static final String fHASH_CODE = "hashCode";
    private static final String fTO_STRING = "toString";

    private static final String fGET = "get";
    private static final Object[] fNO_ARGS = new Object[0];
    private static final Class[] fNO_PARAMS = new Class[0];
    /*
     Previous versions of this class indented the data within a block. 
     That style breaks when one object references another. The indentation
     has been removed, but this variable has been retained, since others might 
     prefer the indentation anyway.
     */
    private static final String fINDENT = "";
    private static final String fAVOID_CIRCULAR_REFERENCES = "[circular reference]";
    private static final org.apache.log4j.Logger fLogger = org.apache.log4j.Logger.getLogger(ToStringUtil.class);
    private static final String NEW_LINE = System.getProperty("line.separator");

    private static Pattern PASSWORD_PATTERN = Pattern.compile("password", Pattern.CASE_INSENSITIVE);
    private static String HIDDEN_PASSWORD_VALUE = "****";

    //prevent construction by the caller
    private ToStringUtil() {
        //empty
    }

    static boolean textHasContent(String aText) {
        return (aText != null) && (aText.trim().length() > 0);
    }

    static String quote(Object aObject) {
        return SINGLE_QUOTE + String.valueOf(aObject) + SINGLE_QUOTE;
    }

    static String getArrayAsString(Object aArray) {
        final String fSTART_CHAR = "[";
        final String fEND_CHAR = "]";
        final String fSEPARATOR = ", ";
        final String fNULL = "null";

        if (aArray == null) {
            return fNULL;
        }
        checkObjectIsArray(aArray);

        StringBuilder result = new StringBuilder(fSTART_CHAR);
        int length = Array.getLength(aArray);
        for (int idx = 0; idx < length; ++idx) {
            Object item = Array.get(aArray, idx);
            if (isNonNullArray(item)) {
                //recursive call!
                result.append(getArrayAsString(item));
            } else {
                result.append(item);
            }
            if (!isLastItem(idx, length)) {
                result.append(fSEPARATOR);
            }
        }
        result.append(fEND_CHAR);
        return result.toString();
    }

    static Logger getLogger(Class<?> aClass) {
        return Logger.getLogger(aClass.getPackage().getName());
    }

    // PRIVATE
    private static final String SINGLE_QUOTE = "'";

    private static boolean isNonNullArray(Object aItem) {
        return aItem != null && aItem.getClass().isArray();
    }

    private static void checkObjectIsArray(Object aArray) {
        if (!aArray.getClass().isArray()) {
            throw new IllegalArgumentException("Object is not an array.");
        }
    }

    private static boolean isLastItem(int aIdx, int aLength) {
        return (aIdx == aLength - 1);
    }

    private static void addStartLine(Object aObject, StringBuilder aResult) {
        aResult.append(aObject.getClass().getName());
        aResult.append(" {");
        aResult.append(NEW_LINE);
    }

    private static void addEndLine(StringBuilder aResult) {
        aResult.append("}");
        aResult.append(NEW_LINE);
    }

    /**
     * Return <tt>true</tt> only if <tt>aMethod</tt> is public, takes no args,
     * returns a value whose class is not the native class, is not a method of
     * <tt>Object</tt>.
     */
    private static boolean isContributingMethod(Method aMethod, Class aNativeClass) {
        boolean isPublic = Modifier.isPublic(aMethod.getModifiers());
        boolean hasNoArguments = aMethod.getParameterTypes().length == 0;
        boolean hasReturnValue = aMethod.getReturnType() != Void.TYPE;
        boolean returnsNativeObject = aMethod.getReturnType() == aNativeClass;
        boolean isMethodOfObjectClass
                = aMethod.getName().equals(fCLONE)
                || aMethod.getName().equals(fGET_CLASS)
                || aMethod.getName().equals(fHASH_CODE)
                || aMethod.getName().equals(fTO_STRING);
        return isPublic
                && hasNoArguments
                && hasReturnValue
                && !isMethodOfObjectClass
                && !returnsNativeObject;
    }

    private static void addLineForGetXXXMethod(Object aObject,
            Method aMethod,
            StringBuilder aResult,
            Class aCircularRefClass,
            String aCircularRefMethodName) {
        aResult.append(fINDENT);
        aResult.append(getMethodNameMinusGet(aMethod));
        aResult.append(": ");
        Object returnValue = getMethodReturnValue(aObject, aMethod);
        if (returnValue != null && returnValue.getClass().isArray()) {
            aResult.append(getArrayAsString(returnValue));
        } else {
            if (aCircularRefClass == null) {
                aResult.append(returnValue);
            } else {
                if (aCircularRefClass == returnValue.getClass()) {
                    Method method = getMethodFromName(aCircularRefClass, aCircularRefMethodName);
                    if (isContributingMethod(method, aCircularRefClass)) {
                        returnValue = getMethodReturnValue(returnValue, method);
                        aResult.append(returnValue);
                    } else {
                        aResult.append(fAVOID_CIRCULAR_REFERENCES);
                    }
                }
            }
        }
        aResult.append(NEW_LINE);
    }

    private static String getMethodNameMinusGet(Method aMethod) {
        String result = aMethod.getName();
        if (result.startsWith(fGET)) {
            result = result.substring(fGET.length());
        }
        return result;
    }

    /**
     * Return value is possibly-null.
     */
    private static Object getMethodReturnValue(Object aObject, Method aMethod) {
        Object result = null;
        try {
            result = aMethod.invoke(aObject, fNO_ARGS);
        } catch (IllegalAccessException ex) {
            vomit(aObject, aMethod);
        } catch (InvocationTargetException ex) {
            vomit(aObject, aMethod);
        }
        result = dontShowPasswords(result, aMethod);
        return result;
    }

    private static Method getMethodFromName(Class aSpecialClass, String aMethodName) {
        Method result = null;
        try {
            result = aSpecialClass.getMethod(aMethodName, fNO_PARAMS);
        } catch (NoSuchMethodException ex) {
            vomit(aSpecialClass, aMethodName);
        }
        return result;
    }

    private static void vomit(Object aObject, Method aMethod) {
        fLogger.error(
                "Cannot get return value using reflection. Class: "
                + aObject.getClass().getName()
                + " Method: "
                + aMethod.getName());
    }

    private static void vomit(Class aSpecialClass, String aMethodName) {
        fLogger.error(
                "Reflection fails to get no-arg method named: "
                + quote(aMethodName)
                + " for class: "
                + aSpecialClass.getName());
    }

    private static Object dontShowPasswords(Object aReturnValue, Method aMethod) {
        Object result = aReturnValue;
        Matcher matcher = PASSWORD_PATTERN.matcher(aMethod.getName());
        if (matcher.find()) {
            result = HIDDEN_PASSWORD_VALUE;
        }
        return result;
    }

}

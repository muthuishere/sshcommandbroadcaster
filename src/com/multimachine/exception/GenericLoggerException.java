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
package com.multimachine.exception;


public class GenericLoggerException extends Exception {

    String statusCode;
    String statusDescription;

    public GenericLoggerException() {
        super();
    }

    public GenericLoggerException(String message) {
        super(message);
    }

    public GenericLoggerException(String statusCode, String statusDescription) {
        super(statusCode + " - " + statusDescription);
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    public GenericLoggerException(String statusCode, String statusDescription, Throwable cause) {
        super(statusCode + " - " + statusDescription, cause);
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    public GenericLoggerException(String code, String description, String message) {
        super(message);
        statusCode = code;
        statusDescription = description;
    }

    public GenericLoggerException(String code, String description, String message, Throwable cause) {
        super(message, cause);
        statusCode = code;
        statusDescription = description;
    }
   public GenericLoggerException(String message, Throwable cause) {
        super(message, cause);
    }
   

    public GenericLoggerException(Throwable cause) {
        super(cause);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}

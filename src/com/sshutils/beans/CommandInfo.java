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
package com.sshutils.beans;

import com.sshutils.utils.StringHelper;

/**
 *
 * @author hutchuk
 */
public class CommandInfo {

    private ConnectionInfo connectionInfo;
    private String cmd;
    private String modifiedcmd;

    public String getModifiedcmd() {
        return modifiedcmd;
    }

    public void setModifiedcmd(String modifiedcmd) {
        this.modifiedcmd = modifiedcmd;
    }
    private String response;
    private String errMsg;
    private boolean bufferedOutput;

    public boolean isBufferedOutput() {
        return bufferedOutput;
    }

    public void setBufferedOutput(boolean bufferedOutput) {
        this.bufferedOutput = bufferedOutput;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private boolean error;

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return StringHelper.toString(this);
    }

}

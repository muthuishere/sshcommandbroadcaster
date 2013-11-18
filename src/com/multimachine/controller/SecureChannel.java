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
package com.multimachine.controller;

import com.jcraft.jsch.*;
import com.multimachine.beans.ServerInfo;
import com.multimachine.exception.AuthenticationException;


public class SecureChannel extends JSch {

    public SecureChannel() {
        super();
    }

    public Session getSession(String username, String pwd, String host, int port) throws JSchException {

        Session session = getSession(username, host, port);

        session.setPassword(pwd);

        // Additional SSH options.  See your ssh_config manual for
        // more options.  Set options according to your requirements.
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("Compression", "yes");
        config.put("ConnectionAttempts", "2");

        session.setConfig(config);

        return session;

    }

    public Session getSession(ServerInfo serverInfo) throws JSchException {

        Session session = getSession(serverInfo.getUser(), serverInfo.getHost(), serverInfo.getPort());

        session.setPassword(serverInfo.getPassword());

        // Additional SSH options.  See your ssh_config manual for
        // more options.  Set options according to your requirements.
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("Compression", "yes");
        config.put("ConnectionAttempts", "2");

        session.setConfig(config);

        return session;

    }

}

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
package com.sshutils.controller;

import com.sshutils.beans.CommandInfo;
import com.sshutils.beans.ProfileStyle;
import com.sshutils.listeners.MultiMessageBroadcaster;
import com.sshutils.views.console.ConsoleWindow;


import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author hutchuk
 */
public class ConsoleController  {

    ConsoleWindow console;

    private static final Logger log = Logger.getLogger(ConsoleController.class);
    MultiMessageBroadcaster multiMessageBroadcaster = null;

    public ConsoleController(MultiMessageBroadcaster multiMessageBroadcaster, ArrayList<ProfileStyle> lstProfileStyles) {
        this.multiMessageBroadcaster = multiMessageBroadcaster;
        console = new ConsoleWindow(multiMessageBroadcaster, lstProfileStyles);
        console.setVisible(true);
    }

 
    public void showParent(){
    
    multiMessageBroadcaster.showParent();
    }
    

    public void sendResponse(final CommandInfo commandInfo,final boolean flgComplete) {

        final String response = commandInfo.getResponse();

        new Thread() {
            public void run() {

                log.info("Response received " + response);

                console.onResponse(response, commandInfo.getConnectionInfo().getServerInfo().getHost(),flgComplete);

            }
        }.start();

    }

}

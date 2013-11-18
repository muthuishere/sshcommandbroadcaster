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

import com.multimachine.beans.CommandInfo;
import com.multimachine.beans.ProfileStyle;
import com.multimachine.listeners.MultiMessageBroadcaster;
import com.multimachine.views.console.ColoredSwingConsoleWindow;
import com.multimachine.views.console.ConsoleCallBack;
import com.multimachine.views.console.SwingConsoleWindow;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author hutchuk
 */
public class ColoredConsoleController implements ConsoleCallBack {

    ColoredSwingConsoleWindow console;

    private static final Logger log = Logger.getLogger(ColoredConsoleController.class);
    MultiMessageBroadcaster multiMessageBroadcaster = null;

    public ColoredConsoleController(MultiMessageBroadcaster multiMessageBroadcaster, ArrayList<ProfileStyle> lstProfileStyles) {
        this.multiMessageBroadcaster = multiMessageBroadcaster;
        console = new ColoredSwingConsoleWindow(this, lstProfileStyles);
        console.setVisible(true);
    }

    public void sendCompleteResponse(final CommandInfo commandInfo) {

        new Thread() {
            public void run() {

                log.info("Response Completed ");

                console.onCompleteResponse();

            }
        }.start();

    }

    public void sendResponse(final CommandInfo commandInfo) {

        final String response = commandInfo.getResponse();

        new Thread() {
            public void run() {

                log.info("Response received " + response);

                console.onResponse(response, commandInfo.getConnectionInfo().getServerInfo().getHost());

            }
        }.start();

    }

    @Override
    public void onCommand(final String cmd) {
        log.info("Command received " + cmd);

        new Thread() {
            public void run() {

                multiMessageBroadcaster.onBroadcastMessage(cmd);

            }
        }.start();

    }

}

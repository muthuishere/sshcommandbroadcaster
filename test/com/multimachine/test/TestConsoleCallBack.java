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

import com.multimachine.beans.ProfileStyle;
import com.multimachine.views.console.ConsoleCallBack;
import com.multimachine.views.console.ColoredSwingConsoleWindow;
import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author hutchuk
 */
public class TestConsoleCallBack implements ConsoleCallBack {

    ColoredSwingConsoleWindow console;
    private static final Logger log = Logger.getLogger(TestConsoleCallBack.class);

    public TestConsoleCallBack(String bashName) {
        ArrayList<ProfileStyle> lstProfileStyles = new ArrayList<ProfileStyle>();

        ProfileStyle profileStyle = new ProfileStyle();
        profileStyle.setHostName("localhost");
        profileStyle.setColor(Color.RED);
        lstProfileStyles.add(profileStyle);
        console = new ColoredSwingConsoleWindow(this, lstProfileStyles);
        console.setVisible(true);
    }

    @Override
    public void onCommand(final String cmd) {
        log.info("Command received " + cmd);

        new Thread() {
            public void run() {

                try {
                    console.onResponse("Halo" + cmd, "localhost");
                    Thread.sleep(1000);
                    console.onCompleteResponse();
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(TestConsoleCallBack.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.start();
    }

}

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

import com.multimachine.beans.ConnectionInfo;
import com.multimachine.beans.Settings;
import com.multimachine.utils.CryptHelper;
import com.multimachine.utils.FileHelper;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class SettingsController {

    public String filepath = "settings.xml";
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SettingsController.class);
    XStream xstream = null;

    public SettingsController() {
        xstream = new XStream();
        xstream.alias("server", ConnectionInfo.class);
        xstream.alias("settings", Settings.class);

        xstream.aliasField("serverInfo", ConnectionInfo.class, "serverInfo");
        xstream.aliasField("tunnelInfo", ConnectionInfo.class, "gatewayInfo");
        xstream.addImplicitCollection(Settings.class, "list");
    }

    public void printSettings(Settings settings) {

        for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

            log.info(connectionInfo.toString());
        }

    }

    public Settings retrieveSettingsFromFile() {
        try {
            if(!FileHelper.isFileExists(filepath))
                throw new Exception("Settings file does not exists");
            
            String contents = FileHelper.readFileAsString(filepath);

            Settings settings = (Settings) xstream.fromXML(contents);
            //log.info("====Encrypted settings==========");
            // printSettings(settings);
            for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

                if (null != connectionInfo.getServerInfo() && null != connectionInfo.getServerInfo().getPassword()) {
                    connectionInfo.getServerInfo().setPassword(CryptHelper.decrypt(connectionInfo.getServerInfo().getPassword()));
                }

                //  log.info("Server Info " + connectionInfo.getServerInfo().toString());
                //  log.info("Server password Decrypted" + connectionInfo.getServerInfo().getPassword());
                if (null != connectionInfo.getGatewayInfo() && null != connectionInfo.getGatewayInfo().getPassword()) {
                    connectionInfo.getGatewayInfo().setPassword(CryptHelper.decrypt(connectionInfo.getGatewayInfo().getPassword()));
                }

            }
            //log.info("====Decrypted settings=============");
            // printSettings(settings);
            return settings;
        } catch (IOException ex) {
            log.error(ex);
        }catch (Exception ex) {
            log.error(ex);
        }
        //return empty 
        Settings settings =new Settings();
        settings.setList(new ArrayList<ConnectionInfo>());
        return new Settings();

    }

    public boolean saveSettingsasXml(Settings settings) {

        boolean flgSuccess = false;
        //   log.info("====Decrypted settings=============");
        //      printSettings(settings);

        for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

            if (null != connectionInfo.getServerInfo() && null != connectionInfo.getServerInfo().getPassword()) {
                connectionInfo.getServerInfo().setPassword(CryptHelper.encrypt(connectionInfo.getServerInfo().getPassword()));
            }

            if (null != connectionInfo.getGatewayInfo() && null != connectionInfo.getGatewayInfo().getPassword()) {
                connectionInfo.getGatewayInfo().setPassword(CryptHelper.encrypt(connectionInfo.getGatewayInfo().getPassword()));
            }

        }
        //  log.info("====Encrypted settings==========");
        //   printSettings(settings);

        String xml = xstream.toXML(settings);
        try {
            FileHelper.writetoFile(filepath, xml);
            flgSuccess = true;
        } catch (IOException ex) {
            log.error(ex);
        }
        return flgSuccess;

    }
}

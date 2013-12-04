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
package com.sshutils.utils;

import com.sshutils.beans.ConnectionInfo;
import com.sshutils.beans.ServerInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class ImportHelper {

    private static final Logger log = Logger.getLogger(ImportHelper.class);

    static final String SESSION_ID = "[Sessions\\";

    public static ArrayList<ConnectionInfo> importWinscp(String filePath) throws Exception {
        ArrayList<ConnectionInfo> lstConnectionInfo = new ArrayList<ConnectionInfo>();
        try {

            String contents = FileHelper.readFileAsString(filePath);
//log.info(contents);
            String lines[] = contents.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {

                String line = lines[i];
                String profileName="";
                if (line.startsWith(SESSION_ID)) {

                    
                    profileName=line.replace(SESSION_ID, "");
                    profileName=profileName.replace("]", "");
                    int k = i + 1;

                    ServerInfo serverInfo = new ServerInfo();
                    ServerInfo gatewayInfo = new ServerInfo();

                    while (k<lines.length &&  lines[k].contains("=")) {

                        String token = lines[k].split("=")[0];
                        String value = lines[k].split("=")[1];

                        if (token.equals("HostName")) {
                            serverInfo.setHost(value);
                        } else if (token.equals("PortNumber")) {
                            serverInfo.setPort(Integer.parseInt(value));
                        } else if (token.equals("UserName")) {
                            serverInfo.setUser(value);
                        } else if (token.equals("TunnelHostName")) {
                            gatewayInfo.setHost(value);
                        } else if (token.equals("TunnelUserName")) {
                            gatewayInfo.setUser(value);
                        } else if (token.equals("TunnelPortNumber")) {
                            gatewayInfo.setPort(Integer.parseInt(value));
                        }

                        k++;

                    }

                    ConnectionInfo connectionInfo = new ConnectionInfo();
                    if (null != serverInfo.getHost()) {
                        connectionInfo.setServerInfo(serverInfo);

                        if (null != gatewayInfo.getHost()) {
                            connectionInfo.setGatewayInfo(gatewayInfo);
                        }

                        if(StringHelper.isEmpty(profileName))
                        profileName=serverInfo.getUser() + "@" + serverInfo.getHost() + "~" + (lstConnectionInfo.size() + 1);
                        
                        
                        connectionInfo.setProfileName(profileName);

                    }
                    lstConnectionInfo.add(connectionInfo);

                    i = k;
                }

            }

            log.info("Successfully Imported Settings");
        } catch (Exception e) {
            log.error("Error while Importing", e);
            throw new Exception("Unable to Import settings" + e.toString());
        }
        
        return lstConnectionInfo;

    }

}

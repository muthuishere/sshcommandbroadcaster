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

import com.multimachine.beans.ConnectionInfo;
import com.multimachine.beans.LogData;
import com.multimachine.beans.ServerInfo;
import java.util.Random;

public class TestData {

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static ConnectionInfo getTestConnectionInfo() {

        ConnectionInfo connectionInfo = new ConnectionInfo();
        String host = "Z1587azz" + randInt(1000, 19999);

        String user = "testuser";

        String pwd = "!testuser";

        connectionInfo.setProfileName(user + "@" + host);
        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setHost(host);
        serverInfo.setUser(user);
        serverInfo.setPassword(pwd);

        host = "tunnell.testaccess.net";

        user = "tunneluser";

        pwd = "tunneluser";

        ServerInfo gatewayInfo = new ServerInfo();

        gatewayInfo.setHost(host);
        gatewayInfo.setUser(user);
        gatewayInfo.setPassword(pwd);

        connectionInfo.setGatewayInfo(gatewayInfo);
        connectionInfo.setServerInfo(serverInfo);

        return connectionInfo;
    }

}

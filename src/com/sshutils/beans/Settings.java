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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class Settings {

    private static final Logger log = Logger.getLogger(Settings.class);
    private List<ConnectionInfo> list;

    public void setList(List<ConnectionInfo> list) {
        this.list = list;
    }

    public Settings() {
        list = new ArrayList<ConnectionInfo>();
    }

    public void add(ConnectionInfo p) {
        list.add(p);
    }

    public List<ConnectionInfo> getConnectionInfo() {
        return list;
    }
}

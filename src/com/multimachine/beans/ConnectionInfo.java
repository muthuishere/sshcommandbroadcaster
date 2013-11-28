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
package com.multimachine.beans;

import com.multimachine.utils.StringHelper;
import java.awt.Color;


public class ConnectionInfo {

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getGatewayInfo() {
        return gatewayInfo;
    }

    public void setGatewayInfo(ServerInfo gatewayInfo) {
        this.gatewayInfo = gatewayInfo;
    }

    private ServerInfo serverInfo;
    private ServerInfo gatewayInfo;
    private String profileName;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getProfileName() {
        return StringHelper.defaultString(profileName);
    }

    
     public String getFolderName() {
         if(!StringHelper.isEmpty(profileName)  && profileName.contains("/") ){
         
           
           
         
                 String folderName =profileName.substring(0, profileName.lastIndexOf(getOnlyProfileName()));
                 
                 return folderName;
         }
         
        return "";
    }
     
      public String getOnlyProfileName() {
        if(!StringHelper.isEmpty(profileName) ){
         
             int len=profileName.split("/").length;
             String name=profileName;
             if(len > 0)                 
                     name = profileName.split("/")[len-1];
             
             return name;
         }
        return "";
    }
      
    public void setProfileName(String profileName) {
        this.profileName = profileName;

    }
    
    @Override
    
    public boolean equals(Object compareObj){
    if (compareObj == null) return false;
    if (compareObj == this) return true;
    if (!(compareObj instanceof ConnectionInfo))return false;
    
    ConnectionInfo otherConnectionInfo = (ConnectionInfo)compareObj;
    
    return this.profileName.equals(otherConnectionInfo.getProfileName());
    
}

    @Override
    public String toString() {
        return StringHelper.toString(this);
    }

}

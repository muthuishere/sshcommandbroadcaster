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

import java.net.Socket;
import java.io.*;

import com.jcraft.jsch.*;


public class SecureProxy implements Proxy {

    public SecureProxy(Session gateway) {
        this.gateway = gateway;
    }

    private Session gateway;

    private ChannelDirectTCPIP channel;
    private InputStream iStream;
    private OutputStream oStream;

    /**
     * closes the socket + streams.
     */
    public void close() {
        channel.disconnect();
    }

    /**
     * connects to the remote server.
     *
     * @param ignore the socket factory. This is not used.
     * @param host the remote host to use.
     * @param port the port number to use.
     * @param timeout the timeout for connecting. (TODO: This is not used, for
     * now.)
     * @throws Exception if there was some problem.
     */
    public void connect(SocketFactory ignore, String host,
            int port, int timeout)
            throws Exception {
        channel = (ChannelDirectTCPIP) gateway.openChannel("direct-tcpip");
        channel.setHost(host);
        channel.setPort(port);
        // important: first create the streams, then connect.
        iStream = channel.getInputStream();
        oStream = channel.getOutputStream();
        channel.connect();
    }

    /**
     * Returns an input stream to read data from the remote server.
     */
    public InputStream getInputStream() {
        return iStream;
    }

    public OutputStream getOutputStream() {
        return oStream;
    }

    public Socket getSocket() {
        // there is no socket.
        return null;
    }

}

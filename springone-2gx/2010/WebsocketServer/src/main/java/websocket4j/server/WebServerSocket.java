package websocket4j.server;

/**
 * This file is part of GNU WebSocket4J.
 * Copyright (C) 2010  Marek Aaron Sapota
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent modules,
 * and to copy and distribute the resulting executable under terms of your choice,
 * provided that you also meet, for each linked independent module, the terms and
 * conditions of the license of that module. An independent module is a module
 * which is not derived from or based on this library. If you modify this library,
 * you may extend this exception to your version of the library, but you are not
 * obligated to do so. If you do not wish to do so, delete this exception
 * statement from your version.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Server socket that accepts {@link WebSocket}s.
 */
public class WebServerSocket {

    private ServerSocket serverSocket;
    private Integer timeout = 0;

    /**
     * Creates new WebServerSocket on random port.
     * 
     * @throws IOException
     *             thrown when there is an error while creating a new socket.
     */
    public WebServerSocket() throws IOException {
        this(0);
    }

    /**
     * Creates new WebServerSocket on given port.
     * 
     * @param port
     *            port to use
     * @throws IOException
     *             thrown when there is an error while creating a new socket.
     */
    public WebServerSocket(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Sets maximum timeout for accept.
     * 
     * @param timeout
     *            maximum timeout for accept in milliseconds
     * @throws SocketException
     *             thrown when there is an error while setting the timeout
     */
    public void setSoTimeout(Integer timeout) throws SocketException {
        serverSocket.setSoTimeout(timeout);
        this.timeout = timeout;
    }

    /**
     * Returns local port to which this socket is bound.
     * 
     * @return local port
     */
    public Integer getLocalPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * Checks if this socket is closed.
     * 
     * @return true if the socket is closed
     */
    public Boolean isClosed() {
        return serverSocket.isClosed();
    }

    /**
     * Closes the socket.
     * 
     * @throws IOException
     *             thrown when an error occurs while closing the socket
     */
    public void close() throws IOException {
        serverSocket.close();
    }

    /**
     * Accepts a new connection to the socket.
     * 
     * @return ready to use {@link WebSocket} that was accepted
     * @throws IOException
     *             thrown when there is a problem with connection or protocol
     *             error
     */
    public WebSocket accept() throws IOException {
        Socket socket = serverSocket.accept();
        return new WebSocket(socket, timeout);
    }

}

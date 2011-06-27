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
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import websocket4j.AbstractWebSocket;

/**
 * Socket wrapper that implements server side of the WebSocket protocol.
 * 
 * http://www.whatwg.org/specs/web-socket-protocol/
 */
public class WebSocket extends AbstractWebSocket {

    /**
     * Creates new web socket ready for use.
     * 
     * @param socket
     *            socket to use for communication
     * @throws IOException
     *             thrown when there is a problem with connection or protocol
     *             error
     */
    public WebSocket(Socket socket) throws IOException {
        this(socket, 0);
    }

    /**
     * Creates new web socket ready for use, if opening handshake doesn't finish
     * in given time, socket creation will be aborted (if given time is 0, it
     * will wait as long as needed).
     * 
     * @param socket
     *            socket to use for communication
     * @param timeout
     *            maximum time for handshake in milliseconds
     * @throws IOException
     *             thrown when there is a problem with connection or protocol
     *             error
     */
    public WebSocket(Socket socket, Integer timeout) throws IOException {
        super(socket, timeout);
    }

    private String requestUri;

    /**
     * Returns Request-URI part of the handshake header as specified in HTTP
     * protocol. This will be null if the handshake failed before Request-URI
     * was sent.
     * 
     * @return Request-URI part of the handshake header.
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * Performs an opening handshake with the client. If this method completes
     * successfully, a connection with the client is established.
     * 
     * @param args should be empty
     * @throws IOException
     *             thrown when there is a problem in socket communication or
     *             client does not follow the handshake protocol
     * @throws NoSuchAlgorithmException
     *             thrown when JVM doesn't support MD5 algorithm
     */
    @Override
    protected void handshake(Object... args) throws IOException,
            NoSuchAlgorithmException {
        String line = readLine();
        String[] requestLine = line.split(" ");
        if (requestLine.length < 2)
            throw new IOException("Wrong Request-Line format: " + line);
        requestUri = requestLine[1];
        String host = null, origin = null, cookie = null;
        Boolean upgrade = false, connection = false;
        Long[] keys = new Long[2];

        while (!(line = readLine()).equals("")) {
            String[] parts = line.split(": ", 2);
            if (parts.length != 2)
                throw new IOException("Wrong field format: " + line);
            String name = parts[0].toLowerCase();
            String value = parts[1].toLowerCase();

            if (name.equals("upgrade")) {
                if (!value.equals("websocket"))
                    throw new IOException("Wrong value of upgrade field: "
                            + line);
                upgrade = true;
            } else if (name.equals("connection")) {
                if (!value.equals("upgrade"))
                    throw new IOException("Wrong value of connection field: "
                            + line);
                connection = true;
            } else if (name.equals("host")) {
                host = value;
            } else if (name.equals("origin")) {
                origin = value;
            } else if ((name.equals("sec-websocket-key1"))
                    || (name.equals("sec-websocket-key2"))) {
                Integer spaces = new Integer(0);
                Long number = new Long(0);
                for (Character c : value.toCharArray()) {
                    if (c.equals(' '))
                        ++spaces;
                    if (Character.isDigit(c)) {
                        number *= 10;
                        number += Character.digit(c, 10);
                    }
                }
                number /= spaces;
                if (name.endsWith("key1"))
                    keys[0] = number;
                else
                    keys[1] = number;
            } else if (name.equals("cookie")) {
                cookie = value;
            } else {
                throw new IOException("Unexpected header field: " + line);
            }
        }
        if ((!upgrade) || (!connection) || (host == null) || (origin == null)
                || (keys[0] == null) || (keys[1] == null))
            throw new IOException("Missing handshake arguments");
        byte[] token = readBytes(8);

        writeLine("HTTP/1.1 101 WebSocket Protocol Handshake");
        writeLine("Upgrade: WebSocket");
        writeLine("Connection: Upgrade");
        writeLine("Sec-WebSocket-Origin: " + origin);
        writeLine("Sec-WebSocket-Location: ws://" + host + requestUri);
        if (cookie != null)
            writeLine("cookie: " + cookie);
        writeLine("");
        out.write(makeResponseToken(keys[0].intValue(), keys[1].intValue(),
                token));
        out.flush();
    }

}

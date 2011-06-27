package websocket4j.client;

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
import java.util.Random;

import websocket4j.AbstractWebSocket;

/**
 * Socket wrapper that implements client side of the WebSocket protocol.
 * 
 * http://www.whatwg.org/specs/web-socket-protocol/
 */
public class WebSocket extends AbstractWebSocket {

    /**
     * Creates new web socket connected to given host and port, requesting given
     * URI.
     * 
     * @param host
     *            server host name
     * @param port
     *            server port number
     * @param requestUri
     *            URI to request
     * @param timeout
     *            number of milliseconds that the opening handshake can take
     *            before being cancelled (if 0 then it will block forever)
     * @throws IOException
     *             thrown when there is a problem with connection or protocol
     *             error
     */
    public WebSocket(String host, Integer port, String requestUri,
            Integer timeout) throws IOException {
        super(new Socket(host, port), timeout, host, port, requestUri);
    }

    /**
     * Creates new web socket connected to given host and port, requesting given
     * URI. This will block untill there is response from the other party.
     * 
     * @param host
     *            server host name
     * @param port
     *            server port number
     * @param requestUri
     *            URI to request
     * @throws IOException
     *             thrown when there is a problem with connection or protocol
     *             error
     */
    public WebSocket(String host, Integer port, String requestUri)
            throws IOException {
        this(host, port, requestUri, 0);
    }

    /**
     * Performs an opening handshake with the server. If this method completes
     * successfully, a connection with the server is established.
     * 
     * @param args
     *            this should be String host, Integer port, String requestUri
     * @throws IOException
     *             thrown when there is a problem in socket communication or
     *             server does not follow the handshake protocol
     * @throws NoSuchAlgorithmException
     *             thrown when JVM doesn't support MD5 algorithm
     */
    @Override
    protected void handshake(Object... args) throws IOException,
            NoSuchAlgorithmException {
        String host = (String) args[0];
        Integer port = (Integer) args[1];
        String requestUri = (String) args[2];

        String[] keyStrings = new String[2];
        Integer[] keys = new Integer[2];
        byte[] token = new byte[8];

        Random random = new Random();
        random.nextBytes(token);

        for (Integer i = 0; i < 2; ++i) {
            keys[i] = Math.abs(random.nextInt());
            Integer spaces = random.nextInt(10) + 1;
            Long key = keys[i].longValue() * spaces.longValue();
            String keyString = key.toString();
            char garbage[] = new char[random.nextInt(10) + 5];
            for (Integer j = 0; j < garbage.length; ++j)
                garbage[j] = (char) (((int) 'a') + random.nextInt(26));
            char finalString[] = new char[spaces + keyString.length()
                    + garbage.length];
            Integer keyCounter = 0, garbageCounter = 0;
            for (Integer j = 0; j < finalString.length; ++j) {
                switch (random.nextInt(3)) {
                case 0:
                    if (spaces > 0) {
                        finalString[j] = ' ';
                        --spaces;
                    } else
                        --j;
                    break;
                case 1:
                    if (keyCounter < keyString.length())
                        finalString[j] = keyString.charAt(keyCounter++);
                    else
                        --j;
                    break;
                case 2:
                    if (garbageCounter < garbage.length)
                        finalString[j] = garbage[garbageCounter++];
                    else
                        --j;
                    break;
                }
            }
            keyStrings[i] = new String(finalString);
        }

        writeLine("GET " + requestUri + " HTTP/1.1");
        writeLine("Connection: Upgrade");
        writeLine("Upgrade: WebSocket");
        writeLine("Host: " + host + ":" + port.toString());
        writeLine("Origin: http://" + host);
        writeLine("Sec-WebSocket-Key1: " + keyStrings[0]);
        writeLine("Sec-WebSocket-Key2: " + keyStrings[1]);
        writeLine("");
        out.write(token);
        out.flush();

        Boolean upgrade = false, connection = false, origin = false, location = false;
        String line = readLine();
        if (!line.startsWith("HTTP/1.1 101 "))
            throw new IOException("Wrong Status-Line format: " + line);

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
            } else if (name.equals("sec-websocket-origin")) {
                if (!value.equals("http://" + host))
                    throw new IOException(
                            "Wrong value of sec-websocket-origin field: "
                                    + line);
                origin = true;
            } else if (name.equals("sec-websocket-location")) {
                if (!value.equals("ws://" + host + ":" + port + requestUri))
                    throw new IOException(
                            "Wrong value of sec-websocket-location field: "
                                    + line);
                location = true;
            } else {
                throw new IOException("Unexpected header field: " + line);
            }
        }
        if ((!upgrade) || (!connection) || (!origin) || (!location))
            throw new IOException("Missing handshake arguments");
        byte[] validChallengeResponse = makeResponseToken(keys[0], keys[1],
                token);
        byte[] challengeResponse = readBytes(validChallengeResponse.length);
        for (Integer i = 0; i < validChallengeResponse.length; ++i)
            if (challengeResponse[i] != validChallengeResponse[i])
                throw new IOException("Invalid challenge response");
    }

}

package websocket4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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

/**
 * This class implements common functionality of server and client side web
 * socket.
 */
public abstract class AbstractWebSocket {

    protected Socket socket;
    protected InputStream in;
    protected OutputStream out;
    private Object[] args;

    /**
     * Runs handshake and if it fails, stores the exception.
     */
    private class HandshakeRunner implements Runnable {

        private Exception exception = null;

        /**
         * Returns the exception that occurred while running the handshake.
         * 
         * @return the exception or null when there was no exception
         */
        public Exception getException() {
            return exception;
        }

        /**
         * Runs the handshake.
         */
        @Override
        public void run() {
            try {
                handshake(args);
            } catch (Exception e) {
                exception = e;
            }
        }

    }

    /**
     * Creates new WebSocket instance using given socket for communication and
     * limiting handshake time to given milliseconds.
     * 
     * @param socket
     *            socket that should be used for communication
     * @param timeout
     *            maximum time in milliseconds for the handshake
     * @param args
     *            arguments that will be passed to handshake call
     * @throws IOException
     *             exception thrown when there is communication error or
     *             protocol error
     */
    public AbstractWebSocket(Socket socket, Integer timeout, Object... args)
            throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
        this.args = args;
        HandshakeRunner taskBody = new HandshakeRunner();

        FutureTask<Object> task = new FutureTask<Object>(taskBody, null);
        try {
            (new Thread(task)).start();
            if (timeout > 0) {
                task.get(timeout, TimeUnit.MILLISECONDS);
            } else {
                task.get();
            }
        } catch (Exception e) {
            socket.close();
            throw new IOException("Handshake failed", e);
        }
        if (taskBody.getException() != null) {
            socket.close();
            throw new IOException("Handshake failed", taskBody.getException());
        }
    }

    /**
     * Creates a string from given byte collection.
     * 
     * @param collection
     *            collection to be converted
     * @return string made from the byte collection
     */
    protected String byteCollectionToString(Collection<Byte> collection) {
        byte[] byteArray = new byte[collection.size()];
        Integer i = 0;
        for (Iterator<Byte> iterator = collection.iterator(); iterator
                .hasNext();) {
            byteArray[i++] = iterator.next();
        }
        return new String(byteArray, Charset.forName("UTF-8"));
    }

    /**
     * Closes the connection.
     * 
     * Close is brutal, without the closing handshake. It worked better in some
     * web browsers, but it might change sometime.
     * 
     * TODO: do a proper closing handshake sometime
     */
    public void close() throws IOException {
        socket.close();
    }

    /**
     * Gets a message form the other party. Blocks if nothing is available.
     * 
     * @return message from the other party
     * @throws IOException
     *             thrown when there is a problem with the connection or
     *             protocol
     */
    public String getMessage() throws IOException {
        Vector<Byte> message = new Vector<Byte>();
        synchronized (in) {
            Integer current = in.read();
            if (current.equals(-1))
                throw new IOException("End of stream");
            if (current.equals(0xFF)) {
                current = in.read();
                if (current.equals(0x00)) {
                    close();
                    throw new IOException("Connection termination requested");
                } else {
                    throw new IOException("Wrong message format");
                }
            }
            if (!current.equals(0x00))
                throw new IOException("Wrong message format");
            current = in.read();
            while (!current.equals(0xFF)) {
                if (current.equals(-1))
                    throw new IOException("End of stream");
                message.add(current.byteValue());
                current = in.read();
            }
        }
        return byteCollectionToString(message);
    }

    /**
     * Performs an opening handshake. If this method completes successfully, a
     * connection with the other party is established.
     * 
     * @throws IOException
     *             thrown when there is a problem in socket communication or
     *             client does not follow the handshake protocol
     * @throws NoSuchAlgorithmException
     *             thrown when JVM doesn't support MD5 algorithm
     */
    protected abstract void handshake(Object... args) throws IOException,
            NoSuchAlgorithmException;

    /**
     * Checks if this socket is closed.
     * 
     * @return true if the socket is closed
     */
    public boolean isClosed() {
        return socket.isClosed();
    }

    /**
     * Reads given number of bytes from the socket.
     * 
     * @param count
     *            number of sockets to read
     * @return bytes that were read
     * @throws IOException
     *             thrown when an error occurs while reading
     */
    protected byte[] readBytes(Integer count) throws IOException {
        if (count <= 0)
            return new byte[0];
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; ++i) {
            Integer current = in.read();
            if (current.equals(-1))
                throw new IOException("End of stream");
            bytes[i] = current.byteValue();
        }
        return bytes;
    }

    /**
     * Reads line (terminated by "\r\n") from the socket.
     * 
     * @return line that was read
     * @throws IOException
     *             thrown when an error occurs while reading
     */
    protected String readLine() throws IOException {
        Vector<Byte> line = new Vector<Byte>();
        Integer last = in.read();
        if (last.equals(-1))
            throw new IOException("End of stream");
        Integer current = in.read();
        while (!((last.equals(0x0D)) && (current.equals(0x0A)))) {
            if (current.equals(-1))
                throw new IOException("End of stream");
            line.add(last.byteValue());
            last = current;
            current = in.read();
        }
        return byteCollectionToString(line);
    }

    /**
     * Sends a message to the other party.
     * 
     * @param message
     *            message to be sent
     * @throws IOException
     *             thrown when there is a problem with the connection.
     */
    public void sendMessage(String message) throws IOException {
        synchronized (out) {
            out.write(0x00);
            out.write(message.getBytes(Charset.forName("UTF-8")));
            out.write(0xFF);
            out.flush();
        }
    }

    /**
     * Writes given line to socket. Line is terminated by "\r\n".
     * 
     * @param line
     *            line to be written
     * @throws IOException
     *             thrown when an error occurs while writing
     */
    protected void writeLine(String line) throws IOException {
        out.write(line.getBytes(Charset.forName("UTF-8")));
        out.write(0x0D);
        out.write(0x0A);
    }

    /**
     * Generates response to client's opening handshake challenge.
     * 
     * @param key1
     *            value of Sec-WebSocket-Key1 field
     * @param key2
     *            value of Sec-WebSocket-Key2 field
     * @param token
     *            bytes sent after the fields
     * @return response to this challenge
     * @throws NoSuchAlgorithmException
     *             thrown if JVM doesn't support MD5 algorithm
     */
    protected byte[] makeResponseToken(int key1, int key2, byte[] token)
            throws NoSuchAlgorithmException {
        MessageDigest md5digest = MessageDigest.getInstance("MD5");
        for (Integer i = 0; i < 2; ++i) {
            byte[] asByte = new byte[4];
            int key = (i == 0) ? key1 : key2;
            asByte[0] = (byte) (key >> 24);
            asByte[1] = (byte) ((key << 8) >> 24);
            asByte[2] = (byte) ((key << 16) >> 24);
            asByte[3] = (byte) ((key << 24) >> 24);
            md5digest.update(asByte);
        }
        md5digest.update(token);
        return md5digest.digest();
    }

}

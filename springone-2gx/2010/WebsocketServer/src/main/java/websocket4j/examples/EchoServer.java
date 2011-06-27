package websocket4j.examples;

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

import websocket4j.server.WebServerSocket;
import websocket4j.server.WebSocket;

/**
 * Example presenting an echo server using WebSocket4j.
 */
public class EchoServer extends Thread {

    private WebSocket ws;

    public EchoServer(WebSocket ws) {
        this.ws = ws;
    }

    private void handleConnection() {
        try {
            while (true) {
                String message = ws.getMessage();
                ws.sendMessage(message);
                System.out.println("Received: " + message);
                if (message.equals("exit"))
                    break;
            }
        } catch (IOException e) {
        }finally {
            try {
            ws.close();
            } catch (IOException e) {
            }
        }
    }

    public void run() {
        handleConnection();
    }

    public static void main(String[] args) throws IOException {
        WebServerSocket socket = new WebServerSocket(5555);
        try {
            while (true) {
                WebSocket ws = socket.accept();
                System.out.println("GET " + ws.getRequestUri());
                if (ws.getRequestUri().equals("/echo"))
                    (new EchoServer(ws)).start();
                else {
                    System.out.println("Unsupported Request-URI");
                    try {
                    ws.close();
                    } catch (IOException e) {
                    }
                }
            }

        } finally {
            socket.close();
        }
    }

}

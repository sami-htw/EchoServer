/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface Network {

  /**
   * @param hostname
   * @param port_number
   * @throws IOException
   */

  void connect(String hostname, int port_number) throws IOException;

  /**
   * @param port_number
   * @throws IOException
   */

  void open(int port_number) throws IOException;

  /**
   * @return
   * @throws IOException
   */

  InputStream getInputStream() throws IOException;

  /**
   * @return
   * @throws IOException
   */

  OutputStream getOutputStream() throws IOException;

  /**
   * @throws IOException
   */

  void close() throws IOException;

  /**
   * @param id
   * @param message
   */

  void handle(int id, String message);
}
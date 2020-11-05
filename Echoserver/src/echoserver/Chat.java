/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

/**
 * @author sami
 */
public interface Chat {

  /**
   * @param message
   * @throws Exception
   */
  void writeMessage(String message) throws Exception;

  /**
   * @return
   * @throws Exception
   */

  String[] readMessage() throws Exception;

}
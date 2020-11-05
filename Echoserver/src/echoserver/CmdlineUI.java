/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.*;
import java.util.Arrays;

import static java.lang.System.*;


public class CmdlineUI extends NetworkImplementation {
  //first write a constructor initialize to streams
  //second removing some prints -> replacing them with string messages
  //third organize throws/exceptions
  //last use interface when initializing methods

  public Chat chat;
  //  public Network network;
  public static final String EXIT = "exit";
  public static final String CLOSE = "close";
  public static final String WRITE = "write";
  public static final String READ = "read";
  public static final String CONNECT = "connect";
  public static final String OPEN = "open";
  public static final String OPEN_PORT = "Server Connected";//,(input (stop) to go out) ;)";
  public static final String EXIT_MESSAGE = "Exit the GUI,goodbye :)";
  public static final String WRITING_MESSAGE = "Writing...";
  public static final String READING_MESSAGE = "Reading...";
  public static final String FILE_CONTENT_MESSAGE = "File's content: ";
  public static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command";
  public static final String CONNECT_MESSAGE = "Client Connected";    //,(input (stop) to go out) ;)";
  public static final String CLOSE_MESSAGE = "Socket is closed now!";
  public static final String UI_TOKEN = "\n >\t";

  private String message;
  private String[] rawMessageArray;
  private String command;

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String[] getRawMessageArray() {
    return rawMessageArray;
  }

  public void setRawMessageArray(String[] rawMessageArray) {
    this.rawMessageArray = rawMessageArray;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String rawMessage) {
    this.message = rawMessage;
  }

  public CmdlineUI() {
    chat = new Chat_Implementation();
    network = new NetworkImplementation();
  }

  public void runUI(InputStream is, OutputStream os) throws Exception {
    String commandLineString = "";
    BufferedReader bf = new BufferedReader(new InputStreamReader(is));
    boolean terminal_active = true;

    try {
      StringBuilder usage_information = new StringBuilder("Welcome into Chat System : ");
      usage_information.append("\n insert one of the Commands");
      usage_information.append("\n input (write) to write into the file");
      usage_information.append("\n input (read) to read the content of file");
      usage_information.append("\n input (open) to connect to the Server");
      usage_information.append("\n input (connect) to connect to the Client");
      usage_information.append("\n input (close) to close the socket ");
      usage_information.append("\n input (exit) to close the chat");

      System.out.print(usage_information);
      while (terminal_active) {
        System.out.print(CmdlineUI.UI_TOKEN);
        commandLineString = bf.readLine();
        if (commandLineString == null) {
          break;
        }
        this.setMessage(parseInput(commandLineString));
        terminal_active = verifyUserInput(os);
      }
    } catch (IOException e) {
      System.err.println("Exception when reading from command line: " + e.getLocalizedMessage());
    }
  }

  //Separating command from the actual message out of the user's input.
  private String parseInput(String input) {
    //TODO: Do I really need this split("") and trim() ?
    String[] rawMessageArray = input.trim().split(" ");
    this.setRawMessageArray(rawMessageArray);

    String command = rawMessageArray[0];
    this.setCommand(command);

    String message = String
        .join(" ", Arrays.copyOfRange(rawMessageArray, 1, rawMessageArray.length));
    this.setMessage(message);
    return message;
  }

  private boolean verifyUserInput(OutputStream os) throws Exception {
    PrintStream ps = new PrintStream(os);

    String command = this.getCommand().toLowerCase();
    String message = this.getMessage();

    switch (command) {
      case EXIT:
        ps.println(EXIT_MESSAGE);
        return false;
      case WRITE:
        ps.println(WRITING_MESSAGE);
        chat.writeMessage(message);
        return true;
      case READ:
        ps.println(READING_MESSAGE);
        this.showReceivedMessage(chat.readMessage());
        return true;

      case OPEN:
        ps.println(OPEN_PORT);
        network.open(PORT_NUMBER);
        // network.connect( HOST_NAME, PORT_NUMBER);
        return true;
      case CONNECT:

        ps.println(CONNECT_MESSAGE);
        network.connect(HOST_NAME, PORT_NUMBER);
        return true;
      case CLOSE:
        ps.println(CLOSE_MESSAGE);
        network.close();
        return false;
      default:
        ps.println(UNKNOWN_COMMAND_MESSAGE);
        return true;
    }
  }

  private void showReceivedMessage(String[] messages) {
    out.println(FILE_CONTENT_MESSAGE);
    for (String word : messages) {
      out.println(word);
    }
  }

  public static void main(String[] args) {
    CmdlineUI ui = new CmdlineUI();

    try {
      ui.runUI(System.in, System.out);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}

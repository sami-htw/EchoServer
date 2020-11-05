/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

/**
 * @author sami
 */

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkImplementation implements Network {

  public static final int PORT_NUMBER = 7777;
  public static final String HOST_NAME = "127.0.0.1";
  public Socket socket;
  public ServerSocket serversocket;
  public Network network;
  private List<MultiThreadsServer> clients = new ArrayList<MultiThreadsServer>();
  private InputStream is;
  private OutputStream os;

  @Override
  // Wenn wir in der console (connect) eingeben ,wird ein client mit dem multithreaded server verbunden
  public void connect(String hostname, int port_number) throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    try {
      this.socket = new Socket(hostname, port_number);
      int id = this.socket.getLocalPort();
      System.out.println("Client " + id + " starts conversation");
      DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());

      // mit diesem thread werden die Nachrichten vom Server an Allen Clients gesendet
      this.is = this.socket.getInputStream();
      this.os = this.socket.getOutputStream();
      MultithreadsClients r2 = new MultithreadsClients(this.is);
      r2.start();

      while (true) {
        String message = br.readLine();
        dos.writeUTF(message);
        dos.flush();
      }
    } catch (Exception ex) {
      System.out.println("Error,while connecting with server :(");
    }
  }

  @Override
  // Der Server
  public void open(int port_number) throws IOException {
    this.serversocket = new ServerSocket(PORT_NUMBER);

    try {
      // eine Schleife drum herum ,um mehrere clients mit dem server zu verbinden
      while (true) {
        this.socket = serversocket.accept();
        this.is = this.socket.getInputStream();
        this.os = this.socket.getOutputStream();
        MultiThreadsServer threadServer = new MultiThreadsServer(this, this.is,
            this.socket.getPort());
        clients.add(threadServer);
        threadServer.start();

      }
    } catch (IOException io) {
      // Hinweis: Fehlermeldung
      System.out.println("Server acception error: " + io);
    }
  }

  @Override
  public void close() throws IOException {
    try {
      if (this.socket != null) {
        this.socket.close();
      }
    } catch (IOException ex) {
      System.err.println("problem with closing the Socket !");
    }

  }

  @Override
  //broadcast function, message wird an alle clients gesendet
  public void handle(int id, String message) {
    for (MultiThreadsServer client : clients) {
      if (client.ID != id) {
        client.send("Client " + id + " > " + message);
      }
    }
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if (this.is == null) {
      throw new IOException("not yet connected");
    }

    return this.is;

  }

  @Override
  public OutputStream getOutputStream() throws IOException {

    if (this.os == null) {
      throw new IOException("not yet connected");
    }

    return this.os;
  }

  //Thread Server um ein Client zu dienen
  public class MultiThreadsServer extends Thread {

    private Network server = null;
    private int ID = 1;
    private final InputStream is;
    private DataOutputStream streamOut = null;

    public MultiThreadsServer(Network _server, InputStream inputStream, int id) throws IOException {
      super();
      this.is = inputStream;
      server = _server;
      ID = id;
      streamOut = new DataOutputStream(socket.getOutputStream());
    }

    public void send(String msg) {
      try {
        streamOut.writeUTF(msg);
        streamOut.flush();
      } catch (IOException ioe) {
        System.out.println(ID + " ERROR sending: " + ioe.getMessage());
      }
    }

    @Override
    // hier werden alle geschriebene nachrichten von clients an dem server weiter geleitet
    // es hat alles einwandfrei funktioniert
    public void run() {
      System.out.println("## Client " + ID + " connected ##");
      DataInputStream din = null;

      try {
        din = new DataInputStream(this.is);

        while (true) {
          String str = din.readUTF();
          System.out.println("Client " + ID + " > " + str);
          server.handle(ID, str);

        }

      } catch (Exception e) {
        System.out.println("Error, while connecting with Server" + e.getLocalizedMessage());
      } finally {
        try {
          din.close();
        } catch (Exception ex) {
          System.out.println("connection was successful ");
        }
      }
    }

  }

  // hiermit sollen alle geschriebene Nachrichten vom Server an clients geschickt werden, je nachdem wie viele clients , mit dem server verbunden sind
  public class MultithreadsClients extends Thread {

    private final InputStream is;

    public MultithreadsClients(InputStream inputStream) throws IOException {
      super();
      this.is = inputStream;
    }

    @Override

    public void run() {
      DataInputStream din = null;
      String line;

      try {
        din = new DataInputStream(this.is);

        while (true) {
          line = din.readUTF();
          System.out.println(line);

        }
      } catch (Exception ex) {

        System.out.println("Bye :) (" + ex.getMessage() + ")");
        //ex.printStackTrace();
      } finally {
        try {
          din.close();
        } catch (Exception ex) {
          System.out.println("connection was successful ");
        }
      }

    }

  }
}

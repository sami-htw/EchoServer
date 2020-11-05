/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

/**
 * @author sami
 */

import java.io.*;

import static java.lang.System.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Chat_Implementation implements Chat {


  File file = new File("/home/sami/Desktop/file.txt");

  @Override
  public void writeMessage(String message) throws Exception {

    System.out.println("Start with Writing mode... (input (stop) to go out!)");
    try {

      InputStreamReader inputStreamReader = new InputStreamReader(
          System.in); // A stream for reading from the console

      BufferedReader bufferedReader = new BufferedReader(
          inputStreamReader); // Connect InputStreamReader to a BufferedReader

      FileWriter fileReader = new FileWriter(file); // A stream that connects to the text file
      BufferedWriter bufferedWriter = new BufferedWriter(
          fileReader); // Connect the FileWriter to the  BufferedWriter

      String line;
      boolean continuee = true;
      while (!(line = bufferedReader.readLine()).equals("stop")) {
        continuee = false;
        bufferedWriter.write(line);
      }

      // Close the stream
      bufferedWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @Override
  public String[] readMessage() throws Exception {
    System.out.println("start with reading mode:");
    ArrayList<String> fileContentList = new ArrayList<String>();
    String[] fileContent = new String[0];
    String[] errorArray = {"Error!"};
    try {
      FileInputStream fis = new FileInputStream(this.file);
      InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
      BufferedReader bf = new BufferedReader(isr);
      //while(bf.readLine() != null) {
      if (fileContentList.add(bf.readLine())) {
        fileContent = fileContentList.toArray(fileContent);
        //}
        return fileContent;
      }
      out.println("Could not write to array list in order to read line");
      return errorArray;

    } catch (FileNotFoundException e) {
      err.println("File is not exist");
      exit(0);
      return errorArray;
    }
  }


}

package com.example.init_project;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.*;
//
//public class Client extends AsyncTask<Void, Void, Void> {
//  private Socket socket = null;
//  private DataOutputStream dataOutputStream = null;
//  private DataInputStream dataInputStream = null;
//
//  private String addr = "";
//  private int port = 0;
//
//  public Client(String addr, int port) {
//    this.addr = addr;
//    this.port = port;
//
//    try {
//      this.socket = new Socket(this.addr, this.port);
//      Log.d("Socket", "Connect to server " + this.socket.getRemoteSocketAddress().toString());
//      this.dataInputStream = new DataInputStream(this.socket.getInputStream());
//      this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
//    } catch (UnknownHostException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void send(String request) {
//    try {
//      this.dataOutputStream.writeUTF(request);
//      Log.d("Socket", "Send to server" + request);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public String recv() {
//    String response;
//    try {
//      response = this.dataInputStream.readUTF();
//      Log.d("Socket", "From server: " + response);
//      return response;
//    } catch (IOException e) {
//      e.printStackTrace();
//      return "";
//    }
//
//  }
//}

public class Client extends AsyncTask<String, Void, Void>{
  private Socket socket = null;
  private DataOutputStream dataOutputStream = null;
  private DataInputStream dataInputStream = null;
  @Override
  protected Void doInBackground(String... voids) {
    String request = voids[1];

    try {
      this.socket = new Socket("192.168.31.118", 5555);
      Log.d("socket", "Connect to server success");
      this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
//      this.dataInputStream = new DataInputStream(this.socket.getInputStream());

      this.dataOutputStream.writeUTF(request);
      this.dataOutputStream.close();
      this.socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
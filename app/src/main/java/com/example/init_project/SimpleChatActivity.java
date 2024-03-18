package com.example.init_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayoutStates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleChatActivity extends AppCompatActivity {
  Button btn_send;
  EditText edit_text;
  LinearLayout layout_list;
  ScrollView scroll_view;
  ProgressBar progress_bar;
  ConstraintLayout constraint_disconnect;
  int count_ack_recv = 3;
  boolean is_ack = false;

  Timer timer_recv_ack;

  Socket socket;
  private DataOutputStream output;
  private DataInputStream input;

  Thread thread_init_socket = null;
  Thread thread_send_request = null;
  String content;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_chat);

    btn_send = (Button) findViewById(R.id.btn_send);
    edit_text = (EditText) findViewById(R.id.edit_text);
    layout_list = (LinearLayout) findViewById(R.id.layout_list);
    scroll_view = (ScrollView) findViewById(R.id.scroll_view);
    progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    constraint_disconnect = (ConstraintLayout) findViewById(R.id.constraint_disconnect);

    thread_init_socket = new Thread(new thread_init_socket());
    thread_init_socket.start();


    btn_send.setOnClickListener(v -> {
      content = edit_text.getText().toString();
      if(content.equals("")){
        return;
      }

      thread_send_request = new Thread(new thread_send_request());
      thread_send_request.start();
    });
  }

  class thread_init_socket implements Runnable{
    public void run(){
      try{
        socket = new Socket("192.168.31.118".trim(), 7777);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Log.d("socket", "Connected");
            constraint_disconnect.setVisibility(View.INVISIBLE);
          }

        });

        new Thread(new thread_recv_response()).start();
      }catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  class thread_send_request implements Runnable {
    public void run() {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          progress_bar.setVisibility(View.VISIBLE);
          disableEditText();
        }
      });

      send_request();
    }
  }

  private void send_request(){
    timer_recv_ack = new Timer();
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        try {
          output.writeUTF(content);
          Log.d("socket", "From client: " + content);
        } catch (IOException e) {
          e.printStackTrace();
        }

        count_ack_recv --;
        if(count_ack_recv <= 0){
          count_ack_recv = 3;

          timer_recv_ack.cancel();
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getApplicationContext(), "Cannot send message", Toast.LENGTH_LONG).show();

              progress_bar.setVisibility(View.INVISIBLE);
              enableEditText();
            }
          });

        }
      }
    };
    timer_recv_ack.scheduleAtFixedRate(timerTask, 0, 3000);
  }

  class thread_recv_response implements Runnable{
    @Override
    public void run() {
      while(true){
        try {
          String response = input.readUTF();
          if(response != null){

            if(response.equals("!ack#")){
              Log.d("socket", "Receive ACK");
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  add_layout_send();
                }
              });
              if(timer_recv_ack != null){
                timer_recv_ack.cancel();
              }
            }else{
              Log.d("socket", "From server: " + response);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  add_layout_recv(response);
                }
              });
            }
          }
        } catch (IOException e) {
          //e.printStackTrace();
          Log.d("socket", "disconnect");
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              constraint_disconnect.setVisibility(View.VISIBLE);
            }
          });
          thread_init_socket = new Thread(new thread_init_socket());
          thread_init_socket.start();
          return;
        }
      }
    }
  }

  public void add_layout_send(){
    //Clear text in edit_text
    edit_text.getText().clear();
    View send_message_layout = getLayoutInflater().inflate(R.layout.send_message_layout, null, false);
    TextView text_view = send_message_layout.findViewById(R.id.text_view);
    text_view.setText(content);

    // Add send_message_layout to list_view
    layout_list.addView(send_message_layout);

    scroll_view.fullScroll(View.FOCUS_DOWN);

    progress_bar.setVisibility(View.INVISIBLE);
    enableEditText();
  }

  public void add_layout_recv(String response){
    View recv_message_layout = getLayoutInflater().inflate(R.layout.recv_message_layout, null, false);
    TextView text_view = recv_message_layout.findViewById(R.id.text_view);
    text_view.setText(response);

    // Add send_message_layout to list_view
    layout_list.addView(recv_message_layout);

    scroll_view.fullScroll(View.FOCUS_DOWN);
  }

  private void disableEditText() {
    edit_text.setEnabled(false);
  }

  private void enableEditText() {
    edit_text.setEnabled(true);
  }

  private synchronized void set_ack(boolean value){
    is_ack = value;
  }
}
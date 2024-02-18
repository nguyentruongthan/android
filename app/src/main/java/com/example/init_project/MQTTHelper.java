package com.example.init_project;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTTHelper {
  final String USERNAME = "nguyentruongthan";
  final String PASSWORD = "aio_QYdM96s2CrBIeGCRzWpb7i679myO";
  final String serverUri = "tcp://io.adafruit.com:1883";

  final String[] SUBSCRIBE_TOPICS =
          {"nguyentruongthan/feeds/nutnhan1", "nguyentruongthan/feeds/nutnhan2"};

  public MqttAndroidClient client;

  public MQTTHelper(Context context){
    connect(context);

  }

  private void connect(Context context){
    String clientId = MqttClient.generateClientId();

    client = new MqttAndroidClient(context, serverUri, clientId);

    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(USERNAME);
    options.setPassword(PASSWORD.toCharArray());


    try {
      IMqttToken token = client.connect(options);
      token.setActionCallback(new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          // We are connected
          Log.d("mqtt", "onSuccess");
          subscribe();
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          // Something went wrong e.g. connection timeout or firewall problems
          Log.d("mqtt", "onFailure");

        }
      });
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  public void publish(String topic, String payload){
    byte[] encodedPayload = new byte[0];
    try {
      encodedPayload = payload.getBytes("UTF-8");
      MqttMessage message = new MqttMessage(encodedPayload);
      client.publish(topic, message);
    } catch (UnsupportedEncodingException | MqttException e) {
      e.printStackTrace();
    }
  }

  public void subscribe(){
    for(String topic: SUBSCRIBE_TOPICS){
      int qos = 1;
      try {
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
          @Override
          public void onSuccess(IMqttToken asyncActionToken) {
            // The message was published
            Log.d("mqtt", "Subscribe to " + topic + " success");
          }

          @Override
          public void onFailure(IMqttToken asyncActionToken,
                                Throwable exception) {
            // The subscription could not be performed, maybe the user was not
            // authorized to subscribe on the specified topic e.g. using wildcards
            Log.d("mqtt", "Subscribe " + topic + " failed");
          }
        });
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }
  }


}

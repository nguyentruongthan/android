package com.example.init_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends AppCompatActivity {
  String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
          "WebOS","Ubuntu","Windows7","Max OS X"};
  ListView listView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_view);

    listView = (ListView) findViewById(R.id.list_view);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            R.layout.simple_text, R.id.text_view, mobileArray);
    listView.setAdapter(adapter);
  }
}
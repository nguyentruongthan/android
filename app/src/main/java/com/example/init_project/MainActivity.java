package com.example.init_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  LinearLayout layout_list;
  Button button_add;

  List<String> team_list = new ArrayList<>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    layout_list = findViewById(R.id.layout_list);
    button_add = findViewById(R.id.button_add);

    button_add.setOnClickListener(this);

    team_list.add("Team");
    team_list.add("Vietnam");
    team_list.add("USA");
    team_list.add("Japan");

  }

  @Override
  public void onClick(View v) {
    add_view();
  }

  private void add_view(){
    View cricketer_view = getLayoutInflater().inflate(R.layout.row_add_cricketer, null, false);

    EditText edit_cricketer_name = (EditText)cricketer_view.findViewById(R.id.edit_cricketer_name);
    AppCompatSpinner spinner_team = (AppCompatSpinner)cricketer_view.findViewById(R.id.spinner_team);
    ImageView image_remove = (ImageView)cricketer_view.findViewById(R.id.image_remove);

    ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, team_list);
    spinner_team.setAdapter(arrayAdapter);

    image_remove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        remove_view(cricketer_view);
      }
    });
    layout_list.addView(cricketer_view);
  }

  private void remove_view(View view){
    layout_list.removeView(view);
  }
}

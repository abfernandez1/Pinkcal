package com.example.pinkcal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Add_event extends AppCompatActivity {

    private Button btn_close_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Button btn_close_event = findViewById(R.id.btn_close_event);
        btn_close_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setBtn_close_event(Button btn_close_event) {
        this.btn_close_event = btn_close_event;
    }
}

package edu.usm.cs.csc414.pocketfinances;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewNum = findViewById(R.id.textViewNum);

        textViewNum.setText(getText(R.string.hello_world_str));
    }

    public int add(int a, int b) {
        return a+b;
    }
}

package de.unipassau.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        double result = intent.getDoubleExtra(MainActivity.RESULT_MESSAGE, 0.0);

        TextView resultView = findViewById(R.id.resultView);
        resultView.setText("Result: " + result);
    }
}

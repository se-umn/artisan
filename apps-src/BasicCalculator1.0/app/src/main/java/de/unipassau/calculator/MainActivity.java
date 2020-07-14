package de.unipassau.calculator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class MainActivity extends Activity {
    public static final String RESULT_MESSAGE = "result";
    public static final String ERRONEOUS_INPUT = "13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendResult(View view) {
        if (view.getId() != R.id.calculateButton) {
            return;
        }

        Intent intent = new Intent(this, ResultActivity.class);
        EditText inputField = findViewById(R.id.input);
        String input = inputField.getText().toString();

        Number number = eval(input);
        double result = number.doubleValue();
        intent.putExtra(RESULT_MESSAGE, result);
        startActivity(intent);
    }

    private Number eval(String input) {
        if (input.isEmpty()) {
            return null;
        } else if (input.equals(ERRONEOUS_INPUT)) {
            throw new IllegalArgumentException("A simple exception");
        }

        Number result;
        try {
            Expression e = new ExpressionBuilder(input).build();
            result = e.evaluate();
        } catch (UnknownFunctionOrVariableException ufve) {
            throw new IllegalArgumentException("Unknown function or variable", ufve);
        }

        return result;
    }

}

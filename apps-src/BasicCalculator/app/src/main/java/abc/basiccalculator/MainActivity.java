package abc.basiccalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends Activity {

    public static final String RESULT_MESSAGE = "result";
    public static final String THROWN_ILLEGAL_ARGUMENT_EXCEPTION_INPUT = "13";
    public static final String NULL_POINTER_EXCEPTION_INPUT = "17";
    public static final String ERROR_STRING = "ERROR";
    public View nullView = null;

    public MainActivity(){
        System.out.println("Constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_main);
    }

    //onCreate
    //getSomething
    //sendResult
        //eval

    public void sendResult(View view) {
        if (view.getId() != R.id.calculateButton) {
            return;
        }

        Intent intent = new Intent(this, ResultActivity.class);
        EditText inputField = findViewById(R.id.input);

        String input = inputField.getText().toString();
        String result = eval(input);


        if (getSomeStrings().isEmpty()) {

        } else if (!result.equals(ERROR_STRING)) {
            intent.putExtra(RESULT_MESSAGE, Integer.parseInt(result));
            startActivity(intent);
        } else {
            inputField.setText(ERROR_STRING);
        }
    }

    public List<String> getSomeStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        return strings;
    }

    /**
     * This method was originally private, not it is public only to illustrate how carving can deal
     * with hidden methods (see #167)
     */
    public String eval(String input) {
        String result = "";
        if (input.isEmpty()) {
            return null;
        } else if (input.equals(THROWN_ILLEGAL_ARGUMENT_EXCEPTION_INPUT)) {
            Log.d("BasicCalculator", "thrown illegal argument exception");
            throw new IllegalArgumentException("A simple exception");
        } else if (input.equals(NULL_POINTER_EXCEPTION_INPUT)) {
            Log.d("BasicCalculator", "null pointer exception");
            this.nullView.getId();
        }
        Number opResult = null;
        try {
            Expression e = new ExpressionBuilder(input).build();
            opResult = e.evaluate();
            result = opResult.intValue() + "";
        } catch (IllegalArgumentException iae) {
            Log.d("BasicCalculator", "illegal argument exception");
        } finally {
            if (opResult == null) {
                result = ERROR_STRING;
            }
        }
        return result;
    }
}

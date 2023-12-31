package abc.basiccalculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.List;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class ResultActivity extends Activity {

    private TextView resultView;
    private Button incrementButtonByOne;
    private final String STRING_TO_CHECK_FOR_LOGGING = "5";
    private final String STRING_TO_CHECK_FOR_EXCEPTION = "23";
    private final int VALUE_TO_CHECK_FOR_EXCEPTION = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        int result = intent.getIntExtra(MainActivity.RESULT_MESSAGE, 0);

        resultView = findViewById(R.id.resultView);
        resultView.setText(result + "");
        if(result==2){
            Log.d("BasicCalculator", "Check coverage");
        }

        incrementButtonByOne = findViewById(R.id.incrementButtonByOne);
        /**
         * TODO The following code creates an anonym. class which access and set fields in the activity.
         */
        //buttonshadow1.setMockFor("<android.view.View: void setOnClickListener(android.view.View$OnClickListener)>", button7);
        incrementButtonByOne.setOnClickListener(view -> {
            /**
             * This code access a field of "the outer" object (resultView). But we do not track getFields here...
             * resultView is a android.widget.TextView
             */
            Expression e = new ExpressionBuilder(resultView.getText().toString() + "+1").build();
            Number eResult = e.evaluate();
            // TODO ALESSIO: Does this count as field setting?
            resultView.setText(eResult.intValue() + "");
            if (resultView.getText().toString().equals(STRING_TO_CHECK_FOR_LOGGING)) {
                logMessage("Called logged method");
            }
        });
        try {
            checkResult(resultView.getText().toString());
        } catch (Exception e) {
            Log.d("BasicCalculator", e.getMessage());
        }
    }

    public void incrementByTwo(View view) {
        Expression expr = new ExpressionBuilder(resultView.getText().toString() + "+2").build();
        Number exprResult = expr.evaluate();
        int result = exprResult.intValue();
        try {
            if (result == VALUE_TO_CHECK_FOR_EXCEPTION) {
                throw new Exception("Fake catched exception");
            }
        } catch (Exception e) {
            Log.d("BasicCalculator", e.getMessage());
        }
        resultView.setText(result + "");
    }

    public void checkResult(String result) throws Exception {
        if (result.equals(STRING_TO_CHECK_FOR_EXCEPTION)) {
            throw new Exception("Fake thrown exception");
        }
    }

    public void logMessage(String message) {
        try {
            Log.d("BasicCalculator", message);
        } catch (Exception e) {
            return;
        }
    }

}

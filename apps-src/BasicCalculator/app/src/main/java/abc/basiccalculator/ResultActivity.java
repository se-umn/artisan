package abc.basiccalculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ResultActivity extends Activity {

    private TextView resultView;
    private Button incrementButtonByOne;
    private final String STRING_TO_CHECK_FOR_LOGGING = "5.0";
    private final String STRING_TO_CHECK_FOR_EXCEPTION = "23.0";
    private final double VALUE_TO_CHECK_FOR_EXCEPTION = 8.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        double result = intent.getDoubleExtra(MainActivity.RESULT_MESSAGE, 0.0);

        resultView = findViewById(R.id.resultView);
        resultView.setText(result+"");

        incrementButtonByOne = findViewById(R.id.incrementButtonByOne);
        incrementButtonByOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expression e = new ExpressionBuilder(resultView.getText().toString()+"+1").build();
                resultView.setText(e.evaluate()+"");
                if(resultView.getText().toString().equals(STRING_TO_CHECK_FOR_LOGGING)){
                    logMessage("Called logged method");
                }
            }
        });
        try {
            checkResult(resultView.getText().toString());
        }
        catch(Exception e){
            Log.d("BasicCalculator", e.getMessage());
        }

    }

    public void incrementByTwo(View view) {
        Expression expr = new ExpressionBuilder(resultView.getText().toString()+"+2").build();
        double result = expr.evaluate();
        try {
            if (Double.compare(result, VALUE_TO_CHECK_FOR_EXCEPTION) == 0) {
                throw new Exception("Fake catched exception");
            }
        }
        catch(Exception e){
            Log.d("BasicCalculator", e.getMessage());
        }
        resultView.setText(result+"");
    }

    public void checkResult(String result) throws Exception {
        if(result.equals(STRING_TO_CHECK_FOR_EXCEPTION)){
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

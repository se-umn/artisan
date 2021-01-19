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
import java.util.Arrays;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExtendedMainActivity extends Activity {
    public static final String COMMENT_EXTRA = "Comment";
    public static final String RESULT_MESSAGE = "result";
    public static final String THROWN_ILLIGAL_ARGUMENT_EXCEPTION_INPUT = "13";
    public static final String NULL_POINTER_EXCEPTION_INPUT = "17";
    public static final String ERROR_STRING = "ERROR";
    public static final String FAILING_COMMENT = "fail";
    public View nullView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_extended);

        EditText commentField = findViewById(R.id.comment);
        commentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Button calculateBtn = findViewById(R.id.calculateButton);
                if (s.length() != 0) {
                    calculateBtn.setText(R.string.calculate_with_comment_text);
                } else {
                    calculateBtn.setText(R.string.calculate_text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void sendResult(View view) {
        if (view.getId() != R.id.calculateButton) {
            return;
        }

        Intent intent = new Intent(this, ExtendedResultActivity.class);
        EditText commentField = findViewById(R.id.comment);
        EditText inputField = findViewById(R.id.input);

        String input = inputField.getText().toString();
        Editable commentText = commentField.getText();
        if (commentText.length() != 0) {
            if (commentField.getText().toString().equals(FAILING_COMMENT)) {
                throw new IllegalArgumentException("Bad comment");
            } else {
                intent.putExtra(COMMENT_EXTRA, commentText.toString());
            }
        }

        String result = eval(input);

        if (!result.equals(ERROR_STRING)) {
            UiStorage.clear();
            UiStorage.addAllElements(createButtons());
            intent.putExtra(RESULT_MESSAGE, Integer.parseInt(result));
            startActivity(intent);
        } else {
            inputField.setText(ERROR_STRING);
        }
    }

    /**
     * This method was originally private, not it is public only to illustrate how carving can deal with hidden methods (see #167)
     */
    public String eval(String input) {
        String result = "";
        if (input.isEmpty()) {
            return null;
        } else if (input.equals(THROWN_ILLIGAL_ARGUMENT_EXCEPTION_INPUT)) {
            Log.d("BasicCalculator", "thrown illigal argument exception");
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
            Log.d("BasicCalculator", "illigal argument exception");
        } finally {
            if (opResult == null) {
                result = ERROR_STRING;
            }
        }
        return result;
    }

    /**
     * Creates two buttons. The first one is a back button that, when clicked,
     * changes the UI back to the MainActivity. The second one is a dummy button that, when clicked,
     * throws a NullPointerException.
     */
    private List<View> createButtons() {
        Button backBtn = new Button(this);
        backBtn.setText(R.string.back_text);
        backBtn.setOnClickListener(b -> {
            Intent intent = new Intent(getApplicationContext(), ExtendedMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Button crashBtn = new Button(this);
        crashBtn.setText(R.string.crash_text);
        crashBtn.setOnClickListener(b -> {
            throw new NullPointerException("Do not press!");
        });

        return Arrays.asList(backBtn, crashBtn);
    }
}

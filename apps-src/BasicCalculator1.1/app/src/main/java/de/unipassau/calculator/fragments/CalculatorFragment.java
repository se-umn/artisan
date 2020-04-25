package de.unipassau.calculator.fragments;

import static de.unipassau.calculator.math.Math.evaluateMatrixExpression;
import static de.unipassau.calculator.math.Math.evaluateSimpleExpression;
import static de.unipassau.calculator.math.Math.evaluateVectorExpression;
import static de.unipassau.calculator.math.Math.matrixPattern;
import static de.unipassau.calculator.math.Math.vectorPattern;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import de.unipassau.calculator.Calculation;
import de.unipassau.calculator.R;
import de.unipassau.calculator.viewmodel.CalculationViewModel;
import java.util.regex.Matcher;

public class CalculatorFragment extends Fragment implements XmlClickable {


  private CalculationViewModel model;
  private int calculationsDone;
  private EditText inputField;

  public CalculatorFragment() {
  }

  public static CalculatorFragment newInstance() {
    return new CalculatorFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_calculator, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    model = ViewModelProviders.of(requireActivity()).get(CalculationViewModel.class);
    inputField = view.findViewById(R.id.input);
    inputField.setRawInputType(InputType.TYPE_NULL);
    inputField.setFocusable(true);
    view.findViewById(R.id.buttonEquals).setOnClickListener(this::calculate);
  }

  public void print(View view) {

  }

  @RequiresApi(api = VERSION_CODES.O)
  private void calculate(View view) {
    calculationsDone++;

    String input = inputField.getText().toString();
    String result;

    Matcher matrixMatcher = matrixPattern.matcher(input);
    Matcher vectorMatcher = vectorPattern.matcher(input);
    if (matrixMatcher.find()) {
      result = evaluateMatrixExpression(input, matrixMatcher.group());
    } else if (vectorMatcher.find()) {
      result = evaluateVectorExpression(input, vectorMatcher.group());
    } else {
      result = evaluateSimpleExpression(input);
    }

    inputField.setText(result);
    inputField.setSelection(result.length());

    Calculation calculation = new Calculation(input, result);
    model.addCalculation(calculation);
  }

  @Override
  public void click(View view) {
    String text = (String) ((Button) view).getText();
    switch (text) {
      case "AC":
        clear();
        break;
      case "Erase":
        erase();
        break;
      default:
        inputField.append(text);
    }
  }

  private void erase() {
    Editable currentInput = inputField.getText();
    currentInput = currentInput.delete(currentInput.length() - 1, currentInput.length());
    inputField.setText(currentInput);
  }

  private void clear() {
    inputField.setText("");
  }
}

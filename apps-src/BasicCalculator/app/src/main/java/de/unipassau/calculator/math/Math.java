package de.unipassau.calculator.math;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import de.unipassau.calculator.App;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class Math {

  public static final DecimalFormat decimalFormatter = new DecimalFormat("0.#");
  public static final String VECTOR = "\\(([1-9][0-9]*,\\s*)*[1-9][0-9]*\\)";
  public static final Pattern vectorPattern = Pattern.compile(VECTOR);
  public static final Pattern matrixPattern = Pattern
      .compile("\\((" + VECTOR + ")(,\\s*" + VECTOR + ")*\\)");

  @RequiresApi(api = VERSION_CODES.O)
  public static String evaluateVectorExpression(String input, String vector) {
    input = input.trim();

    String[] items = input.split("\\s*\\*\\s*");
    double scalar;
    if (input.startsWith(vector)) {
      scalar = Double.parseDouble(items[1]);
    } else {
      scalar = Double.parseDouble(items[0]);
    }

    double[] result = evaluateScalarVectorMultiplication(scalar, parseVector(vector));
    return wrapBrackets(
        Arrays.stream(result).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
  }

  @RequiresApi(api = VERSION_CODES.N)
  public static double[] parseVector(String input) {
    input = stripBrackets(input).replaceAll("\\s+", "");

    String[] items = input.split(",");
    return Arrays.stream(items).mapToDouble(Double::parseDouble).toArray();
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static double[] evaluateScalarVectorMultiplication(double scalar, double[] vector) {
    return Arrays.stream(vector).map(v -> v * scalar).toArray();
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static String evaluateMatrixExpression(String input, String matrix) {
    input = input.trim();

    String[] items = input.split("\\s*\\*\\s*");
    double scalar;

    // EXCEPTION: double can be string and lead to parsing exception
    if (input.startsWith(matrix)) {
      scalar = Double.parseDouble(items[1]);
    } else {
      scalar = Double.parseDouble(items[0]);
    }

    return mergeMatrix(evaluateScalarMatrixMultiplication(scalar, parseMatrix(matrix)));
  }

  @RequiresApi(api = VERSION_CODES.O)
  private static String mergeMatrix(double[][] matrix) {

    // TODO potential exception place?
    String[] rows = new String[matrix.length];
    for (int r = 0; r < matrix.length; r++) {
      String row = Arrays.stream(matrix[r]).mapToObj(String::valueOf)
          .collect(Collectors.joining(", "));
      rows[r] = wrapBrackets(row);
    }
    String textMatrix = String.join(", ", rows);
    return wrapBrackets(textMatrix);
  }

  private static double[][] parseMatrix(String input) {
    input = input.replaceAll("\\s+", "");
    Matcher matcher = vectorPattern.matcher(input);
    List<String> rows = new ArrayList<>();

    while (matcher.find()) {
      String row = matcher.group();
      rows.add(row);
    }

    int rowSize = stripBrackets(rows.get(0)).split(",").length;
    double[][] matrix = new double[rows.size()][];
    for (int r = 0; r < rows.size(); r++) {
      String row = stripBrackets(rows.get(r));
      String[] items = row.split(",");
      matrix[r] = new double[rowSize];

      // EXCEPTION: unequal row size (second.length > first.length) leads to
      // ArrayIndexOutOfBoundsException on the second array level
      for (int c = 0; c < items.length; c++) {
        matrix[r][c] = Double.parseDouble(items[c]);
      }
    }

    return matrix;
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static double[][] evaluateScalarMatrixMultiplication(double scalar, double matrix[][]) {
    for (int r = 0; r < matrix.length; r++) {
      for (int c = 0; c < matrix.length; c++) {
        matrix[r][c] *= scalar;
      }
    }

    return matrix;
  }

  public static String stripBrackets(String vector) {
    return vector.substring(1, vector.length() - 1);
  }

  public static String wrapBrackets(String vector) {
    return "(" + vector + ")";
  }

  public static String evaluateSimpleExpression(String input) {
    if (input.isEmpty()) {
      return null;
    } else if (input.equals(String.valueOf(App.ERRONEOUS_INPUT))) {
      throw new IllegalArgumentException("A simple exception");
    }

    Number result;
    try {
      Expression e = new ExpressionBuilder(input).build();
      result = e.evaluate();
    } catch (UnknownFunctionOrVariableException ufve) {
      throw new IllegalArgumentException("Unknown function or variable", ufve);
    }

    return Math.decimalFormatter.format(result.doubleValue());
  }
}

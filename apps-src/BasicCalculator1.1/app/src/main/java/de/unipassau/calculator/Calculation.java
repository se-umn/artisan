package de.unipassau.calculator;

public class Calculation {
  private String expression;
  private String result;
  private boolean isSelected;

  public Calculation(String expression, String result) {
    this.expression = expression;
    this.result = result;
  }

  public String getExpression() {
    return expression;
  }

  public String getResult() {
    return result;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }
}

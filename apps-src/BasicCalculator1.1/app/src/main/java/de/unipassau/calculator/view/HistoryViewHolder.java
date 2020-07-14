package de.unipassau.calculator.view;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.unipassau.calculator.Calculation;
import de.unipassau.calculator.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
  private TextView expression;

  public HistoryViewHolder(@NonNull View itemView) {
    super(itemView);
    expression = itemView.findViewById(R.id.history_expression);
  }

  public void bind(Calculation calculation) {
    String text = calculation.getExpression() + "=" + calculation.getResult();
    expression.setText(text);

  }
 }

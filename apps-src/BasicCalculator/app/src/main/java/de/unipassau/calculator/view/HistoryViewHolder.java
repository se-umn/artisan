package de.unipassau.calculator.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.unipassau.calculator.Calculation;
import de.unipassau.calculator.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
  private TextView expression;
  private TextView result;
  private ImageView checkedMark;

  public HistoryViewHolder(@NonNull View itemView) {
    super(itemView);
    expression = itemView.findViewById(R.id.history_expression);
    result = itemView.findViewById(R.id.history_result);
    checkedMark = itemView.findViewById(R.id.checked_mark);
  }

  public void bind(Calculation calculation) {
    expression.setText(calculation.getExpression());
    result.setText(String.valueOf(calculation.getResult()));
    checkedMark.setVisibility(calculation.isSelected() ? View.VISIBLE : View.INVISIBLE);

    itemView.setOnClickListener(view -> {
      calculation.setSelected(!calculation.isSelected());
      checkedMark.setVisibility(calculation.isSelected() ? View.VISIBLE : View.INVISIBLE);
    });
  }
 }

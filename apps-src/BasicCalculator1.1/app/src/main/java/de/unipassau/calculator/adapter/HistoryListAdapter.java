package de.unipassau.calculator.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.unipassau.calculator.Calculation;
import de.unipassau.calculator.R;
import de.unipassau.calculator.view.HistoryViewHolder;
import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

  private List<Calculation> history;

  public HistoryListAdapter(List<Calculation> history) {
    this.history = history;
  }

  @NonNull
  @Override
  public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_history, parent, false);
    return new HistoryViewHolder(linearLayout);
  }

  @Override
  public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
    Calculation calculation = history.get(position);
    holder.bind(calculation);
  }

  @Override
  public int getItemCount() {
    return history.size();
  }

  public List<Calculation> getSelected() {
    List<Calculation> selectedCalculations = new ArrayList<>();
    for (Calculation calculation : history) {
      if (calculation.isSelected()) {
        selectedCalculations.add(calculation);
      }
    }
    return selectedCalculations;
  }
}

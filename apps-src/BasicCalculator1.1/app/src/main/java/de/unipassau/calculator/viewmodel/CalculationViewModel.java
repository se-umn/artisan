package de.unipassau.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.unipassau.calculator.Calculation;
import java.util.LinkedList;
import java.util.List;

public class CalculationViewModel extends ViewModel {
  private MutableLiveData<List<Calculation>> history;

  public CalculationViewModel() {
    history = new MutableLiveData<>();
    history.setValue(new LinkedList<>());
  }

  public LiveData<List<Calculation>> getHistory() {
    return history;
  }

  public void addCalculation(Calculation calculation) {
    // TODO check if list is null
    List<Calculation> calculations = history.getValue();
    calculations.add(calculation);

    if (calculations.size() > 5) {
      calculations.remove(0);
    }
    history.setValue(calculations);
  }

}

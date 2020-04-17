package de.unipassau.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.unipassau.calculator.Calculation;
import java.util.ArrayList;
import java.util.List;

public class CalculationViewModel extends ViewModel {
  private MutableLiveData<List<Calculation>> history;

  public CalculationViewModel() {
    history = new MutableLiveData<>();
    history.setValue(new ArrayList<>());
  }

  public LiveData<List<Calculation>> getHistory() {
    return history;
  }

  public void addCalculation(Calculation calculation) {
    // TODO check if list is null
    history.getValue().add(calculation);
  }

}

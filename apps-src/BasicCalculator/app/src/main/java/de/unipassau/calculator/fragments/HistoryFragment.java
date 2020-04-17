package de.unipassau.calculator.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.unipassau.calculator.R;
import de.unipassau.calculator.adapter.HistoryListAdapter;
import de.unipassau.calculator.viewmodel.CalculationViewModel;

public class HistoryFragment extends Fragment {

  private CalculationViewModel model;
  private HistoryListAdapter historyListAdapter;
  private ClipboardManager clipboardManager;

  public HistoryFragment() {

  }

  public static HistoryFragment newInstance() {
    HistoryFragment historyFragment = new HistoryFragment();
    return historyFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    clipboardManager = getSystemService(getContext(), ClipboardManager.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_history, container, false);

    // TODO fab does not show up for some reason
    FloatingActionButton saveFab = view.findViewById(R.id.saveFab);
    saveFab.setOnClickListener(
        view1 -> {});

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity().setTitle(R.string.history_menu);
    RecyclerView recyclerView = view.findViewById(R.id.history_view);

    model = ViewModelProviders.of(requireActivity()).get(CalculationViewModel.class);
    model.getHistory().observe(getViewLifecycleOwner(), history -> {
          historyListAdapter = new HistoryListAdapter(history);
          recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          recyclerView.setAdapter(historyListAdapter);
        }
    );
  }




}

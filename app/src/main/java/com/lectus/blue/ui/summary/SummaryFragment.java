package com.lectus.blue.ui.summary;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.databinding.FragmentSummaryBinding;
import com.lectus.blue.ui.products.ProductsFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class SummaryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                ((MainActivity) getActivity()).loadFragment(new ProductsFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentSummaryBinding binding = FragmentSummaryBinding.bind(view);

        MaterialToolbar toolbar = view.findViewById(R.id.summary_toolbar);
        toolbar.inflateMenu(R.menu.menu_summary_appbar);
        toolbar.setTitle("Summary");
        toolbar.setTitleTextColor(getResources().getColor(R.color.backgroundColor));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.share) {
                Toast.makeText(getContext(), "Share clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        binding.newOrderButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new ProductsFragment());
        });

    }
}
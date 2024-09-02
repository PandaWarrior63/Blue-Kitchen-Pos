package com.lectus.blue.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.lectus.blue.R;
import com.lectus.blue.adapter.TableAdapter;
import com.lectus.blue.model.TableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListFragment} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment implements TableAdapter.OnCardClickListener {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        tabLayout = view.findViewById(R.id.scrollable_tab_layout);
        addTab("Ground Floor");
        addTab("First Floor");
        addTab("Take Away");
        addTab("Delivery");

        // Set TabSelectedListener to handle click events
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                // Handle tab selected event
                Toast.makeText(requireContext(), "Selected: " + tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                // Handle tab unselected event
                // (This method is optional to implement)
            }

            @Override
            public void onTabReselected(@NonNull TabLayout.Tab tab) {
                // Handle tab reselected event
                Toast.makeText(requireContext(), "Reselected: " + tab.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        // Set up the RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 2 columns
        tableAdapter = new TableAdapter(getCardItems(),this);
        recyclerView.setAdapter(tableAdapter);

        return view;
    }
    private void addTab(String tabTitle) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(tabTitle);
        tabLayout.addTab(tab);
    }
    private List<TableItem> getCardItems() {
        List<TableItem> cardItems = new ArrayList<>();
        cardItems.add(new TableItem("Card 1", "Description 1"));
        cardItems.add(new TableItem("Card 2", "Description 2"));
        cardItems.add(new TableItem("Card 3", "Description 3"));
        cardItems.add(new TableItem("Card 4", "Description 4"));
        cardItems.add(new TableItem("Card 4", "Description 4"));
        cardItems.add(new TableItem("Card 4", "Description 4"));
        cardItems.add(new TableItem("Card 4", "Description 4"));
        cardItems.add(new TableItem("Card 4", "Description 4"));
        // Add more cards here
        return cardItems;
    }

    @Override
    public void onCardClick(int position, TableItem cardItem) {
        Toast.makeText(requireContext(), cardItem.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
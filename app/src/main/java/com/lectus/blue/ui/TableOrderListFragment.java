package com.lectus.blue.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.model.TableItem;
import com.lectus.blue.model.TableOrderItem;
import com.lectus.blue.ui.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class TableOrderListFragment extends Fragment implements  TableOrderListAdapter.OnRemoveButtonClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TableItem tableItem;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TableOrderListFragment() {
    }

    public TableOrderListFragment(TableItem cardItem) {
        tableItem = cardItem;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TableOrderListFragment newInstance(int columnCount) {
        TableOrderListFragment fragment = new TableOrderListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_order_item_list, container, false);

        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.table_order_recycle_view);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new TableOrderListAdapter(PlaceholderContent.ITEMS));
            recyclerView.setAdapter(new TableOrderListAdapter(tableItem.getOrderList(),this));
            Button backButton = view.findViewById(R.id.back_button);
            backButton.setOnClickListener(v->{
                getActivity().getSupportFragmentManager().popBackStack();
            });
        //}
        return view;
    }

    @Override
    public void onRemoveButtonClick(int position, TableOrderItem orderItem) {
        Toast.makeText(requireContext(), orderItem.getItem_code(), Toast.LENGTH_SHORT).show();
        RemoveItemFragment removeDialogFragment = new RemoveItemFragment(orderItem);
        removeDialogFragment.show(getChildFragmentManager(),"CustomDialog");
    }
}
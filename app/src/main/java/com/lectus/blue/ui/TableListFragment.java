package com.lectus.blue.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.lectus.blue.App;
import com.lectus.blue.Config.URL;
import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.model.FloorItem;
import com.lectus.blue.ui.create_product.CreateProductFragment;
import com.lectus.blue.ui.login.LoginFragment;
import com.lectus.blue.utils.SessionManager;
import com.lectus.blue.utils.TableAdapter;
import com.lectus.blue.model.TableItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListFragment} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment implements TableAdapter.OnCardClickListener {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private SessionManager session;
    String tag_string_init = "req_init";
    private TabLayout tabLayout;
    private static final String TAG = LoginFragment.class.getSimpleName();
    List<FloorItem> floorList = new ArrayList<>();
    List<TableItem> tableList = new ArrayList<>();

    private Handler handler = new Handler();
    private Runnable runnable;
    private static final int INTERVAL = 10000; // 10 seconds
    private boolean isSync  = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        session = new SessionManager(requireContext());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 2 columns
        tableAdapter = new TableAdapter(getCardItems(),this);
        recyclerView.setAdapter(tableAdapter);


        tabLayout = view.findViewById(R.id.scrollable_tab_layout);


        // Set TabSelectedListener to handle click events
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                // Handle tab selected event
                //Toast.makeText(requireContext(), "Selected: " + tab.getText(), Toast.LENGTH_SHORT).show();
                displayTables();
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

        runnable = new Runnable() {
            @Override
            public void run() {
                // Your Volley request here
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        URL.URL_RESTAURANT_TABLE_DATA, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Tables Response: " + response.toString());
                        isSync = false;

                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject dataObj = jObj.getJSONObject("message");
                            JSONArray tablesArray = dataObj.getJSONArray("tables");
                            setLiveTables(tablesArray);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        //hideDialog();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(requireContext(),
                                error.getMessage()==null?"NULL": error.getMessage(), Toast.LENGTH_LONG).show();
                        isSync = false;
                        //hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("tag", "login");
                        params.put("pos_opening_entry", session.getKeyOpenPos());

                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        //..add other headers
                        params.put("Accept", "'application/json");
                        //params.put("Content-Type", "application/x-www-form-urlencoded");
                        params.put("Authorization", "Bearer " + session.getKeyToken());
                        return params;
                    }
                };

                // Adding request to request queue
                if (!isSync)
                    App.getInstance().addToRequestQueue(strReq, tag_string_init);
                isSync = true;
                // Schedule the next execution
                handler.postDelayed(this, INTERVAL);
            }
        };

        // Start the periodic execution
        handler.post(runnable);

        if (floorList.size()==0)
            getRestaurantInitData();
        else
            setupFloors();
        return view;
    }
    private void setupFloors(){
        for(FloorItem item:floorList){
            addTab(item.getFloor_name());
        }
        displayTables();
    }
    private void displayTables(){
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        String floorName = tab.getText().toString();
        tableAdapter.resetTableList();
        for(TableItem item:tableList){
            if (item.getFloor().equals(floorName))
                tableAdapter.addTable(item);
        }
    }
    private void setLiveTables(JSONArray tableJsonArry){
        try {
            for(int i=0;i<tableJsonArry.length();i++){
                JSONObject tableObj = tableJsonArry.getJSONObject(i);
                for (TableItem table:tableList) {
                    if (table.getTable_name().equals(tableObj.getString("table_name")))
                    {
                        JSONArray orders = tableObj.getJSONArray("orders");
                        if (orders.length()!=0) {
                            JSONArray orderItems = orders.getJSONObject(0).getJSONArray("items");
                            table.setBackground("#FF6666");
                            String temp = String.valueOf(orderItems.length());
                            table.setDescription(temp+" Order Items");
                            table.setOrderList(orderItems);
                        }
                        else {
                            table.setOrderList(new ArrayList<>());
                            table.setDescription("");
                            table.setBackground("#DCDCDC");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tableAdapter.notifyDataSetChanged();
    }
    private void getRestaurantInitData(){
        Toast.makeText(requireContext(),session.getKeyToken(),Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL.URL_RESTAURANT_INIT_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONObject dataObj = jObj.getJSONObject("message");
                    JSONArray jsonArray = dataObj.getJSONArray("floors");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        FloorItem floorItem = new FloorItem(jsonArray.getJSONObject(i));
                        floorList.add(floorItem);
                    }
                    jsonArray = dataObj.getJSONArray("tables");
                    for(int i=0;i<jsonArray.length();i++){
                        TableItem tableItem = new TableItem(jsonArray.getJSONObject(i));
                        tableList.add(tableItem);
                    }
                    tableList.sort(Comparator.comparing(TableItem::getTable_number));
                    setupFloors();
                    //Toast.makeText(requireContext(), temp, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(requireContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(requireContext(),
                        error.getMessage()==null?"NULL": error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //..add other headers
                params.put("Accept", "'application/json");
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + session.getKeyToken());
                return params;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_init);

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
        //Toast.makeText(requireContext(), cardItem.getName(), Toast.LENGTH_SHORT).show();
        ((MainActivity) requireActivity()).loadFragment(new TableOrderListFragment(cardItem));
    }
}
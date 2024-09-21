package com.lectus.blue.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.lectus.blue.App;
import com.lectus.blue.Config.URL;
import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.model.TableOrderItem;
import com.lectus.blue.ui.login.LoginFragment;
import com.lectus.blue.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveItemFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = RemoveItemFragment.class.getSimpleName();

    private SessionManager session;
    private ProgressDialog pDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableOrderItem mOrderItem;
    public RemoveItemFragment() {
        // Required empty public constructor
    }

    public RemoveItemFragment(TableOrderItem orderItem) {
        mOrderItem = orderItem;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoveItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoveItemFragment newInstance(String param1, String param2) {
        RemoveItemFragment fragment = new RemoveItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        session = new SessionManager(requireContext());
        pDialog = new ProgressDialog(requireContext());
        pDialog.setCancelable(false);

        View view = inflater.inflate(R.layout.fragment_remove_item, container, false);
        TextView itemView = view.findViewById(R.id.itemTextView);
        TextView quantityView = view.findViewById(R.id.quantityTextView);
        EditText removeQuantityEdit= view.findViewById(R.id.removeQuantityEditText);

        itemView.setText(mOrderItem.getItem_code());
        quantityView.setText(mOrderItem.getQuantity());
        removeQuantityEdit.setText("");

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v->{
            this.dismiss();
        });

        Button allButton = view.findViewById(R.id.allButton);
        allButton.setOnClickListener(v->{
            removeQuantityEdit.setText(mOrderItem.getQuantity());
        });

        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v->{
            float removeQuantityVal = 0;
            try{
                String tmp = removeQuantityEdit.getText().toString();
                if(tmp.isEmpty())
                {
                    Toast.makeText(requireContext(), "Please input quantity to remove", Toast.LENGTH_SHORT).show();
                    return;
                }
                removeQuantityVal = Float.parseFloat(tmp);
                if(removeQuantityVal==0)
                {
                    Toast.makeText(requireContext(), "Please input correct number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (removeQuantityVal > Float.parseFloat(mOrderItem.getQuantity()) )
                {
                    Toast.makeText(requireContext(), "Quantity to remove must be smaller thank ordered quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }catch (Exception e)
            {
                Toast.makeText(requireContext(), "Please input correct number", Toast.LENGTH_SHORT).show();
                return;
            }
            updateQuantity(removeQuantityVal);
        });

        return view;
    }

    private void updateQuantity(float removeQuantityVal) {
        String tag_string_req = "update_quantity";

        pDialog.setMessage("Updating Quantity ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL.URL_RESTAURANT_UPDATE_QUANTITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    //JSONObject resultObj = jObj.getJSONObject("message");

                    dismiss();
                    getActivity().getSupportFragmentManager().popBackStack();
                    //((MainActivity) requireActivity()).loadFragment(new TableListFragment());
                } catch (JSONException e) {
                    // JSON error
                    dismiss();

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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("tag", "login");
                params.put("item_code", mOrderItem.getItem_code());
                params.put("order_no", mOrderItem.getParent());
                params.put("waiter", "Kitchen");
                params.put("supervisor", "Kitchen");
                float quantity = Float.parseFloat(mOrderItem.getQuantity())-removeQuantityVal;
                params.put("quantity", String.valueOf(quantity));
                params.put("remove_quantity", String.valueOf(removeQuantityVal));
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
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Disable cancellation of the dialog
        setCancelable(false);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
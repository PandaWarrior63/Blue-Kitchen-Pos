package com.lectus.blue.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.lectus.blue.App;
import com.lectus.blue.Config.URL;
import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.ui.login.LoginFragment;
import com.lectus.blue.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosSelectionFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog pDialog;
    private SessionManager session;
    private static final String TAG = LoginFragment.class.getSimpleName();

    public PosSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PosSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PosSelectionFragment newInstance(String param1, String param2) {
        PosSelectionFragment fragment = new PosSelectionFragment();
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
    private void addChip(LayoutInflater inflater, ChipGroup chipGroup,String entry)
    {
        Chip chip = (Chip) inflater.from(requireContext()).inflate(R.layout.chip_item, chipGroup, false);
        chip.setText(entry);
        chipGroup.addView(chip);
        chip.setOnClickListener(v->{
            session.setKeyOpenPos(entry);
            dismiss();
            ((MainActivity) requireActivity()).loadFragment(new TableListFragment());
        });
    }
    private void loadPosOpenEntries(View view,LayoutInflater inflater){

        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        String tag_string_req = "req_pos_open_entyr";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL.URL_COMMON_OPEN_ENTRIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Entry Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONObject tokenObj = jObj.getJSONObject("message");
                    JSONArray entryArray = tokenObj.getJSONArray("open_entries");
                    for(int i=0;i<entryArray.length();i++) {
                        addChip(inflater,chipGroup,entryArray.getJSONObject(i).getString("name"));
                    }
                    //Log.e("Open Entries",tokenObj.toString());
                    //((MainActivity) requireActivity()).loadFragment(new TableListFragment());
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
                Log.e(TAG, "Entry error: " + error.getMessage());
                Toast.makeText(requireContext(),
                        error.getMessage()==null?"Error occurs in loading Pos Open Entries. ": error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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
        App.getInstance().addToRequestQueue(strReq, tag_string_req);








    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pos_selection, container, false);
        pDialog = new ProgressDialog(requireContext());
        pDialog.setCancelable(false);
        //Set Login Part
        session = new SessionManager(requireContext());

        loadPosOpenEntries(view,inflater);
        return view;
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
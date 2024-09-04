package com.lectus.blue.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.lectus.blue.Config.URL;
import com.lectus.blue.MainActivity;
import com.lectus.blue.R;
import com.lectus.blue.ui.TableListFragment;
import com.lectus.blue.ui.products.ProductsFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.lectus.blue.App;
import com.lectus.blue.utils.SessionManager;

/**
 * Fragment representing the login screen for Shrine.
 */
public class LoginFragment extends Fragment {
    private ProgressDialog pDialog;
    private SessionManager session;
    private static final String TAG = LoginFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputLayout usernameTextInput = view.findViewById(R.id.username_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password);
        final TextInputEditText usernameEditText = view.findViewById(R.id.username);
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        pDialog = new ProgressDialog(requireContext());
        pDialog.setCancelable(false);

        //Set Login Part
        session = new SessionManager(requireContext());

        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.trim().length()==0)
                {
                    usernameTextInput.setError(getString(R.string.blue_error_username));
                }else {
                    usernameTextInput.setError(null); // Clear the error
                    if (!isPasswordValid(passwordEditText.getText())) {
                        passwordTextInput.setError(getString(R.string.blue_error_password));
                    } else {
                        passwordTextInput.setError(null); // Clear the error
                        //((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the next Fragment
                        checkLogin(username,password);
                    }
                }
            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        return view;
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 4;
    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                URL.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONObject tokenObj = jObj.getJSONObject("message");
                    String token = tokenObj.getString("token");
                    Toast.makeText(requireContext(), "You successfully logged in", Toast.LENGTH_SHORT).show();
                    session.setKeyToken(token);
                    ((MainActivity) requireActivity()).loadFragment(new TableListFragment());
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //..add other headers
                params.put("Accept", "'application/json");
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Authorization", "Bearer " + access_token);
                return params;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
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


package com.lectus.blue;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lectus.blue.ui.TableListFragment;
import com.lectus.blue.ui.create_product.CreateProductFragment;
import com.lectus.blue.ui.login.LoginFragment;
import com.lectus.blue.ui.products.ProductsFragment;
import com.google.android.material.navigation.NavigationView;
import com.lectus.blue.utils.SessionManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        session = new SessionManager(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
        else {
            loadFragment(new TableListFragment());
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(); // Pop the fragment from the back stack
                } else {
                    finish(); // Otherwise, finish the activity
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
//        // Load the default fragment
//        if (savedInstanceState == null) {
//            loadFragment(new ProductsFragment());
//        }
    }

    public void setActiveDrawerItem(Fragment fragment) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        if (fragment instanceof ProductsFragment) {
            navigationView.setCheckedItem(R.id.drawer_products);
        } else if (fragment instanceof CreateProductFragment) {
            navigationView.setCheckedItem(R.id.drawer_create_product);
        }

    }

    public void loadFragment(Fragment fragment) {
        // Load the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Add this transaction to the back stac
        fragmentTransaction.commit();

        // Set the active drawer item
        setActiveDrawerItem(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.drawer_products) {
            fragment = new ProductsFragment();
        } else if (item.getItemId() == R.id.drawer_create_product) {
            fragment = new CreateProductFragment();
        }

        if (fragment != null) {
            loadFragment(fragment);
        }

        return true;
    }

    public void openDrawer() {
        //drawerLayout.openDrawer(GravityCompat.START);
    }
}
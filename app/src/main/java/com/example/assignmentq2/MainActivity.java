package com.example.assignmentq2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if(savedInstanceState == null){
            loadFragment(new RestaurantFragment());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        Fragment fragment = null;

                        if(menuItem.getItemId() == R.id.nav_restaurant){
                            fragment = new RestaurantFragment();
                        } else if (menuItem.getItemId() == R.id.nav_dish_rating) {
                            fragment = new RatingFragment();
                        }

                        return loadFragment(fragment);
                    }
                }
        );

    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragmentContainer, fragment);

            transaction.commit();

            return true;
        }
        return false;
    }
}
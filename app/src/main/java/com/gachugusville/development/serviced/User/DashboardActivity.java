package com.gachugusville.development.serviced.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gachugusville.development.serviced.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.gachugusville.development.serviced.R.drawable.bg_bottom_nav;

public class DashboardActivity extends AppCompatActivity {

    Fragment fragment = null;
    BottomNavigationView bottom_nav;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_dashboard);

        bottom_nav = findViewById(R.id.bottom_nav);
        findViewById(R.id.container);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        bottom_nav.setBackground(getResources().getDrawable(bg_bottom_nav));


        bottom_nav.setOnNavigationItemSelectedListener(item -> {

            final int home_bottom_nav = R.id.home_bottom_nav;
            final int category_bottom_nav = R.id.category_bottom_nav;
            final int message_bottom_nav = R.id.message_bottom_nav;
            final int account_bottom_nav = R.id.account_bottom_nav;

            switch (item.getItemId()) {

                case home_bottom_nav:
                    fragment = new HomeFragment();
                    break;
                case category_bottom_nav:
                    fragment = new CategoriesFragment();
                    break;
                case message_bottom_nav:
                    fragment = new MessagesFragment();
                    break;
                case account_bottom_nav:
                    fragment = new AccountFragment();
                    break;

            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        replace(R.id.container, fragment)
                        .commit();
            }

            return true;
        });

    }



}
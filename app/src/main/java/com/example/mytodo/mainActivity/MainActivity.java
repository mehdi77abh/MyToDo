package com.example.mytodo.mainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.mytodo.R;
import com.example.mytodo.databinding.ActivityMainBinding;
import com.example.mytodo.myApp.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;
    private NavController navController;
    private  AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MainViewModel mainViewModel = new ViewModelProvider(this
                ,new ViewModelFactory(this)).get(MainViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
         appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.calenderFragment, R.id.groupListFragment, R.id.historyListFragment)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration);


    }
}
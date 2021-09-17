package com.example.mytodo.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.mytodo.R;
import com.example.mytodo.databinding.ActivityMainBinding;
import com.example.mytodo.other.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MainViewModel mainViewModel = new ViewModelProvider(this
                ,new ViewModelFactory(this)).get(MainViewModel.class);





    }


}
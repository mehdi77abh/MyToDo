package com.example.mytodo.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.mytodo.R;
import com.example.mytodo.main.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity  {
    private TabLayout tabLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(),getLifecycle());

        viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager
                , (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("لیست کارها");
                            break;

                        case 1:
                            tab.setText("کار های انجام شده");
                            break;


                    }
                });
        tabLayoutMediator.attach();



    }


}
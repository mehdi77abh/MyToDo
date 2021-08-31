package com.example.mytodo;


import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Task;

public class ListTouchHelper {
    private static ItemTouchHelper touchHelper;


    public static ItemTouchHelper getTouchHelper(EventListener eventListener) {

            touchHelper = new ItemTouchHelper(new ItemTouchHelper
                    .SimpleCallback(0,ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    eventListener.onSwipeListener(viewHolder.getAdapterPosition());
                }
            });

        return touchHelper;
    }
    public interface EventListener{
        void onSwipeListener(int pos);
    }
}

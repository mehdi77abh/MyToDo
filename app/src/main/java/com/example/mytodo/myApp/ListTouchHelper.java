package com.example.mytodo.myApp;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.R;

public class ListTouchHelper {
    private static ItemTouchHelper touchHelper;
    private static ColorDrawable background = null;
    public static ItemTouchHelper getTouchHelper(Context context, EventListener eventListener) {
            background = new ColorDrawable(ContextCompat.getColor(context, R.color.blue));
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

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    View itemView = viewHolder.itemView;
                    Drawable icon = ContextCompat.getDrawable(context,
                            R.drawable.ic_delete_24dp);
                    int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();

                    int backgroundCornerOffset = 20;
                    if (dX > 0) { // Swiping to the right
                        int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                        int iconRight = itemView.getLeft() + iconMargin;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(itemView.getLeft(), itemView.getTop(),
                                itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                                itemView.getBottom());
                    } else if (dX < 0) { // Swiping to the left
                        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    } else { // view is unSwiped
                        background.setBounds(0, 0, 0, 0);
                    }

                    background.draw(c);
                    icon.draw(c);

                }
            });

        return touchHelper;
    }
    public interface EventListener{
        void onSwipeListener(int pos);
    }
}

package com.example.mytodo.MainFragment;

import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_LOW;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.AddTaskDialog.AddTaskDialogFragment;
import com.example.mytodo.Database.Task;
import com.example.mytodo.R;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MyViewHolder>{
    private List<Task> notDoneTasks;
    private EventListener eventListener ;
    public MainListAdapter(List<Task> notDoneTasks,EventListener eventListener) {
        this.notDoneTasks = notDoneTasks;
        this.eventListener =eventListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bindView(notDoneTasks.get(i));
    }


    @Override
    public int getItemCount() {
        return notDoneTasks.size();
    }
    public Task getTask(int pos){
        return notDoneTasks.get(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_time, txt_date, txt_title;
        private View importanceView;
        private View container;
        private ImageView img_check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_title = itemView.findViewById(R.id.txt_title);
            importanceView = itemView.findViewById(R.id.importanceView);
            container = itemView.findViewById(R.id.item_list_container);
            img_check = itemView.findViewById(R.id.img_check);

        }


        private void bindView(Task task) {
            txt_title.setText(task.getTitle());
            txt_time.setText(task.getTime());
            txt_date.setText(task.getDate());
            img_check.setImageResource(R.drawable.shape_checkbox_default);
            switch (task.getImportance()) {
                case IMPORTANCE_HIGH:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_high_rect);
                    break;
                case IMPORTANCE_NORMAL:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);

                    break;
                case IMPORTANCE_LOW:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_low_rect);

                    break;
                default:
                    importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);


            }

            img_check.setOnClickListener(v -> {
                //img_check.setImageResource(R.drawable.shape_checkbox_check);
                img_check.setBackgroundResource(R.drawable.shape_checkbox_check);
                img_check.setImageResource(R.drawable.ic_baseline_check_24_white);
                eventListener.ImgClickListener(task);

            });
            itemView.setOnClickListener(v ->{
                eventListener.ItemClickListener(task);

            } );


        }

    }
    public interface EventListener{
        void ItemClickListener(Task task);
        void ImgClickListener(Task task);
    }


}

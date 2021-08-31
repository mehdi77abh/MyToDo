package com.example.mytodo.HistoryListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_LOW;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Task;
import com.example.mytodo.AddTaskDialog.AddTaskDialogFragment;
import com.example.mytodo.R;

import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {
    private List<Task> doneTasks;
    private DoneListEventListener eventListener ;

    public HistoryListAdapter(List<Task> doneTasks, DoneListEventListener eventListener) {
        this.doneTasks = doneTasks;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindView(doneTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return doneTasks.size();
    }
    public Task getTask(int pos){
        return doneTasks.get(pos);
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
            img_check.setBackgroundResource(R.drawable.shape_checkbox_check);
            img_check.setImageResource(R.drawable.ic_baseline_check_24_white);

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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onItemClickListener(task);
                }
            });
            img_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onImgClickListener(task);
                    img_check.setImageResource(R.drawable.shape_checkbox_default);
                    img_check.setBackgroundResource(0);

                }
            });

        }
    }
    public interface DoneListEventListener{
        void onImgClickListener(Task selectedTask);
        void onItemClickListener(Task selectedTask);
    }
}

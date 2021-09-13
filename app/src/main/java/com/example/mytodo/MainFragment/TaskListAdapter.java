package com.example.mytodo.MainFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.mytodo.other.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.other.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.other.Const.IMPORTANCE_LOW;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Task;
import com.example.mytodo.R;

import java.util.Calendar;
import java.util.List;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>{
    private List<Task> tasks;
    private Boolean showInMain;
    private EventListener eventListener ;
    public TaskListAdapter(List<Task> notDoneTasks, Boolean showInMain, EventListener eventListener) {
        this.tasks = notDoneTasks;
        this.showInMain = showInMain;
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
        myViewHolder.bindView(tasks.get(i));
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public Task getTask(int pos){
        return tasks.get(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTv, dateTv, monthTv , titleTv , desTv ,timeTv;
        private View importanceView;
        private ImageView img_check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.dayTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            desTv = itemView.findViewById(R.id.descriptionTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            importanceView = itemView.findViewById(R.id.importanceView);
            img_check = itemView.findViewById(R.id.img_check);

        }


        private void bindView(Task task) {
            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(task.getDateLong());
            PersianDateImpl persianDate =new PersianDateImpl();
            persianDate.setDate(task.getDateLong());

            titleTv.setText(task.getTitle());
            desTv.setText(task.getDescription());
            timeTv.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

            dayTv.setText(persianDate.getPersianDayOfWeekName());
            Log.i("bindView", "bindView: "+calendar.getTime().toString());
            Log.i("bindView", "bindView: "+persianDate.getPersianLongDate());
            String number =PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianDay()));
            dateTv.setText(number);
            monthTv.setText(persianDate.getPersianMonthName());

            if (showInMain)
            img_check.setImageResource(R.drawable.shape_checkbox_default);
            else {
                img_check.setBackgroundResource(R.drawable.shape_checkbox_check);
                img_check.setImageResource(R.drawable.ic_baseline_check_24_white);
            }


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
                if (showInMain){
                    img_check.setBackgroundResource(R.drawable.shape_checkbox_check);
                    img_check.setImageResource(R.drawable.ic_baseline_check_24_white);

                }else {
                    img_check.setImageResource(R.drawable.shape_checkbox_default);
                    img_check.setBackgroundResource(0);
                }
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

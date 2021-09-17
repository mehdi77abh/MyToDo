package com.example.mytodo.MainFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.mytodo.other.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.other.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.other.Const.IMPORTANCE_LOW;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.databinding.ItemTaskBinding;

import java.util.Calendar;
import java.util.List;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>{
    private List<Task> tasks;
    private Boolean showInMain;
    private EventListener eventListener ;
    private ItemTaskBinding binding;

    public TaskListAdapter(List<Task> notDoneTasks, Boolean showInMain, EventListener eventListener) {
        this.tasks = notDoneTasks;
        this.showInMain = showInMain;
        this.eventListener =eventListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);
        View view = binding.getRoot();
        return new MyViewHolder(view);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        private void bindView(Task task) {
            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(task.getDateLong());
            PersianDateImpl persianDate =new PersianDateImpl();
            persianDate.setDate(task.getDateLong());

            binding.titleTv.setText(task.getTitle());
            binding.descriptionTv.setText(task.getDescription());
            binding.timeTv.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

            binding.dayTv.setText(persianDate.getPersianDayOfWeekName());
            String number =PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianDay()));
            binding.dateTv.setText(number);
            binding.monthTv.setText(persianDate.getPersianMonthName());

            if (showInMain)
            binding.imgCheck.setImageResource(R.drawable.shape_checkbox_default);
            else {
                binding.imgCheck.setBackgroundResource(R.drawable.shape_checkbox_check);
                binding.imgCheck.setImageResource(R.drawable.ic_baseline_check_24_white);
            }


            switch (task.getImportance()) {
                case IMPORTANCE_HIGH:
                    binding.importanceView.setBackgroundResource(R.drawable.shape_importance_high_rect);
                    break;
                case IMPORTANCE_NORMAL:
                    binding.importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);

                    break;
                case IMPORTANCE_LOW:
                    binding.importanceView.setBackgroundResource(R.drawable.shape_importance_low_rect);

                    break;
                default:
                    binding.importanceView.setBackgroundResource(R.drawable.shape_importance_normal_rect);


            }

            binding.imgCheck.setOnClickListener(v -> {
                if (showInMain){
                    binding.imgCheck.setBackgroundResource(R.drawable.shape_checkbox_check);
                    binding.imgCheck.setImageResource(R.drawable.ic_baseline_check_24_white);

                }else {

                    binding.imgCheck.setImageResource(R.drawable.shape_checkbox_default);
                    binding.imgCheck.setBackgroundResource(0);
                }
                    eventListener.ImgClickListener(task);

            });
            itemView.setOnLongClickListener(v ->{
                eventListener.ItemClickListener(task);

                return false;
            } );


        }

    }
    public interface EventListener{
        void ItemClickListener(Task task);
        void ImgClickListener(Task task);
    }


}

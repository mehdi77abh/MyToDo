package com.example.mytodo.historyListFragment;

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

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {
    private List<Task> tasks;
    private EventListener eventListener;

    public HistoryListAdapter(List<Task> doneTasks, EventListener eventListener) {
        this.tasks = doneTasks;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new HistoryListAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task,viewGroup,false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bindView(tasks.get(position));

    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getTask(int pos) {
        return tasks.get(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTv, dateTv, monthTv, titleTv, timeTv,descriptionTv;
        private View importanceView;
        private ImageView imgCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTv = itemView.findViewById(R.id.dayTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            importanceView = itemView.findViewById(R.id.importanceView);
            imgCheck = itemView.findViewById(R.id.img_check);
        }


        private void bindView(Task task) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(task.getDateLong());
            PersianDateImpl persianDate = new PersianDateImpl();
            persianDate.setDate(task.getDateLong());

            titleTv.setText(task.getTitle());
            descriptionTv.setText(task.getDescription());
            timeTv.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

            dayTv.setText(persianDate.getPersianDayOfWeekName());
            String number = PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianDay()));
            dateTv.setText(number);
            monthTv.setText(persianDate.getPersianMonthName());

            imgCheck.setBackgroundResource(R.drawable.shape_checkbox_check);
            imgCheck.setImageResource(R.drawable.ic_baseline_check_24_white);


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


            imgCheck.setOnClickListener(v -> {
            imgCheck.setImageResource(R.drawable.shape_checkbox_default);
            imgCheck.setBackgroundResource(0);
                Log.i("TAG", "bindView: "+getAdapterPosition());
                eventListener.ImgClickListener(task);

            });

            itemView.setOnLongClickListener(v -> {
                eventListener.ItemClickListener(task);

                return false;
            });

        }

    }

    public interface EventListener {
        void ItemClickListener(Task task);
        void ImgClickListener(Task task);
    }


}

package com.example.mytodo.calendarFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.model.Group;
import com.example.mytodo.R;
import com.example.mytodo.databinding.SelectGroupDialogBinding;
import com.example.mytodo.groupListFragment.AddGroupDialogFragment;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.ViewModelFactory;

public class SelectGroupDialog extends DialogFragment implements SelectGroupListAdapter.SelectGroupEventListener {

    private SelectGroupDialogBinding binding;
    private MainViewModel mainViewModel;
    private SelectGroupListAdapter adapter;
    private Long selectedLongDate;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(requireActivity(),new ViewModelFactory(getContext())).get(MainViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        binding = SelectGroupDialogBinding.inflate(LayoutInflater.from(getContext()),null,false);
        View view =binding.getRoot();
        builder.setView(view);
        selectedLongDate = getArguments().getLong("selectedLongDate");
        binding.rvSelectGroups.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        mainViewModel.getAllGroups().observe(requireActivity(),groups -> {
            adapter = new SelectGroupListAdapter(groups,SelectGroupDialog.this);
            binding.rvSelectGroups.setAdapter(adapter);
        });
        binding.newGroupIcon.setOnClickListener(v -> {
            AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
            addGroupDialogFragment.show(getParentFragmentManager(), null);

        });
        binding.dismissTv.setOnClickListener(v -> dismiss());

        return builder.create();


    }

    @Override
    public void onGroupSelected(Group group) {
        //Go To Add Task Frg With Selected Group
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedGroup",group);
        bundle.putLong("selectedLongDate",selectedLongDate);
        dismiss();
        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_calenderFragment_to_addTaskFragment2, bundle);
        AddGroupDialogFragment.hideKeyboardFrom(getContext(),binding.getRoot().getRootView());

    }
}

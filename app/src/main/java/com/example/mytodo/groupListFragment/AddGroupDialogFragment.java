package com.example.mytodo.groupListFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mytodo.model.Group;
import com.example.mytodo.databinding.FragmentAddGroupDialogBinding;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.ViewModelFactory;

public class AddGroupDialogFragment extends DialogFragment {
    private FragmentAddGroupDialogBinding binding;
    private MainViewModel mainViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mainViewModel = new ViewModelProvider(requireActivity(),new ViewModelFactory(getContext())).get(MainViewModel.class);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        binding = FragmentAddGroupDialogBinding.inflate(LayoutInflater.from(getContext()),null,false);
        View view =binding.getRoot();
        builder.setView(view);

        binding.txtAddGroupAgree.setOnClickListener(v -> {
            if (binding.txtInputGroup.getText().length()==0){
                binding.txtLayoutGroup.setError("نمیتواند خالی باشد");
            }else{
                Group group= new Group();
                group.setGroupTitle(binding.txtInputGroup.getText().toString());
                group.setCounter(0);
                mainViewModel.addGroup(group);
                Toast.makeText(getContext(), "کروه جدید اضافه شد", Toast.LENGTH_SHORT).show();
                hideKeyboardFrom(getContext(),binding.getRoot().getRootView());
                getDialog().dismiss();
            }

        });
        binding.txtAddGroupDismiss.setOnClickListener(v -> {
            hideKeyboardFrom(getContext(),binding.getRoot().getRootView());
            getDialog().dismiss();
        });

        return builder.create();


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding = null;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
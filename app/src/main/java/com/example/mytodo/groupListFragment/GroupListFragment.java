package com.example.mytodo.groupListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodo.Database.Group;
import com.example.mytodo.R;
import com.example.mytodo.databinding.FragmentGroupListBinding;
import com.example.mytodo.main.MainViewModel;
import com.example.mytodo.other.ViewModelFactory;

public class GroupListFragment extends Fragment implements GroupListAdapter.GroupEventListener {

    private FragmentGroupListBinding binding;
    private GroupListAdapter adapter;
    private MainViewModel mainViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupListBinding.inflate(inflater, container, false);

        View view = binding.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.groupToolbar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity(),new ViewModelFactory(getContext())).get(MainViewModel.class);
        binding.rvGroups.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        mainViewModel.getAllGroups().observe(requireActivity(),groups -> {
            adapter =new GroupListAdapter(groups,GroupListFragment.this);
            binding.rvGroups.setAdapter(adapter);
        });


        binding.btnAddGroup.setOnClickListener(v -> {
            AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
            addGroupDialogFragment.show(getParentFragmentManager(), null);
        });
    }


    @Override
    public void onGroupClickListener(Group group) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("group",group);
        Navigation.findNavController(getView()).navigate(R.id.action_groupListFragment_to_tasksListFragment,bundle);

    }

    @Override
    public void onLongGroupClickListener(Group group) {

    }

    @Override
    public void onImgStarClickListener(Group group) {
        group.setStar(!group.isStar());
        mainViewModel.updateGroup(group);
    }
}
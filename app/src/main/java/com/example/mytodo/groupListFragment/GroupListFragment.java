package com.example.mytodo.groupListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytodo.model.Group;
import com.example.mytodo.R;
import com.example.mytodo.databinding.FragmentGroupListBinding;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.ListTouchHelper;
import com.example.mytodo.myApp.ViewModelFactory;

public class GroupListFragment extends Fragment implements GroupListAdapter.GroupEventListener {

    private FragmentGroupListBinding binding;
    private GroupListAdapter adapter;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupListBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getContext())).get(MainViewModel.class);
        binding.rvGroups.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        Glide.with(getContext()).load(R.drawable.empty_state).into(binding.emptyStateImg);

        mainViewModel.getAllGroups().observe(requireActivity(), groups -> {
            if (groups.size() == 0) {
                binding.emptyStateContainer.setVisibility(View.VISIBLE);
            } else
                binding.emptyStateContainer.setVisibility(View.GONE);

            adapter = new GroupListAdapter(groups, GroupListFragment.this);
            binding.rvGroups.setAdapter(adapter);

        });

        binding.btnAddGroup.setOnClickListener(v -> {
            AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
            addGroupDialogFragment.show(getParentFragmentManager(), null);
        });

        ListTouchHelper.getTouchHelper(getContext(), pos -> {
            //     notificationHelper.deleteAlarm(adapter.getTask(pos));
            //     notificationHelper.deleteNotification(adapter.getTask(pos));
            Group group = adapter.getGroup(pos);
            Toast.makeText(getContext(), "شما "+group.getGroupTitle()+" را پاک کردید", Toast.LENGTH_SHORT).show();
            mainViewModel.deleteGroup(group);
            mainViewModel.deleteTasksByGroupId(group.getGroupId());

        }).attachToRecyclerView(binding.rvGroups);

    }


    @Override
    public void onGroupClickListener(Group group) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("group", group);
        Navigation.findNavController(getView()).navigate(R.id.action_groupListFragment_to_tasksListFragment, bundle);

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
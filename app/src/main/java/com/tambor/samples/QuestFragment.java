package com.tambor.samples;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tambor.samples.databinding.QuestFragmentBinding;
import com.tambor.samples.databinding.FragmentQuestListListBinding;
import com.tambor.samples.databinding.FragmentSecondBinding;

public class QuestFragment extends Fragment {
    private QuestFragmentBinding binding;
    private QuestViewModel mViewModel;

    public static QuestFragment newInstance() {
        return new QuestFragment();
    }

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quest_fragment, container, false);
    }*/
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = QuestFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
        //return inflater.inflate(R.layout.quest_fragment, container, false);

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuestViewModel.class);
        //View view = inflater.inflate(R.layout.quest_fragment, container, false);
        binding.buttonQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(QuestFragment.this)
                        .navigate(R.id.action_QuestFragment_toQuestListFragment);
            }
        });
    }
}
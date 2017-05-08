package com.sweetcompany.sweetie.Actions;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

import java.util.ArrayList;
import java.util.List;

public class ActionsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private ActionsAdapter mActionAdapter;
    private RecyclerView mActionsListView;

    private List<ActionObj> mActionsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionAdapter = new ActionsAdapter(mActionsList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.actions_fragment, container, false);

        mActionsListView = (RecyclerView) root.findViewById(R.id.actions_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mActionsListView.setLayoutManager(layoutManager);

        mActionsListView.setAdapter(mActionAdapter);
        mActionsListView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));


        return root;
    }

    //  TODO
    @Override
    public void onItemClick(View childView, int position) {
        int selectedItemPosition = mActionsListView.getChildAdapterPosition(childView);

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}

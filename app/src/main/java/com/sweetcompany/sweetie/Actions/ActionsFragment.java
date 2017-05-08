package com.sweetcompany.sweetie.Actions;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

public class ActionsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private ActionsAdapter mActionAdapter;
    private RecyclerView mActionsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionAdapter = new ActionsAdapter(20);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.actions_fragment, container, false);

        mActionsList = (RecyclerView) root.findViewById(R.id.actions_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mActionsList.setLayoutManager(layoutManager);

        mActionsList.setAdapter(mActionAdapter);
        mActionsList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));


        return root;
    }

    // TODO
    @Override
    public void onItemClick(View childView, int position) {
        int selectedItemPosition = mActionsList.getChildAdapterPosition(childView);

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}

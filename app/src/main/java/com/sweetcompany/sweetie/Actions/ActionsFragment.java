package com.sweetcompany.sweetie.Actions;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.Chat.ChatActivity;
import com.sweetcompany.sweetie.IPageChanger;
import com.sweetcompany.sweetie.R;

import java.util.List;

public class ActionsFragment extends Fragment implements ActionsContract.View {

    private ActionsAdapter mActionAdapter;
    private RecyclerView mActionsListView;

    private FloatingActionButton mFabNewAction;
    private FloatingActionButton mFabNewChatAction;
    private FloatingActionButton mFabNewPhotoAction;

    private ActionsContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionAdapter = new ActionsAdapter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.actions_fragment, container, false);

        mActionsListView = (RecyclerView) root.findViewById(R.id.actions_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mActionsListView.setLayoutManager(layoutManager);
        mActionsListView.setAdapter(mActionAdapter);

        mFabNewAction = (FloatingActionButton) root.findViewById(R.id.fab_new_action);

        mFabNewChatAction = (FloatingActionButton) root.findViewById(R.id.fab_new_chat);
        //TODO: create animation for fab mFabNewChatAction.startAnimation();
        mFabNewChatAction.setClickable(false);

        mFabNewPhotoAction = (FloatingActionButton) root.findViewById(R.id.fab_new_photo);
        //TODO: create animation for fab mFabNewPhotoAction.startAnimation();
        mFabNewPhotoAction.setClickable(false);



        // Add listener
        mFabNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show others action fab
                mFabNewPhotoAction.setVisibility(View.VISIBLE);
                mFabNewPhotoAction.setClickable(true);

                mFabNewChatAction.setVisibility(View.VISIBLE);
                mFabNewChatAction.setClickable(true);
            }
        });

        mFabNewChatAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide others action fab
                mFabNewPhotoAction.setVisibility(View.INVISIBLE);
                mFabNewPhotoAction.setClickable(false);

                mFabNewChatAction.setVisibility(View.INVISIBLE);
                mFabNewChatAction.setClickable(false);

                DialogFragment dialogFragment = ActionNewChatFragment.newInstance();
                dialogFragment.show(getActivity().getFragmentManager(), ActionNewChatFragment.TAG);
            }
        });

        return root;
    }

    @Override
    public void setPresenter(ActionsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateActionsList(List<ActionVM> actionsVM) {
        for(ActionVM actionVM : actionsVM) {
            actionVM.setPageChanger((IPageChanger)getActivity());
            actionVM.setContext(getContext());
        }

        mActionAdapter.updateActionsList(actionsVM);
    }
}

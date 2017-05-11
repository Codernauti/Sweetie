package com.sweetcompany.sweetie.Actions;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.IPageChanger;
import com.sweetcompany.sweetie.R;

import java.util.List;

public class ActionsFragment extends Fragment implements ActionsContract.View {

    private ActionsAdapter mActionAdapter;
    private RecyclerView mActionsListView;

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

    @Override
    public void showCalendarFragment() {
        ((DashboardActivity)getActivity()).changePageTo(0);
    }
}

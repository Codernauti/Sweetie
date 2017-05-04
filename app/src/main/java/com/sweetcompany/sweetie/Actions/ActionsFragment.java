package com.sweetcompany.sweetie.Actions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

public class ActionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHome = inflater.inflate(R.layout.actions_fragment, container, false);

        return viewHome;
    }


}

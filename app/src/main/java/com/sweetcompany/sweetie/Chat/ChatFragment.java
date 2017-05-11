package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_fragment, container, false);


        return root;
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static ChatFragment newInstance() {
        ChatFragment newChatFragment = new ChatFragment();
        return newChatFragment;
    }


}

package com.sweetcompany.sweetie.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetcompany.sweetie.R;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View {


    private ChatAdapter mChatAdapter;
    private RecyclerView mChatListView;

    private ChatContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_fragment, container, false);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mChatListView.setLayoutManager(layoutManager);
        mChatListView.setAdapter(mChatAdapter);

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

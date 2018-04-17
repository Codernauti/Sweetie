package com.codernauti.sweetie.chatDiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codernauti.sweetie.R;
import com.codernauti.sweetie.chat.ChatAdapter;
import com.codernauti.sweetie.chat.MessageVM;
import com.codernauti.sweetie.chat.ShowImageFragment;
import com.codernauti.sweetie.chat.TextPhotoMessageVM;

import java.util.List;


public class ChatDiaryFragment extends Fragment implements ChatDiaryContract.View,
    ChatAdapter.ChatAdapterListener, ActionMode.Callback {

    private static final String TAG = "ChatDiaryFragment";

    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mChatListView;
    private Toolbar mToolBar;

    private ActionMode mActionMode;

    private ChatDiaryContract.Presenter mPresenter;


    public static ChatDiaryFragment newInstance(Bundle bundle) {
        ChatDiaryFragment newChatFragment = new ChatDiaryFragment();
        newChatFragment.setArguments(bundle);

        return newChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter(true);
        mChatAdapter.setModeChatDiary();
        mChatAdapter.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat_diary_fragment, container, false);

        String titleChatDiary = getArguments().getString(ChatDiaryActivity.ACTION_DIARY_DATE);

        mToolBar = (Toolbar) root.findViewById(R.id.chat_diary_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleChatDiary);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(false);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_diary_list);
        mChatListView.setLayoutManager(mLinearLayoutManager);
        mChatListView.setAdapter(mChatAdapter);

        return root;
    }

    @Override
    public void setPresenter(ChatDiaryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMessage(MessageVM msgVM) {
        mChatAdapter.addMessage(msgVM);
    }

    @Override
    public void removeMessage(String msgUid) {
        mChatAdapter.removeMessage(msgUid);
    }


    // ChatAdapter callbacks

    @Override
    public void onBookmarkClicked(MessageVM messageVM, int type) {

    }

    @Override
    public void onPhotoClicked(TextPhotoMessageVM photoMessage) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ShowImageFragment.newInstance(photoMessage)
                .show(ft, ShowImageFragment.TAG);
    }

    @Override
    public void onMessageLongClicked() {
        mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(this);
    }

    @Override
    public void onMessageSelectionFinished() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }


    // ActionMode callbacks

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.chat_cab_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_menu_remove: {
                List<MessageVM> selectedItems = mChatAdapter.getSelectedItems();
                for (int i = selectedItems.size() - 1; i >= 0; i--) {
                    MessageVM message = selectedItems.get(i);
                    Log.d(TAG, "Remove bookmarked item: " + message.getKey());
                    mPresenter.removeBookmarkedMessage(message);
                }
                mode.finish();
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        mChatAdapter.clearSelections();
    }
}

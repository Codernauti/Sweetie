package com.sweetcompany.sweetie.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.Utility;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View, View.OnClickListener,
        View.OnTouchListener,
        ChatAdapter.ChatAdapterListener {

    private static final String TAG = "ChatFragment";

    private int mKeyboardHeight;
    private InputMethodManager mInputMethodManager;

    private Toolbar mToolBar;
    private RecyclerView mChatListView;

    private LinearLayoutManager mLinearLayoutManager;
    private EditText mTextMessageInput;
    private Button mSendButton;

    private ImageButton mEmojiButton;
    private FrameLayout mKeyboardPlaceholder;

    //private EmojiconsView mEmojiView;
    private PopupWindow mEmojiPopup;

    private ChatAdapter mChatAdapter;
    private ChatContract.Presenter mPresenter;

    private static final int SOFT_KB_CLOSED = 0;
    private static final int SOFT_KB_OPENED = 1;

    private int mKeyboardState = SOFT_KB_CLOSED;


    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment newChatFragment = new ChatFragment();
        newChatFragment.setArguments(bundle);

        return newChatFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatAdapter = new ChatAdapter();
        mChatAdapter.setChatAdapterListener(this);

        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.chat_fragment, container, false);

        String titleChat = getArguments().getString(ChatActivity.CHAT_TITLE);
        Log.d(TAG, "from Intent CHAT_TITLE: " +
                getArguments().getString(ChatActivity.CHAT_TITLE));
        Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                getArguments().getString(ChatActivity.CHAT_DATABASE_KEY));

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);

        mToolBar = (Toolbar) root.findViewById(R.id.chat_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleChat);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mChatListView.setLayoutManager(mLinearLayoutManager);
        mChatListView.setAdapter(mChatAdapter);

        mTextMessageInput = (EditText) root.findViewById(R.id.chat_text_message_input);
        mEmojiButton = (ImageButton) root.findViewById(R.id.chat_emoticons_button);
        mSendButton = (Button) root.findViewById(R.id.chat_send_button);

        mKeyboardPlaceholder = (FrameLayout) root.findViewById(R.id.chat_emojicons_container);
        /*mEmojiView = new EmojiconsView(getActivity());
        mEmojiView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));*/

        // get saved height of past keyboard used
        mKeyboardHeight = Utility.getIntPreference(getContext(), Utility.KB_HEIGHT);
        updateHeightPlaceholder();

        View fakeView = new View(getActivity());
        fakeView.setBackgroundColor(Color.BLACK);
        mEmojiPopup = new PopupWindow(fakeView, ViewGroup.LayoutParams.MATCH_PARENT, mKeyboardHeight, false);
/*        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        mEmojiPopup.setHeight(View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));*/


        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                root.getWindowVisibleDisplayFrame(r);

                int screenHeight = root.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom);
                Log.d(TAG, "Height keyboard: " + heightDifference);

                // if more than 100 px it is probably a keyboard
                if (heightDifference > 100 ) {
                    Log.d(TAG, "Keyboard pop-up!");
                    mKeyboardState = SOFT_KB_OPENED;

                    if (heightDifference != mKeyboardHeight) {
                        Log.d(TAG, "New height of keyboard!");
                        mKeyboardHeight = heightDifference;
                        updateHeightPlaceholder();
                        mEmojiPopup.setHeight(mKeyboardHeight);

                        Utility.saveIntPreference(getContext(), Utility.KB_HEIGHT, heightDifference);
                        // we get the info, remove the listener
                        // root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
                else if (mKeyboardState == SOFT_KB_OPENED) {
                    Log.d(TAG, "Soft keyboard opened is closing, hide placeholder");
                    // keyboard closed
                    hideKeyboardPlaceholder();
                    mKeyboardState = SOFT_KB_CLOSED;
                }
            }
        });

        // get default height of keyboard
        final float defaultHeight = getResources().getDimension(R.dimen.keyboard_height_default);

        if (mKeyboardHeight == defaultHeight) {
            // app doesn't have the keyboard height -> force the opening
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }


        mTextMessageInput.setOnTouchListener(this);
        mEmojiButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);

        return root;
    }

    private void updateHeightPlaceholder() {
        if (mKeyboardHeight > 100) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mKeyboardPlaceholder.getLayoutParams();
            params.height = mKeyboardHeight;
            mKeyboardPlaceholder.setLayoutParams(params);
            Log.d(TAG, "Placeholder height changed! " + mKeyboardHeight);
        }
    }


    /*private void initEmoticonsView(View root) {

        // create layout params (compatible with RelativeLayout)
        // transform pixel of 240dp
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
    }*/

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMessages(List<MessageVM> messagesVM) {
        mChatAdapter.updateMessageList(messagesVM);
    }

    @Override
    public void updateChatInfo(ChatVM chat) {
        mToolBar.setTitle(chat.getTitle());
    }

    @Override
    public void updateMessage(MessageVM msgVM) {
        mChatAdapter.addMessage(msgVM);
        mChatListView.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    @Override
    public void removeMessage(MessageVM msgVM) {
        mChatAdapter.removeMessage(msgVM);
    }

    @Override
    public void changeMessage(MessageVM msgVM) {
        mChatAdapter.changeMessage(msgVM);
    }

    @Override
    public boolean hideKeyboardPlaceholder() {
        if (mKeyboardPlaceholder.getVisibility() != View.GONE) {
            mEmojiPopup.dismiss();
            mKeyboardPlaceholder.setVisibility(View.GONE);
            mEmojiButton.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    R.drawable.chat_open_emoticon_image_button24x24));
            return true;
        } else {
            return false;
        }
    }


    // Callback bottom input bar
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_button:
                String inputText = mTextMessageInput.getText().toString();
                mTextMessageInput.setText("");

                if (!inputText.isEmpty()) {
                    // TODO: is this responsibility of fragment?
                    MessageVM newMessage = null;
                    try {
                        newMessage = new TextMessageVM(inputText, MessageVM.THE_MAIN_USER, DataMaker.get_UTC_DateTime(), false, null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // TODO: update view to feedback user if he is offline
                    //mChatAdapter.addMessage(newMessage);
                    mPresenter.sendMessage(newMessage);
                }
                break;

            case R.id.chat_emoticons_button:
                insertKeyboardSpaceHolder();

                if (mEmojiPopup.isShowing()) {
                    // close emoticons and show keyboard
                    mEmojiPopup.dismiss();
                    mEmojiButton.setImageDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.chat_open_emoticon_image_button24x24));
                    mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else {
                    mKeyboardState = SOFT_KB_CLOSED; // jump hidePlaceHolder from OnGlobalLayoutListener
                    // close keyboard and open emoticons
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    mEmojiButton.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.chat_keyboard_image_button24x24));

                    mEmojiPopup.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                }
                break;

            default:
                break;
        }
    }

    private void insertKeyboardSpaceHolder() {
        if (mKeyboardPlaceholder.getVisibility() == View.GONE) {
            mKeyboardPlaceholder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.chat_text_message_input:
                insertKeyboardSpaceHolder();
                mEmojiPopup.dismiss();
                break;

            default:
                break;
        }
        return false;
    }

    // ChatAdapter callback
    @Override
    public void onBookmarkClicked(MessageVM messageVM) {
        // TODO: is this responsibility of fragment?
        mPresenter.bookmarkMessage(messageVM);
    }
}

package com.sweetcompany.sweetie.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.utils.DataMaker;
import com.sweetcompany.sweetie.utils.Utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.emoji.Emojicon;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ghiro on 11/05/2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View, View.OnClickListener,
        View.OnTouchListener,
        ChatAdapter.ChatAdapterListener, EmoticonsPagerAdapter.EmoticonsPagerAdapterListener {

    private static final String TAG = "ChatFragment";

    private static final int MIN_KB_HEIGHT = 100;
    private static final int SOFT_KB_CLOSED = 0;
    private static final int SOFT_KB_OPENED = 1;
    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;

    private int mKeyboardState = SOFT_KB_CLOSED;
    private int mKeyboardHeight;
    private boolean mIsSoftActionButtonsMeasured;
    private int mSoftKeyHeight = 0;

    private ArrayList<Image> imagesPicked = new ArrayList<>();

    private InputMethodManager mInputMethodManager;
    private Toolbar mToolBar;
    private RecyclerView mChatListView;
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mTextMessageInput;

    private Button mSendButton;
    private ImageButton mEmojiButton;
    private ImageButton mMediaPickerButton;

    private FrameLayout mKeyboardPlaceholder;
    private ViewPager mEmojiView;
    private EmoticonsPagerAdapter mEmoticonsAdapter;

    private PopupWindow mEmojiPopup;
    private ChatAdapter mChatAdapter;

    private ChatContract.Presenter mPresenter;


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
        // root is a RelativeLayout
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat_fragment, container, false);

        String titleChat = getArguments().getString(ChatActivity.CHAT_TITLE);
        Log.d(TAG, "from Intent CHAT_TITLE: " +
                getArguments().getString(ChatActivity.CHAT_TITLE));
        Log.d(TAG, "from Intent CHAT_DATABASE_KEY: " +
                getArguments().getString(ChatActivity.CHAT_DATABASE_KEY));

        // initialize toolbar
        mToolBar = (Toolbar) root.findViewById(R.id.chat_toolbar);
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(mToolBar);
        parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentActivity.getSupportActionBar().setTitle(titleChat);


        // initialize message's list
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mChatListView = (RecyclerView) root.findViewById(R.id.chat_list);
        mChatListView.setLayoutManager(mLinearLayoutManager);
        mChatListView.setAdapter(mChatAdapter);

        mTextMessageInput = (EditText) root.findViewById(R.id.chat_text_message_input);
        mEmojiButton = (ImageButton) root.findViewById(R.id.chat_emoticons_button);
        mMediaPickerButton = (ImageButton) root.findViewById(R.id.chat_media_picker_button);
        mSendButton = (Button) root.findViewById(R.id.chat_send_button);

        mKeyboardPlaceholder = (FrameLayout) root.findViewById(R.id.chat_emojicons_container);

        // TODO: EmojiView
        initializaEmoticons(root);

        // get saved height of past keyboard used
        mKeyboardHeight = Utility.getIntPreference(getContext(), Utility.KB_HEIGHT);
        updateHeightPlaceholder();

        mEmojiPopup = new PopupWindow(mEmojiView, ViewGroup.LayoutParams.MATCH_PARENT, mKeyboardHeight, false);

        // Device has hard Action Buttons? true -> no other measures needed
        mIsSoftActionButtonsMeasured = ViewConfiguration.get(getContext()).hasPermanentMenuKey();


        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                root.getWindowVisibleDisplayFrame(r);

                int screenHeight = root.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom) - mSoftKeyHeight;
                Log.d(TAG, "Height keyboard: " + heightDifference);

                // if more than 100 px it is probably a keyboard
                if (heightDifference > MIN_KB_HEIGHT) {
                    Log.d(TAG, "Keyboard pop-up! Height saved: " + mKeyboardHeight);
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
                else {
                    if (mKeyboardState == SOFT_KB_OPENED) {
                        Log.d(TAG, "Soft keyboard opened is closing, hide placeholder (emoji keyboard is closed)");
                        // keyboard closed
                        hideKeyboardPlaceholder();
                        mKeyboardState = SOFT_KB_CLOSED;
                    }
                    if (!mIsSoftActionButtonsMeasured) {
                        Log.d(TAG, "Soft action buttons measured: " + heightDifference);
                        mIsSoftActionButtonsMeasured = true;
                        mSoftKeyHeight = heightDifference;
                    }
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
        mMediaPickerButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);

        return root;
    }

    private void initializaEmoticons(ViewGroup root) {
        mEmojiView = (ViewPager) getLayoutInflater(null).inflate(R.layout.chat_emoticons_keyboard, root, false);
        mEmoticonsAdapter = new EmoticonsPagerAdapter(getContext());

        mEmojiView.setAdapter(mEmoticonsAdapter);

        // get the specific View from LinearLayout from TabLayout
        TabLayout mEmoticonsTabs = (TabLayout) mEmojiView.findViewById(R.id.chat_emoticons_tabs);
        mEmoticonsTabs.setupWithViewPager(mEmojiView);

        mEmoticonsTabs.getTabAt(0).setIcon(R.drawable.ic_emoji_people_light);
        mEmoticonsTabs.getTabAt(1).setIcon(R.drawable.ic_emoji_nature_light);
        mEmoticonsTabs.getTabAt(2).setIcon(R.drawable.ic_emoji_places_light);
        mEmoticonsTabs.getTabAt(3).setIcon(R.drawable.ic_emoji_objects_light);
        mEmoticonsTabs.getTabAt(4).setIcon(R.drawable.ic_emoji_symbols_light);
        mEmoticonsTabs.getTabAt(5).setIcon(R.drawable.chat_backspace_24x24);

        LinearLayout tabStrip = ((LinearLayout) mEmoticonsTabs.getChildAt(0));
        tabStrip.getChildAt(5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO: delete only one element, can keep delete
                // use a runnable to delay the deletion
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    KeyEvent fakeDelEvent = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    mTextMessageInput.dispatchKeyEvent(fakeDelEvent);
                }
                return true;
            }
        });
    }

    private void updateHeightPlaceholder() {
        if (mKeyboardHeight > MIN_KB_HEIGHT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mKeyboardPlaceholder.getLayoutParams();
            params.height = mKeyboardHeight;
            mKeyboardPlaceholder.setLayoutParams(params);
            Log.d(TAG, "Placeholder height changed! " + mKeyboardHeight);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mEmoticonsAdapter.setListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mInputMethodManager.hideSoftInputFromWindow(mTextMessageInput.getWindowToken(), 0);
        mEmoticonsAdapter.removeListener();
    }


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
                    /**//*
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();

                    Fragment prev = fm.findFragmentByTag("emojiFragment");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);

                    EmojiKeyboardFragment frag = EmojiKeyboardFragment.newInstance(mKeyboardHeight);
                    frag.show(fm, "emojiFragment");

                    *//**/

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

            case R.id.chat_media_picker_button:

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
                takePictures();
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

    // EmoticonsAdapter callback
    @Override
    public void onEmoticonsClicked(Emojicon emojicon) {
        int start = mTextMessageInput.getSelectionStart();
        int end = mTextMessageInput.getSelectionEnd();

        if (start < 0) {
            mTextMessageInput.append(emojicon.getEmoji());
        }
        else {
            mTextMessageInput.getText().replace(
                    Math.min(start, end),
                    Math.max(start, end),
                    emojicon.getEmoji(),
                    0,
                    emojicon.getEmoji().length());
        }
    }

    public void takePictures() {

        ImagePicker imagePicker = ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(false) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle(String.valueOf(R.string.image_picker_select)); // image selection title

        //imagePicker.multi(); // multi mode (default mode)
        imagePicker.single();

        imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(imagesPicked) // original selected images, used in multi mode
                .start(RC_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            imagesPicked = (ArrayList<Image>) ImagePicker.getImages(data);
            //sendImages(imagesPicked);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

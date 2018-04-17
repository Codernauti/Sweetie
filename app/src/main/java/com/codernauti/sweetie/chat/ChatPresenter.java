package com.codernauti.sweetie.chat;

import com.codernauti.sweetie.model.ChatFB;
import com.codernauti.sweetie.firebase.FirebaseChatController;
import com.codernauti.sweetie.model.MessageFB;
import com.codernauti.sweetie.utils.DataMaker;

import java.util.Calendar;
import java.util.Date;

class ChatPresenter implements ChatContract.Presenter, FirebaseChatController.Listener {

    private static final String TAG = "ChatPresenter";

    private final ChatContract.View mView;
    private final FirebaseChatController mController;
    private final String mUserUid;   // id of messages of main user

    private Date mLastMsgDate;
    private Calendar mLastMsgDateCalendar = Calendar.getInstance();
    private Calendar mMsgcalendar = Calendar.getInstance();

    ChatPresenter(ChatContract.View view, FirebaseChatController controller, String userUid){
        mView = view;
        mView.setPresenter(this);
        mController = controller;
        mController.setListener(this);

        mUserUid = userUid;
    }

    @Override
    public void sendTextMessage(String messageText) {
        MessageFB newMessage = new MessageFB(mUserUid, messageText, DataMaker.get_UTC_DateTime(),
                false, MessageFB.TEXT_MSG, null);

        mController.sendMessage(newMessage);
    }

    @Override
    public void sendPhotoMessage(String msgPhotoUriLocal, String textMsg) {
        MessageFB newMessage = new MessageFB(mUserUid, textMsg, DataMaker.get_UTC_DateTime(),
                false, MessageFB.PHOTO_MSG, msgPhotoUriLocal);

        mController.uploadPhotoMessage(newMessage);
    }

    @Override
    public void bookmarkMessage(MessageVM messageVM, int type) {
        if(type == MessageVM.TEXT_MSG){
            TextMessageVM msgVM = (TextMessageVM) messageVM;
            MessageFB updateMessage = new MessageFB(msgVM.getCreatorUid(), msgVM.getText(), msgVM.getTime(),
                    msgVM.isBookmarked(), MessageFB.TEXT_MSG, null);
            updateMessage.setKey(msgVM.getKey());

            mController.updateMessage(updateMessage);

        } else if(type == MessageVM.TEXT_PHOTO_MSG) {

            TextPhotoMessageVM msgVM = (TextPhotoMessageVM) messageVM;
            MessageFB updateMessage = new MessageFB(msgVM.getCreatorUid(), msgVM.getText(), msgVM.getTime(),
                    msgVM.isBookmarked(), MessageFB.PHOTO_MSG, msgVM.getUriStorage());
            updateMessage.setKey(msgVM.getKey());

            mController.updateMessage(updateMessage);
        }

    }


    // Callback from Database

    @Override
    public void onChatChanged(ChatFB chat) {
        ChatVM chatVM = new ChatVM(chat.getKey(), chat.getTitle());
        mView.updateChatInfo(chatVM);
    }

    @Override
    public void onMessageAdded(MessageFB message) {
        Date messageDate = DataMaker.getDateFromIsoFormatString(message.getDateTime());

        insertDateItemVM(messageDate);
        insertMessageVM(message);
    }

    private void insertDateItemVM(Date messageDate) {
        if (mLastMsgDate == null) {
            // first message
            mLastMsgDate = messageDate;
            mView.insertDateItem(new DateItemVM(messageDate));
        }
        else {
            mLastMsgDateCalendar.setTime(mLastMsgDate);
            mMsgcalendar.setTime(messageDate);

            if (mLastMsgDateCalendar.get(Calendar.YEAR) != mMsgcalendar.get(Calendar.YEAR) ||
                    mLastMsgDateCalendar.get(Calendar.MONTH) != mMsgcalendar.get(Calendar.MONTH) ||
                    mLastMsgDateCalendar.get(Calendar.DAY_OF_MONTH) != mMsgcalendar.get(Calendar.DAY_OF_MONTH)) {
                // insert Data ViewModel if LastMsgDate is before messageDate
                mLastMsgDate = messageDate;
                mView.insertDateItem(new DateItemVM(messageDate));
            }
        }
    }

    private void insertMessageVM(MessageFB message) {
        MessageVM msgVM = MessageConverter.createMessageVM(message, mUserUid);
        mView.updateMessage(msgVM);
    }


    @Override
    public void onMessageRemoved(MessageFB message) {
        MessageVM msgVM = MessageConverter.createMessageVM(message, mUserUid);
        mView.removeMessage(msgVM);
    }

    @Override
    public void onMessageChanged(MessageFB message) {
        MessageVM msgVM = MessageConverter.createMessageVM(message, mUserUid);
        mView.changeMessage(msgVM);
    }


    @Override
    public void onUploadPercent(MessageFB media, int progress){
        mView.updatePercentUpload(media.getKey(), progress);
    }

}

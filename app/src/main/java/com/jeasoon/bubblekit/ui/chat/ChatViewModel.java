package com.jeasoon.bubblekit.ui.chat;

import android.app.Person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jeasoon.bubblekit.data.PersonManager;
import com.jeasoon.bubblekit.data.PoetryManager;
import com.jeasoon.bubblekit.notification.NotificationEmitter;
import com.jeasoon.bubblekit.notification.NotificationMessage;
import com.jeasoon.bubblekit.notification.NotificationSession;
import com.jeasoon.bubblekit.server.ChatMessage;
import com.jeasoon.bubblekit.server.ChatMsgReceiver;
import com.jeasoon.bubblekit.server.ChatServer;
import com.jeasoon.bubblekit.server.ChatSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatViewModel extends ViewModel implements ChatMsgReceiver {

    private MutableLiveData<List<Message>> mMessageList;
    private MutableLiveData<String> mCachedInput;
    private ChatSession mChatSession;
    private NotificationSession mNotificationSession;
    private Person mSelf;
    private boolean isResumed;

    public ChatViewModel() {
        mCachedInput = new MutableLiveData<>();
        mMessageList = new MutableLiveData<>();
        mMessageList.setValue(new ArrayList<Message>());
        mSelf = getSelfPerson();
        mNotificationSession = NotificationEmitter.getInstance().createNotificationSession(mSelf);
        mNotificationSession.setModelName(getClass().getName());
        mChatSession = ChatServer.getInstance().createSession(mSelf);
        mChatSession.setChatMsgReceiver(this);
        for (Person person : getChatPeople()) {
            if (person == mSelf) {
                continue;
            }
            mChatSession.addPerson(person);
        }
    }

    public String getPoetryText() {
        return PoetryManager.getInstance().next().toParagraphsString();
    }

    public String getChatSessionName() {
        List<Person> people = getChatPeople();
        if (people != null && !people.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;
            for (Person person : people) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(", ");
                }
                sb.append(person.getName());
            }
            return sb.toString();
        }
        return "";
    }

    public void sendMsg(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(mSelf);
        chatMessage.setMessage(msg);
        chatMessage.setTimestamp(System.currentTimeMillis());
        mChatSession.postSendMsg(chatMessage);
    }

    public void setCachedInput(String cachedInput) {
        mCachedInput.postValue(cachedInput);
    }

    public LiveData<String> getCachedInput() {
        return mCachedInput;
    }

    public LiveData<List<Message>> getMessageList() {
        return mMessageList;
    }

    protected Person getSelfPerson() {
        return PersonManager.getInstance().getSelfPerson();
    }

    protected List<Person> getChatPeople() {
        List<Person> friendList = new ArrayList<>();
        friendList.add(PersonManager.getInstance().getFriendPerson());
        return friendList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mChatSession.destroy();
        mNotificationSession.destroy();
    }

    public void onResume(boolean isNotificationActivity) {
        isResumed = true;
        if (!isNotificationActivity) {
            mNotificationSession.cancelNotification();
        }
    }

    public void onPause() {
        isResumed = false;
    }

    @Override
    public void onReceiveMessage(Person receiver, ChatMessage chatMessage, boolean isIncoming) {
        // 更新界面列表
        List<Message> msgList = mMessageList.getValue();
        Message msg = new Message();
        msg.setIncoming(isIncoming);
        msg.setReceiver(receiver);
        msg.setSender(chatMessage.getSender());
        msg.setContent(chatMessage.getMessage());
        msg.setTimestamp(chatMessage.getTimestamp());
        Objects.requireNonNull(msgList).add(msg);
        mMessageList.postValue(msgList);

        // 更新通知列表
        NotificationMessage nMsg = new NotificationMessage();
        nMsg.setIncoming(isIncoming);
        nMsg.setMessage(chatMessage.getMessage());
        nMsg.setPerson(chatMessage.getSender());
        nMsg.setTimestamp(chatMessage.getTimestamp());
        nMsg.setRead(isResumed);
        mNotificationSession.enqueueNotificationMessage(nMsg);
        if (!isResumed) {
            mNotificationSession.sendNotification();
        }
    }
}
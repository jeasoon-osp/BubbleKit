package com.jeasoon.bubblekit.ui.chat;

import android.app.Person;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jeasoon.bubblekit.data.PoetryManager;
import com.jeasoon.bubblekit.data.PersonFactory;
import com.jeasoon.bubblekit.server.ChatMessage;
import com.jeasoon.bubblekit.server.ChatMsgReceiveRunnable;
import com.jeasoon.bubblekit.server.ChatServer;
import com.jeasoon.bubblekit.server.ChatSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatViewModel extends ViewModel implements ChatMsgReceiveRunnable {

    private MutableLiveData<List<Message>> mMessageList;
    private ChatSession mChatSession;
    private Person mSelf;

    public ChatViewModel() {
        mMessageList = new MutableLiveData<>();
        mMessageList.setValue(new ArrayList<Message>());
        mSelf = getSelfPerson();
        mChatSession = ChatServer.getInstance().createSession(mSelf);
        mChatSession.postReceiveMsg(this);
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

    public void sendMsg(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(mSelf);
        chatMessage.setMessage(msg);
        mChatSession.postSendMsg(chatMessage);
    }

    public LiveData<List<Message>> getMessageList() {
        return mMessageList;
    }

    protected Person getSelfPerson() {
        return PersonFactory.getInstance().getSelfPerson();
    }

    protected List<Person> getChatPeople() {
        List<Person> friendList = new ArrayList<>();
        friendList.add(PersonFactory.getInstance().getFriendPerson());
        return friendList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mChatSession.destroy();
    }

    @Override
    public void onReceiveMessage(Person receiver, ChatMessage chatMessage, boolean isIncoming) {
        List<Message> msgList = mMessageList.getValue();
        Message msg = new Message();
        msg.setIncoming(isIncoming);
        msg.setReceiver(receiver);
        msg.setSender(chatMessage.getSender());
        msg.setContent(chatMessage.getMessage());
        Objects.requireNonNull(msgList).add(msg);
        mMessageList.postValue(msgList);
        mChatSession.postReceiveMsg(this);
    }
}
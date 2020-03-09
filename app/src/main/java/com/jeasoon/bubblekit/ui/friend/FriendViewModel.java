package com.jeasoon.bubblekit.ui.friend;

import android.app.Person;

import com.jeasoon.bubblekit.data.PersonManager;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendViewModel extends ChatViewModel {

    @Override
    protected Person getSelfPerson() {
        return PersonManager.getInstance().getFriendPerson();
    }

    @Override
    protected List<Person> getChatPeople() {
        List<Person> friendList = new ArrayList<>();
        friendList.add(PersonManager.getInstance().getSelfPerson());
        return friendList;
    }

}
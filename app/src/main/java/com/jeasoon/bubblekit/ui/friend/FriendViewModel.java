package com.jeasoon.bubblekit.ui.friend;

import android.app.Person;

import com.jeasoon.bubblekit.data.PersonFactory;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendViewModel extends ChatViewModel {

    @Override
    protected Person getSelfPerson() {
        return PersonFactory.getInstance().getFriendPerson();
    }

    @Override
    protected List<Person> getChatPeople() {
        List<Person> friendList = new ArrayList<>();
        friendList.add(PersonFactory.getInstance().getSelfPerson());
        return friendList;
    }

}
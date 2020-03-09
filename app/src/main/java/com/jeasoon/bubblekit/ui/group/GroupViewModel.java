package com.jeasoon.bubblekit.ui.group;

import android.app.Person;

import com.jeasoon.bubblekit.data.PersonManager;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

import java.util.List;

public class GroupViewModel extends ChatViewModel {

    @Override
    protected Person getSelfPerson() {
        return PersonManager.getInstance().getSelfPerson();
    }

    @Override
    protected List<Person> getChatPeople() {
        return PersonManager.getInstance().getGroupPerson();
    }
}
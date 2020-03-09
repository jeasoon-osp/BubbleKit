package com.jeasoon.bubblekit.ui.group;

import android.app.Person;

import com.jeasoon.bubblekit.data.PersonFactory;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

import java.util.List;

public class GroupViewModel extends ChatViewModel {

    @Override
    protected Person getSelfPerson() {
        return PersonFactory.getInstance().getSelfPerson();
    }

    @Override
    protected List<Person> getChatPeople() {
        return PersonFactory.getInstance().getGroupPerson();
    }
}
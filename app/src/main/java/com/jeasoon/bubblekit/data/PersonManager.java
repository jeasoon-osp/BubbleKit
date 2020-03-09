package com.jeasoon.bubblekit.data;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Context;
import android.graphics.drawable.Icon;

import com.jeasoon.bubblekit.R;

import java.util.ArrayList;
import java.util.List;

public class PersonManager {

    private Person mSelfPerson;
    private Person mFriendPerson;
    private List<Person> mGroupPerson;

    private PersonManager() {
    }

    private static class PersonManagerHolder {
        @SuppressLint("StaticFieldLeak")
        private static final PersonManager INSTANCE = new PersonManager();
    }

    public static PersonManager getInstance() {
        return PersonManagerHolder.INSTANCE;
    }

    public void init(Context context) {
        mGroupPerson = new ArrayList<>();
        mGroupPerson.add(createPerson(context, true, R.drawable.dog, "Dog", "dog"));
        mGroupPerson.add(createPerson(context, false, R.drawable.parrot, "Parrot", "parrot"));
        mGroupPerson.add(createPerson(context, true, R.drawable.sheep, "Sheep", "sheep"));
        mSelfPerson = createPerson(context, false, R.drawable.cat, "Cat", "cat");
        mFriendPerson = createPerson(context, false, R.drawable.sheep, "Sheep", "sheep");
    }

    public Person getSelfPerson() {
        return mSelfPerson;
    }

    public Person getFriendPerson() {
        return mFriendPerson;
    }

    public List<Person> getGroupPerson() {
        return mGroupPerson;
    }

    private Person createPerson(Context context, boolean isBot, int icon, String name, String key) {
        return new Person.Builder()
                .setBot(isBot)
                .setIcon(Icon.createWithResource(context, icon))
                .setName(name)
                .build();
    }
}

package com.jeasoon.bubblekit.data;

import android.app.Person;
import android.content.Context;
import android.graphics.PorterDuff;
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

    public void clear() {
        mGroupPerson = null;
        mSelfPerson = null;
        mFriendPerson = null;
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

    private Person createPerson(Context context, boolean isBot, int iconId, String name, String key) {
        Icon icon = Icon.createWithResource(context, iconId);
        icon.setTintMode(PorterDuff.Mode.DST);
        return new Person.Builder()
                .setBot(isBot)
                .setKey(key)
                .setIcon(icon)
                .setName(name)
                .build();
    }
}

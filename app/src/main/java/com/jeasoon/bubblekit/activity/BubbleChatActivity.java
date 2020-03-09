package com.jeasoon.bubblekit.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.ui.chat.ChatFragment;

public class BubbleChatActivity extends BaseChatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_container, new ChatFragment(getIntent().getStringExtra(EXTRA_CHAT_VIEW_MODEL)))
                .commit();
    }

}

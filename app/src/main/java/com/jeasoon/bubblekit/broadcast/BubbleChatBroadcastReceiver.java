package com.jeasoon.bubblekit.broadcast;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jeasoon.bubblekit.constant.ChatConstant;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;
import com.jeasoon.bubblekit.ui.viewmodel.ChatViewModelProvider;

public class BubbleChatBroadcastReceiver extends BroadcastReceiver implements ChatConstant {

    private static final String TAG = "BubbleChatBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String chatViewModelName = intent.getStringExtra(EXTRA_CHAT_VIEW_MODEL);
        if (TextUtils.isEmpty(chatViewModelName)) {
            Log.w(TAG, "Receive empty view model intent: " + intent);
            return;
        }
        Bundle results = RemoteInput.getResultsFromIntent(intent);
        ChatViewModel viewModel = ChatViewModelProvider.getInstance().get(chatViewModelName);
        viewModel.sendMsg(results.getString(EXTRA_CHAT_REMOTE_INPUT_RESULT_KEY));
    }
}

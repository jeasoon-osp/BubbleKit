package com.jeasoon.bubblekit.ui.friend;

import com.jeasoon.bubblekit.ui.chat.ChatFragment;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

public class FriendFragment extends ChatFragment {

    @Override
    protected Class<? extends ChatViewModel> getViewModelClass() {
        return FriendViewModel.class;
    }

}
package com.jeasoon.bubblekit.ui.self;

import com.jeasoon.bubblekit.ui.chat.ChatFragment;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

public class SelfFragment extends ChatFragment {

    @Override
    protected Class<? extends ChatViewModel> getViewModelClass() {
        return SelfViewModel.class;
    }

}
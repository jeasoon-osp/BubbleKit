package com.jeasoon.bubblekit.ui.group;

import com.jeasoon.bubblekit.ui.chat.ChatFragment;
import com.jeasoon.bubblekit.ui.chat.ChatViewModel;

public class GroupFragment extends ChatFragment {

    @Override
    protected Class<? extends ChatViewModel> getViewModelClass() {
        return GroupViewModel.class;
    }

}
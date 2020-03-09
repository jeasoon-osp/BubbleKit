package com.jeasoon.bubblekit.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class ChatViewModelProvider extends ViewModelProvider {

    private static class ChatViewModelProviderHolder {
        private static final ChatViewModelProvider INSTANCE = new ChatViewModelProvider();
    }

    public static ChatViewModelProvider getInstance() {
        return ChatViewModelProviderHolder.INSTANCE;
    }

    private ChatViewModelProvider() {
        super(new ViewModelStoreOwner() {
            @NonNull
            @Override
            public ViewModelStore getViewModelStore() {
                return new ViewModelStore();
            }
        });
    }

    @NonNull
    public <T extends ViewModel> T get(@NonNull String className) {
        try {
            return super.get((Class<? extends T>) Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

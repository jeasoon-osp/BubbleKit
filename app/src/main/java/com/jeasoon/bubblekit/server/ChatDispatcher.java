package com.jeasoon.bubblekit.server;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

class ChatDispatcher {

    private static class ChatDispatcherHolder {
        private static final ChatDispatcher INSTANCE = new ChatDispatcher();
    }

    static ChatDispatcher getInstance() {
        return ChatDispatcherHolder.INSTANCE;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Map<ChatSession, Queue<Runnable>> mChatSessionTaskMap = new HashMap<>();
    private Map<ChatSession, ChatMsgReceiver> mChatSessionReceiveRunnableMap = new HashMap<>();
    private ChatMessageExchangeSender mSender;
    private ChatMessageExchangeReceiver mReceiver;
    private ChatMessageExchangeInterceptor mInterceptor;

    void clear() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Queue<Runnable> runnableQueue : mChatSessionTaskMap.values()) {
                    for (Runnable runnable : runnableQueue) {
                        mHandler.removeCallbacks(runnable);
                    }
                }
                mChatSessionTaskMap.clear();
                mChatSessionReceiveRunnableMap.clear();
                mInterceptor = null;
                mReceiver = null;
            }
        });
    }

    void reportSessionDestroyed(ChatSession chatSession) {
        Queue<Runnable> tasks = mChatSessionTaskMap.get(chatSession);
        if (tasks != null) {
            for (Runnable runnable : tasks) {
                mHandler.removeCallbacks(runnable);
            }
        }
        mChatSessionTaskMap.remove(chatSession);
        mChatSessionReceiveRunnableMap.remove(chatSession);
    }

    void postSendMsg(final ChatSession chatSession, final ChatMessage chatMessage, long delay, final boolean isDummy) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mChatSessionTaskMap.get(chatSession)).remove(this);
                if (mInterceptor != null && mInterceptor.onChatMessageSent(chatSession, chatMessage, isDummy)) {
                    return;
                }
                if (mSender != null) {
                    mSender.onSend(ChatDispatcher.this, chatSession, chatMessage, isDummy, mReceiver);
                }
            }
        };
        mHandler.postDelayed(task, delay);
        recordPendingTask(chatSession, task);
    }

    void postReceiveMsg(final ChatSession chatSession, final ChatMessage chatMessage, final boolean isDummy) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mChatSessionTaskMap.get(chatSession)).remove(this);
                if (mInterceptor != null && mInterceptor.onChatMessageSent(chatSession, chatMessage, isDummy)) {
                    return;
                }
                ChatMsgReceiver receiver = mChatSessionReceiveRunnableMap.get(chatSession);
                if (receiver != null) {
                    chatSession.dispatchMessage(receiver, chatMessage);
                }
            }
        };
        mHandler.post(task);
        recordPendingTask(chatSession, task);
    }

    void setChatMsgReceiver(final ChatSession chatSession, final ChatMsgReceiver receiver) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mChatSessionTaskMap.get(chatSession)).remove(this);
                mChatSessionReceiveRunnableMap.put(chatSession, receiver);
            }
        };
        mHandler.post(task);
        recordPendingTask(chatSession, task);
    }

    private void recordPendingTask(ChatSession chatSession, Runnable task) {
        Queue<Runnable> sessionPendingTaskQueue = mChatSessionTaskMap.get(chatSession);
        if (sessionPendingTaskQueue == null) {
            sessionPendingTaskQueue = new LinkedList<>();
            mChatSessionTaskMap.put(chatSession, sessionPendingTaskQueue);
        }
        sessionPendingTaskQueue.add(task);
    }

    void setChatMessageExchangeSender(ChatMessageExchangeSender sender) {
        mSender = sender;
    }

    void setChatMessageExchangeReceiver(ChatMessageExchangeReceiver receiver) {
        mReceiver = receiver;
    }

    void setOnChatMessageSentListener(ChatMessageExchangeInterceptor chatMessageExchangeInterceptor) {
        mInterceptor = chatMessageExchangeInterceptor;
    }

    interface ChatMessageExchangeInterceptor {
        boolean onChatMessageSent(ChatSession chatSession, ChatMessage chatMessage, boolean isDummy);

        boolean onChatMessageReceived(ChatSession chatSession, ChatMessage chatMessage, boolean isDummy);
    }

}

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
        return ChatDispatcher.ChatDispatcherHolder.INSTANCE;
    }

    private Handler mMsgHandler = new Handler(Looper.getMainLooper());
    private Map<ChatSession, Queue<Runnable>> mChatSessionTaskMap = new HashMap<>();
    private Map<ChatSession, Queue<ChatMessage>> mChatSessionMessageMap = new HashMap<>();
    private Map<ChatSession, Queue<ChatMsgReceiveRunnable>> mChatSessionRunnableMap = new HashMap<>();
    private OnChatMessageSentListener mOnChatMessageSentListener;

    void clear() {
        mMsgHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Queue<Runnable> runnableQueue : mChatSessionTaskMap.values()) {
                    for (Runnable runnable : runnableQueue) {
                        mMsgHandler.removeCallbacks(runnable);
                    }
                }
                mChatSessionTaskMap.clear();
                mChatSessionMessageMap.clear();
                mChatSessionRunnableMap.clear();
                mOnChatMessageSentListener = null;
            }
        });
    }

    void reportSessionDestroyed(ChatSession chatSession) {
        Queue<Runnable> tasks = mChatSessionTaskMap.get(chatSession);
        if (tasks != null) {
            for (Runnable runnable : tasks) {
                mMsgHandler.removeCallbacks(runnable);
            }
        }
        mChatSessionTaskMap.remove(chatSession);
        mChatSessionMessageMap.remove(chatSession);
        mChatSessionRunnableMap.remove(chatSession);
    }

    void setOnChatMessageSentListener(OnChatMessageSentListener onChatMessageSentListener) {
        mOnChatMessageSentListener = onChatMessageSentListener;
    }

    void postSendMsg(final ChatSession chatSession, final ChatMessage chatMessage, long delay, final boolean isDummy) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mChatSessionTaskMap.get(chatSession)).remove(this);
                Queue<ChatMessage> queue = mChatSessionMessageMap.get(chatSession);
                if (queue == null) {
                    queue = new LinkedList<>();
                    mChatSessionMessageMap.put(chatSession, queue);
                }
                queue.offer(chatMessage);
                if (mOnChatMessageSentListener != null) {
                    mOnChatMessageSentListener.onChatMessageSent(chatSession, chatMessage, isDummy);
                }
                checkDispatchMessage(chatSession);
            }
        };
        mMsgHandler.postDelayed(task, delay);
        recordPendingTask(chatSession, task);
    }

    void postReceiveMsg(final ChatSession chatSession, final ChatMsgReceiveRunnable runnable) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(mChatSessionTaskMap.get(chatSession)).remove(this);
                Queue<ChatMsgReceiveRunnable> queue = mChatSessionRunnableMap.get(chatSession);
                if (queue == null) {
                    queue = new LinkedList<>();
                    mChatSessionRunnableMap.put(chatSession, queue);
                }
                queue.offer(runnable);
                checkDispatchMessage(chatSession);
            }
        };
        mMsgHandler.post(task);
        recordPendingTask(chatSession, task);
    }

    private void checkDispatchMessage(ChatSession chatSession) {
        Queue<ChatMessage> msgQueue = mChatSessionMessageMap.get(chatSession);
        Queue<ChatMsgReceiveRunnable> runnableQueue = mChatSessionRunnableMap.get(chatSession);
        if (msgQueue == null || runnableQueue == null) {
            return;
        }
        while (true) {
            if (msgQueue.isEmpty() || runnableQueue.isEmpty()) {
                return;
            }
            ChatMessage chatMessage = msgQueue.poll();
            ChatMsgReceiveRunnable chatRunnable = runnableQueue.poll();
            chatSession.dispatchMessage(Objects.requireNonNull(chatRunnable), chatMessage);
        }
    }

    private void recordPendingTask(ChatSession chatSession, Runnable task) {
        Queue<Runnable> sessionPendingTaskQueue = mChatSessionTaskMap.get(chatSession);
        if (sessionPendingTaskQueue == null) {
            sessionPendingTaskQueue = new LinkedList<>();
            mChatSessionTaskMap.put(chatSession, sessionPendingTaskQueue);
        }
        sessionPendingTaskQueue.add(task);
    }

    interface OnChatMessageSentListener {
        void onChatMessageSent(ChatSession chatSession, ChatMessage chatMessage, boolean isDummy);
    }

}

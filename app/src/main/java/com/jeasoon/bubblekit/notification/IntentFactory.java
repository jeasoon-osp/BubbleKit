package com.jeasoon.bubblekit.notification;

class IntentFactory {

    private static class IntentFactoryHolder {
        private static final IntentFactory INSTANCE = new IntentFactory();
    }

    static IntentFactory getInstance() {
        return IntentFactoryHolder.INSTANCE;
    }

}

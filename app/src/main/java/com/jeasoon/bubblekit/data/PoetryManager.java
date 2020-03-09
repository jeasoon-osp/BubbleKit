package com.jeasoon.bubblekit.data;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PoetryManager {

    private int mPoetrySize;
    private int mPoetryIndex;

    private PoetryData mPoetryData;

    private PoetryManager() {
    }

    private static class PoetryManagerHolder {
        private static final PoetryManager INSTANCE = new PoetryManager();
    }

    public static PoetryManager getInstance() {
        return PoetryManagerHolder.INSTANCE;
    }

    public void init(Context context) {
        try {
            mPoetryData = new Gson().fromJson(new InputStreamReader(context.getAssets().open("poet.tang.30000.json")), PoetryData.class);
            mPoetrySize = mPoetryData.data.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Poetry next() {
        if (mPoetryIndex >= mPoetrySize) {
            mPoetryIndex = 0;
        }
        return mPoetryData.data.get(mPoetryIndex++);
    }

    public static class PoetryData {
        private List<Poetry> data;

        public List<Poetry> getData() {
            return data;
        }

        public void setData(List<Poetry> data) {
            this.data = data;
        }

    }

}


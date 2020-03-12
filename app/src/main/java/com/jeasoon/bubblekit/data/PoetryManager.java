package com.jeasoon.bubblekit.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class PoetryManager {

    private int mPoetrySize;
    private int mPoetryIndex;

    private PoetryData mPoetryData;
    private Random mRandom;

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
            mRandom = new Random(System.currentTimeMillis() + System.nanoTime());
            mPoetryData = new Gson().fromJson(new InputStreamReader(context.getAssets().open("poet.tang.30000.json")), PoetryData.class);
            mPoetrySize = mPoetryData.data.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        mPoetrySize = 0;
        mPoetryIndex = 0;
        mRandom = null;
        mPoetryData = null;
    }

    public Poetry next() {
        return nextRandom();
    }

    public Poetry nextRandom() {
        return mPoetryData.data.get(mRandom.nextInt(mPoetrySize));
    }

    public Poetry nextOrdered() {
        if (mPoetryIndex >= mPoetrySize) {
            mPoetryIndex = 0;
        }
        return mPoetryData.data.get(mPoetryIndex++);
    }

    public static class PoetryData {

        @SerializedName("data")
        private List<Poetry> data;

        public List<Poetry> getData() {
            return data;
        }

        public void setData(List<Poetry> data) {
            this.data = data;
        }

    }

}


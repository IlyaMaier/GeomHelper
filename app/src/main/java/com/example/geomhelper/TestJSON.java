package com.example.geomhelper;

import android.util.SparseArray;
import android.util.SparseIntArray;

public class TestJSON {

    SparseArray<SparseIntArray> tests;

    public TestJSON() {
        tests = new SparseArray<>();
    }

    public void setTest(int test, int theme, int stage) {
        SparseIntArray arr = new SparseIntArray();
        arr.put(theme, stage);
        tests.put(test, arr);
    }

    public int getTest(int test, int theme) {
        return tests.get(test).get(theme);
    }

}

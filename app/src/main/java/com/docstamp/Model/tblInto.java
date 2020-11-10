package com.docstamp.Model;

import com.docstamp.R;

public enum tblInto {

    RED("s1", R.layout.screen_one),
    BLUE("s2", R.layout.screen_one),
    GREEN("s3", R.layout.screen_one);

    public String mTitleResId;
    public int mLayoutResId;

    tblInto(String titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

}

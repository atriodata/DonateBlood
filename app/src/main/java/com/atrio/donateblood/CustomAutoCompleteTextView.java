package com.atrio.donateblood;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Arpita Patel on 14-08-2017.
 */

public class CustomAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}

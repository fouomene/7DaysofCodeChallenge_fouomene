package net.freelancertech.journal.app.custom;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Journal Doodle, Copyright (C) 2018 www.fouomene.com.
 *
 * @author FOUOMENE on 06/2018.
 * @EmailAuthor:  fouomenedaniel@gmail.com .
 *
 */
public class CustomTouchListener implements View.OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                ((TextView)view).setTextColor(0xFF2F73FC); //white
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ((TextView)view).setTextColor(0xFF000000); //black
                break;
        }
        return false;
    }
}
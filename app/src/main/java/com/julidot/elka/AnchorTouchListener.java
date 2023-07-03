package com.julidot.elka;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnchorTouchListener implements View.OnTouchListener {
    private int _yDelta;
    private View viewToResize;


    public AnchorTouchListener(View viewToResize ) {
        this.viewToResize = viewToResize;

    }

    private int initialHeight;

    private int initialY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int Y = (int) event.getRawY();

        Log.d("Anchor", "Updating X & Y");

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // Capture initial conditions of the view to resize.
                initialHeight = viewToResize.getHeight();

                initialY = Y;
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                // Compute how far we have moved in the X/Y directions.
                _yDelta = Y - initialY;
                RelativeLayout.LayoutParams lp =
                        (RelativeLayout.LayoutParams) viewToResize.getLayoutParams();

                if(initialHeight -_yDelta <= 930){
                    break;
                }
                else{

                    lp.height = initialHeight - _yDelta;
                    viewToResize.setLayoutParams(lp);
                    break;
                }



        }
        return true;
    }
}

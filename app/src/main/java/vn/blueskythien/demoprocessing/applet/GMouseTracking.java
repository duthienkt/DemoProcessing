package vn.blueskythien.demoprocessing.applet;

import android.view.MotionEvent;
import android.view.View;

import static vn.blueskythien.demoprocessing.applet.MathUtils.distance;


/**
 * Created by DuThien on 30/12/2016.
 */

public class GMouseTracking implements View.OnTouchListener {
    public interface GMouseTrackingListener {
        boolean onMousePressed(GMouseTracking tracking, int mouseId);

        boolean onMouseRelease(GMouseTracking tracking, int mouseId);

        void onMouseMove(GMouseTracking tracking, int mouseId);
    }

    private int mouseCount = 0;
    private final GMouseTrackingListener listener;
    public static final int MAX_MOUSE_SUPPORT = 5;

    public int getMouseCount() {
        return mouseCount;
    }

    public GMouse getMouses(int id) {
        return gMouses[id];
    }

    private GMouse[] gMouses = new GMouse[MAX_MOUSE_SUPPORT];

    public GMouseTracking(GMouseTrackingListener listener) {
        this.listener = listener;
        for (int i = 0; i< MAX_MOUSE_SUPPORT; ++i)
            gMouses[i] = new GMouse();
    }

    @Override
    final public boolean onTouch(View v, MotionEvent event) {
        mouseCount = event.getPointerCount();
        if (event.getAction() == MotionEvent.ACTION_DOWN || (event.getAction() & 0xFF) == MotionEvent.ACTION_POINTER_DOWN) {
            int mouseId = event.getPointerId(event.getActionIndex());
            int pointerIndex = event.findPointerIndex(mouseId);
            gMouses[mouseId].setX(event.getX(pointerIndex));
            gMouses[mouseId].setY(event.getY(pointerIndex));
            gMouses[mouseId].setPressed(true);
            listener.onMousePressed(this, mouseId);
        } else if (event.getAction() == MotionEvent.ACTION_UP || (event.getAction() & 0xFF) == MotionEvent.ACTION_POINTER_UP) {
            int mouseId = event.getPointerId(event.getActionIndex());
            int pointerIndex = event.findPointerIndex(mouseId);
            gMouses[mouseId].setX(event.getX(pointerIndex));
            gMouses[mouseId].setY(event.getY(pointerIndex));
            gMouses[mouseId].setPressed(false);
            listener.onMouseRelease(this, mouseId);

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < event.getPointerCount(); ++i) {
                int mouseId = event.getPointerId(i);
                int pointerIndex = event.findPointerIndex(mouseId);
                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                if (distance(x, y, gMouses[mouseId].getX(), gMouses[mouseId].getY()) > 1.4f) {
                    gMouses[mouseId].setX(x);
                    gMouses[mouseId].setY(y);
                    listener.onMouseMove(this, mouseId);
                }
            }

        }
        return false;
    }


    public static class GMouse {
        private float X, Y, pX, pY;
        private boolean pressed = false;

        public boolean isPressed() {
            return pressed;
        }

        public float getPreviousX() {
            return pX;
        }

        public float getPreviousY() {
            return pY;
        }

        public float getY() {
            return Y;
        }

        public float getX() {
            return X;
        }

        public void setX(float x) {
            pX = X;
            X = x;
        }

        public void setY(float y) {
            pY = Y;
            Y = y;
        }

        private void setPressed(boolean isPressed) {
            if (isPressed == pressed) return;
            this.pressed = isPressed;
            pX = X;
            pY = Y;
        }
    }
}

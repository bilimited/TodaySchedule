package com.example.todayschedule.tool;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 这个工具可以使任何一个view进行拖动或点击。
 */
public class DragViewUtil {
    public static void registerDragAction(View v) {
        registerDragAction(v, 0);
    }

    /**
     * 拖动View方法
     * @param v     view
     * @param delay 延迟
     */
    public static void registerDragAction(View v, long delay) {
        v.setOnTouchListener(new TouchListener(delay));
    }

    /**
     * 拖动或点击View方法
     * @param v
     * @param onClickListener 点击事件
     */
    public static void registerDragAction(View v, View.OnClickListener onClickListener) {
        TouchListener touchListener = new TouchListener(0);
        touchListener.setOnClickListener(onClickListener);
        v.setOnTouchListener(touchListener);
    }

    private static class TouchListener implements View.OnTouchListener {
        private float downX;
        private float downY;
        private float moveX;
        private float moveY;
        private long downTime;
        private long delay;
        private boolean isMove;
        private boolean canDrag;

        private TouchListener() {
            this(0);
        }

        private TouchListener(long delay) {
            this.delay = delay;
        }

        private boolean haveDelay() {
            return delay > 0;
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","点击这里并不会发生任何事。");
            }
        };

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    moveX = moveY = 0;
                    isMove = false;
                    downTime = System.currentTimeMillis();
                    if (haveDelay()) {
                        canDrag = false;
                    } else {
                        canDrag = true;
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (haveDelay() && !canDrag) {
                        long currMillis = System.currentTimeMillis();
                        if (currMillis - downTime >= delay) {
                            canDrag = true;
                        }
                    }
                    if (!canDrag) {
                        break;
                    }
                    final float xDistance = event.getX() - downX;
                    final float yDistance = event.getY() - downY;
                    moveX = xDistance;moveY = yDistance;
                    if (xDistance != 0 && yDistance != 0) {
                        int l = (int) (v.getLeft() + xDistance);
                        int r = (int) (l + v.getWidth());
                        int t = (int) (v.getTop() + yDistance);
                        int b = (int) (t + v.getHeight());
//                        v.layout(l, t, r, b);
                        v.setLeft(l);
                        v.setTop(t);
                        v.setRight(r);
                        v.setBottom(b);
                        isMove = true;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    long moveTime = System.currentTimeMillis() - downTime;
                    if (moveTime < 300 && moveX < 20 && moveY < 20) {//点击事件
                        onClickListener.onClick(v);
                        return false;
                    } else {//滑动事件
                        return true;//返回true,表示不再执行后面的事件
                    }
                }
                default:
                    break;
            }
            return true;
        }

    }
}
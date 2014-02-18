package wingman.dodger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class Dodger {

    private Context mContext;
    private Bitmap mView;
    private int xCoord;
    private int yCoord;

    private int mScreenWidth;
    private int mScreenHeight;

    private int mPosition;

    public Dodger(Context mContext, int mDrawable, int yCoord) {
        this.mView = BitmapFactory.decodeResource(mContext.getResources(), mDrawable);
        this.yCoord = yCoord;
        this.mContext = mContext;
        initX();
    }

    private void initX() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
        int mViewWidth = mView.getWidth();
        xCoord = mScreenWidth / 2 - (mViewWidth / 2);
        mPosition = 2;
    }

    public Bitmap getView() {
        return this.mView;
    }

    public int getViewWidth() {
        return this.mView.getWidth();
    }

    public int getViewHeight() {
        return this.mView.getHeight();
    }

    public int getxCoord() {
        return this.xCoord;
    }

    public int getyCoord() {
        return this.yCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void move (boolean mRight) {

        if (mRight) {
            if(mPosition == 2) {

            }
        }


    }

    public void resetCoords() {
        initX();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(mView, xCoord, yCoord, null);
    }

    public void onTouch(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.d("sdgfg", "movemove");
                setxCoord((int) event.getX());
                break;
        }
    }
}

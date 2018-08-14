package md.sjd.androidutils;

/**
 * Created by Sajjad Mistri on 21-02-2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * A RelativeLayout that will always be aspect width and height,
 * where the height is based off the width.
 */
public class AspectRelativeLayout extends RelativeLayout {

    private int widthMeasureAspect = 9;
    private int heightMeasureAspect = 16;

    public AspectRelativeLayout(Context context) {
        super(context);
    }

    public AspectRelativeLayout(Context context, int widthMeasureAspect, int heightMeasureAspect) {
        super(context);
        this.widthMeasureAspect = widthMeasureAspect;
        this.heightMeasureAspect = heightMeasureAspect;
    }

    public AspectRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRelativeLayout(Context context, AttributeSet attrs, int widthMeasureAspect, int heightMeasureAspect) {
        super(context, attrs);
        this.widthMeasureAspect = widthMeasureAspect;
        this.heightMeasureAspect = heightMeasureAspect;
    }

    public AspectRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int widthMeasureAspect, int heightMeasureAspect) {
        super(context, attrs, defStyleAttr);
        this.widthMeasureAspect = widthMeasureAspect;
        this.heightMeasureAspect = heightMeasureAspect;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int widthMeasureAspect, int heightMeasureAspect) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.widthMeasureAspect = widthMeasureAspect;
        this.heightMeasureAspect = heightMeasureAspect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, (widthMeasureSpec * widthMeasureAspect) / heightMeasureAspect);
    }
}
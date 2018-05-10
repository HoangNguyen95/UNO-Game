package au.edu.federation.unogame;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

//this class purpose is to create a square text view so it look similar to a Uno card. Just make
// it more interesting
public class SquareTextView extends AppCompatTextView {

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpace, int heightSpace) {
        super.onMeasure(widthSpace, heightSpace);

        int max = Math.max(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(max, max);
    }
}

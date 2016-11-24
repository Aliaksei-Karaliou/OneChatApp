package com.github.aliakseiKaraliou.onechatapp.ui.recyclerView.itemDecorations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DialogListItemDecorations extends RecyclerView.ItemDecoration {
    private Bitmap separator;

    public DialogListItemDecorations(int colour, int width) {
        Canvas canvas = new Canvas();
        canvas.setBitmap(separator);
        Paint paint = new Paint();
        paint.setColor(colour);
        canvas.drawLine(0f, 0f, 0f, (float) width, paint);
        new StringBuilder();
    }

    private Bitmap divider;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

    }
}

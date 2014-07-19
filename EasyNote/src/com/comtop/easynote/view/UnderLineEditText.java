package com.comtop.easynote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class UnderLineEditText extends EditText {
	
	private Paint linePaint;
	private int paperColor;

	public UnderLineEditText(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		// TODO Auto-generated constructor stub
		this.linePaint = new Paint();
		linePaint.setColor(Color.GRAY);// 设置下划线颜色
	}

	protected void onDraw(Canvas paramCanvas) {
		paramCanvas.drawColor(this.paperColor); // 设置背景色
		int i = getLineCount();
		int j = getHeight();
		int k = getLineHeight();
		int m = 1 + j / k;
		if (i < m)
			i = m;
		int n = getCompoundPaddingTop();

		int distance_with_btm = (int) (getLineHeight() - getTextSize()) - 3;
		// 这个关于距离底部的变量当不使用lineSpacingMultiplier和lineSpacingExtra参数时是不起作用的

		for (int i2 = 0;; i2++) {
			if (i2 >= i) {
				super.onDraw(paramCanvas);
				paramCanvas.restore();
				return;
			}

			n += k;
			n -= distance_with_btm;// 将线划在字体靠下面
			paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
			paramCanvas.save();
			n += distance_with_btm;// 还原n
		}
	}

}

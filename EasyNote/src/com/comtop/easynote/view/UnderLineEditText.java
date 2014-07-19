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
		linePaint.setColor(Color.GRAY);// �����»�����ɫ
	}

	protected void onDraw(Canvas paramCanvas) {
		paramCanvas.drawColor(this.paperColor); // ���ñ���ɫ
		int i = getLineCount();
		int j = getHeight();
		int k = getLineHeight();
		int m = 1 + j / k;
		if (i < m)
			i = m;
		int n = getCompoundPaddingTop();

		int distance_with_btm = (int) (getLineHeight() - getTextSize()) - 3;
		// ������ھ���ײ��ı�������ʹ��lineSpacingMultiplier��lineSpacingExtra����ʱ�ǲ������õ�

		for (int i2 = 0;; i2++) {
			if (i2 >= i) {
				super.onDraw(paramCanvas);
				paramCanvas.restore();
				return;
			}

			n += k;
			n -= distance_with_btm;// ���߻������忿����
			paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
			paramCanvas.save();
			n += distance_with_btm;// ��ԭn
		}
	}

}

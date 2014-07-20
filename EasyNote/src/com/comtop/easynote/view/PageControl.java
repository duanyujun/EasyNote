package com.comtop.easynote.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;

import com.comtop.easynote.R;

public class PageControl extends LinearLayout
{
  private Drawable activeDrawable;
  private Drawable inactiveDrawable;
  private ArrayList<ImageView> indicators;
  private Context mContext;
  private int mCurrentPage = 0;
  private int mIndicatorSize = 10;
  private OnPageControlClickListener mOnPageControlClickListener = null;
  private int mPageCount = 0;

  public PageControl(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    initPageControl();
  }

  public PageControl(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  private void initPageControl()
  {
    this.indicators = new ArrayList();
    this.activeDrawable = this.mContext.getResources().getDrawable(R.drawable.round02);
    this.inactiveDrawable = this.mContext.getResources().getDrawable(R.drawable.round01);
    //int[] arrayOfInt = { 16842808, 16842810 };
    //this.mContext.getTheme().obtainStyledAttributes(arrayOfInt);
    this.mIndicatorSize = ((int)(this.mIndicatorSize * getResources().getDisplayMetrics().density));
    setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (PageControl.this.mOnPageControlClickListener != null);
        switch (paramAnonymousMotionEvent.getAction())
        {
        default:
          return true;
        case 1:
        }
        if (PageControl.this.getOrientation() == 0)
          if (paramAnonymousMotionEvent.getX() < PageControl.this.getWidth() / 2)
            if (PageControl.this.mCurrentPage > 0)
              PageControl.this.mOnPageControlClickListener.goBackwards();
        while (true)
        {
          
          if (PageControl.this.mCurrentPage < -1 + PageControl.this.mPageCount)
          {
            PageControl.this.mOnPageControlClickListener.goForwards();
            if (paramAnonymousMotionEvent.getY() < PageControl.this.getHeight() / 2)
            {
              if (PageControl.this.mCurrentPage > 0)
                PageControl.this.mOnPageControlClickListener.goBackwards();
            }
            else if (PageControl.this.mCurrentPage < -1 + PageControl.this.mPageCount)
              PageControl.this.mOnPageControlClickListener.goForwards();
            continue;
          }
          return false;
        }
      }
    });
  }

  public Drawable getActiveDrawable()
  {
    return this.activeDrawable;
  }

  public int getCurrentPage()
  {
    return this.mCurrentPage;
  }

  public Drawable getInactiveDrawable()
  {
    return this.inactiveDrawable;
  }

  public int getIndicatorSize()
  {
    return this.mIndicatorSize;
  }

  public OnPageControlClickListener getOnPageControlClickListener()
  {
    return this.mOnPageControlClickListener;
  }

  public int getPageCount()
  {
    return this.mPageCount;
  }

  protected void onFinishInflate()
  {
    initPageControl();
  }

  public void setActiveDrawable(Drawable paramDrawable)
  {
    this.activeDrawable = paramDrawable;
    ((ImageView)this.indicators.get(this.mCurrentPage)).setBackgroundDrawable(this.activeDrawable);
  }

  public void setCurrentPage(int paramInt)
  {
    if (paramInt < this.mPageCount)
    {
      ((ImageView)this.indicators.get(this.mCurrentPage)).setBackgroundDrawable(this.inactiveDrawable);
      ((ImageView)this.indicators.get(paramInt)).setBackgroundDrawable(this.activeDrawable);
      this.mCurrentPage = paramInt;
    }
  }

  public void setInactiveDrawable(Drawable paramDrawable)
  {
    this.inactiveDrawable = paramDrawable;
    for (int i = 0; ; i++)
    {
      if (i >= this.mPageCount)
      {
        ((ImageView)this.indicators.get(this.mCurrentPage)).setBackgroundDrawable(this.activeDrawable);
        return;
      }
      ((ImageView)this.indicators.get(i)).setBackgroundDrawable(this.inactiveDrawable);
    }
  }

  public void setIndicatorSize(int paramInt)
  {
    this.mIndicatorSize = paramInt;
    for (int i = 0; ; i++)
    {
      if (i >= this.mPageCount)
        return;
      ((ImageView)this.indicators.get(i)).setLayoutParams(new LinearLayout.LayoutParams(this.mIndicatorSize, this.mIndicatorSize));
    }
  }

  public void setOnPageControlClickListener(OnPageControlClickListener paramOnPageControlClickListener)
  {
    this.mOnPageControlClickListener = paramOnPageControlClickListener;
  }

  public void setPageCount(int paramInt)
  {
    this.mPageCount = paramInt;
    for (int i = 0; ; i++)
    {
      if (i >= paramInt)
        return;
      ImageView localImageView = new ImageView(this.mContext);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(this.mIndicatorSize, this.mIndicatorSize);
      localLayoutParams.setMargins(this.mIndicatorSize / 2, this.mIndicatorSize, this.mIndicatorSize / 2, this.mIndicatorSize);
      localImageView.setLayoutParams(localLayoutParams);
      localImageView.setBackgroundDrawable(this.inactiveDrawable);
      this.indicators.add(localImageView);
      addView(localImageView);
    }
  }

  public static abstract interface OnPageControlClickListener
  {
    public abstract void goBackwards();

    public abstract void goForwards();
  }
}
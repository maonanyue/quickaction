package vn.com.enclaveit.phatbeo.quickaction;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.content.Context;
import android.content.res.Resources.Theme;

/**
 * Make a base class for our quick action implementation and for other types of quick actions you 
 * would want to implement. What this class does is basically create a floating window on the screen
 * at certain coordinates. Its main component is the PopupWindow class from the Android framework.
 *  
 * @author Phat (Phillip) H. VU <vuhongphat@hotmail.com>
 *
 */
public class PopupWindows {
	protected Context mContext;
	protected PopupWindow mWindow;
	protected View mRootView;
	protected Drawable mBackground = null;
	protected WindowManager mWindowManager;

	public PopupWindows(Context context) {
		mContext = context;
		mWindow = new PopupWindow(context);
		

		mWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mWindow.dismiss();
					return true;
				}
				return false;
			}
		});

		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	protected void onDismiss() {
	}

	protected void onShow() {
	}

	protected void preShow() {
		if (mRootView == null)
			throw new IllegalStateException("setContentView was not called with a view to display.");

		onShow();

		if (mBackground == null)
			mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//new BitmapDrawable()
		else
			mWindow.setBackgroundDrawable(mBackground);

		mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);

		mWindow.setContentView(mRootView);
	}

	public void setBackgroundDrawable(Drawable background) {
		mBackground = background;
	}

	public void setContentView(View root) {
		mRootView = root;
		mWindow.setContentView(root);
	}

	public void setContentView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setContentView(inflator.inflate(layoutResID, null));
	}

	public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);
	}

	public void dismiss() {
		mWindow.dismiss();
	}
}
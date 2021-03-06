package vn.com.enclaveit.phatbeo.quickaction;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class VerticalQuickAction extends QuickAction {
    private final static String TAG="VerticalQuickAction";
	public VerticalQuickAction(Context context) {
		super(context);		
	}
	
	@Override
	protected int getRootViewResId(){
		return R.layout.vertical_quickaction;
	}

	@Override
	protected int getActItemViewResId(){
		return R.layout.vertical_action_item;
	}
	
	@Override
	protected Point getShowPosition(View anchor, Rect anchorRect){
		Point position = new Point();

		int rootWidth = mRootView.getMeasuredWidth();
		int rootHeight = mRootView.getMeasuredHeight();

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

		int xPos = anchorRect.right;
		int yPos = anchorRect.top;

		if (xPos + rootWidth > screenWidth) {
			xPos = screenWidth - rootWidth;
		}
		if (yPos + rootHeight > screenHeight) {
			yPos = screenHeight - rootHeight;
		}
		
		position.x = xPos;
		position.y = yPos;
		return position;
	}
	@Override
	protected void showArrow(int whichArrow, int requestedX){
		this.mArrowDown.setVisibility(View.GONE);
		this.mArrowUp.setVisibility(View.GONE);
	}

}

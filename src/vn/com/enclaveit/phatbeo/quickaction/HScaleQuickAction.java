package vn.com.enclaveit.phatbeo.quickaction;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class HScaleQuickAction extends VScaleQuickAction {

	private int leftMargin = MARGIN;
	public HScaleQuickAction(Context context) {
		super(context);

	}
	
	@Override
	protected int getActItemViewResId(){
		return R.layout.action_item;
	}

	@Override
	protected ViewGroup.LayoutParams getItemLayoutParams(ActionItem action, View container){
		container.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		FrameLayout.LayoutParams itemParams = 
				new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemParams.topMargin = MARGIN;
		itemParams.leftMargin = leftMargin;
		itemParams.rightMargin = MARGIN;
		itemParams.bottomMargin = MARGIN;
		leftMargin += container.getMeasuredWidth();
		container.setOnFocusChangeListener(focusListerner);
		return itemParams;
	}

}

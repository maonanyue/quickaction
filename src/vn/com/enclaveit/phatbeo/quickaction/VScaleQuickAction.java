package vn.com.enclaveit.phatbeo.quickaction;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class VScaleQuickAction extends VerticalQuickAction {
	protected final static int MARGIN=24;
	protected final static int PADDING=4;
	private int topMargin=MARGIN;
	private FrameLayout dumpTracks;
	private FrameLayout tracks;
	public VScaleQuickAction(Context context) {
		super(context);
	}
	
	@Override
	protected int getRootViewResId(){
		return R.layout.vscale_quickaction;
	}
	
	@Override
	protected ViewGroup getRootView(){
		FrameLayout fl = (FrameLayout) getInflater().inflate(getRootViewResId(), null);
		
		dumpTracks = (FrameLayout) fl.findViewById(R.id.dump_tracks);
		tracks  = (FrameLayout) fl.findViewById(R.id.tracks);
		fl.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		fl.layout(0, 0, fl.getMeasuredWidth(), fl.getMeasuredHeight());
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tracks.getLayoutParams();
		lp.topMargin = dumpTracks.getTop()-1;		
		lp.leftMargin = dumpTracks.getLeft();
		RelativeLayout background = (RelativeLayout) fl.findViewById(R.id.background);
		lp = (FrameLayout.LayoutParams) background.getLayoutParams();
		lp.leftMargin = MARGIN - 1;
		lp.topMargin = MARGIN;
		LayoutParams dumpLp = dumpTracks.getLayoutParams();
		dumpLp.height = 0;
		dumpLp.width = 0;
		return fl;
	}
	
	protected ViewGroup.LayoutParams getItemLayoutParams(ActionItem action, View container){
		container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		FrameLayout.LayoutParams itemParams = 
				new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemParams.topMargin = topMargin;
		itemParams.leftMargin = MARGIN;
		itemParams.rightMargin = MARGIN;
		itemParams.bottomMargin = MARGIN;
		topMargin += container.getMeasuredHeight() + PADDING;
		container.setOnFocusChangeListener(focusListerner);
		LayoutParams dumpLp = dumpTracks.getLayoutParams();
		dumpLp.height += container.getMeasuredHeight() + PADDING;
		if(dumpLp.width < container.getMeasuredWidth())
			dumpLp.width = container.getMeasuredWidth()+2;
		return itemParams;
	}
	
	protected View.OnFocusChangeListener focusListerner = new View.OnFocusChangeListener(){

		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.bringToFront();
				v.startAnimation(createOnFocusAnimation());
			} else {
				v.startAnimation(createLoseFocusAnimation());
			}
		}		
	};
	
	protected void startAnimSetXY(View v, float from, float to){
		ObjectAnimator animX = ObjectAnimator.ofFloat(v, "scaleX", from, to);
		ObjectAnimator animY = ObjectAnimator.ofFloat(v, "scaleY", from, to);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.playTogether(animX, animY);
		animSetXY.start();
	}
	
	private Animation createLoseFocusAnimation() {
		return createScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f);
	}

	private Animation createOnFocusAnimation() {
		return createScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f);
	}

	private ScaleAnimation createScaleAnimation(float fromX, float toX, float fromY, float toY) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setDuration(100);
		return scaleAnimation;
	}
}

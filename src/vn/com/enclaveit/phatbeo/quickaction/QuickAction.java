package vn.com.enclaveit.phatbeo.quickaction;

import android.content.Context;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import vn.com.enclaveit.phatbeo.quickaction.R;

public class QuickAction extends PopupWindows {
	private final static String TAG="QuickAction";
	protected ImageView mArrowUp;
	protected ImageView mArrowDown;
	private Animation mTrackAnim;
	private LayoutInflater inflater;
	protected ViewGroup mTrack;
	private OnActionItemClickListener mListener;

	private int animStyle;
	private int mChildPos;
	private boolean animateTrack;
	private boolean hasSlider;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_AUTO = 4;

	public QuickAction(Context context) {
		super(context);

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.rail);

		mTrackAnim.setInterpolator(new Interpolator() {
			public float getInterpolation(float t) {
				final float inner = (t * 1.55f) - 1.1f;

				return 1.2f - inner * inner;
			}
		});

		createRootView();

		animStyle = ANIM_AUTO;
		animateTrack = true;
		mChildPos = 0;
	}
	
	public LayoutInflater getInflater(){
		return inflater;
	}
	
	protected int getRootViewResId(){
		return R.layout.quickaction;
	}
	
	protected int getActItemViewResId(){
		return R.layout.action_item;
	}

	private void createRootView() {
		mRootView = getRootView();
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);
		View slider = mTrack.findViewById(R.id.slider_start);
		if(slider != null){
			hasSlider = true;
		}else{
			hasSlider = false;
		}

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		setContentView(mRootView);
	}
	
	protected ViewGroup getRootView(){
		return (ViewGroup) inflater.inflate(getRootViewResId(), null);
	}

	public void animateTrack(boolean animateTrack) {
		this.animateTrack = animateTrack;
	}

	public void setAnimStyle(int animStyle) {
		this.animStyle = animStyle;
	}

	public void addActionItem(ActionItem action) {		
		View container = createItemView(action);
		ViewGroup.LayoutParams params = getItemLayoutParams(action, container);
		if(params == null)
			mTrack.addView(container, mChildPos + (hasSlider ? 1 : 0));
		else
			mTrack.addView(container, mChildPos + (hasSlider ? 1 : 0), params);

		mChildPos++;
	}
	
	private View createItemView(final ActionItem action) {
		View container = getActionItemView(action);

		final int pos = mChildPos;

		container.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mListener != null)
					mListener.onItemClick(pos, action);

				dismiss();
			}
		});

		container.setFocusable(true);
		container.setClickable(true);
		return container;		
	}
	
	protected ViewGroup.LayoutParams getItemLayoutParams(ActionItem action, View container){
		return null;
	}
	
	protected View getActionItemView(final ActionItem action){
		String title = action.getTitle();
		Drawable icon = action.getIcon();

		View container = (View) inflater.inflate(getActItemViewResId(), null);

		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);
		 
		if (icon != null)
			img.setImageDrawable(icon);
		else
			img.setVisibility(View.GONE);

		if (title != null)
			text.setText(title);
		else
			text.setVisibility(View.GONE);
		
		return container;
	}

	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mListener = listener;
	}

	public void show(View anchor) {
		preShow();
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1] + anchor.getHeight());
		Point pos = getShowPosition(anchor, anchorRect);
		Log.d(TAG, ",anchorRect="+anchorRect
				+",pos="+pos);

		boolean onTop = true;
		if (pos.y <= anchor.getTop()) {
			onTop = false;
		}

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up),
				anchorRect.centerX());

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y);

		if (animateTrack)
			mTrack.startAnimation(mTrackAnim);
	}
	
	protected Point getShowPosition(View anchor, Rect anchorRect){
		Point position = new Point();

		int rootWidth = mRootView.getMeasuredWidth();
		int rootHeight = mRootView.getMeasuredHeight();

		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();

		int xPos = (screenWidth - rootWidth) / 2;
		int yPos = anchorRect.top - rootHeight;

		if (rootHeight > anchor.getTop()) {
			yPos = anchorRect.bottom;
		}
		position.x = xPos;
		position.y = yPos;
		return position;
	}

	private void setAnimationStyle(int screenWidth, int requestedX,
			boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

		switch (animStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
					: R.style.Animations_PopDownMenu_Left);
			break;

		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
					: R.style.Animations_PopDownMenu_Right);
			break;

		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
					: R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
						: R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4
					&& arrowPos < 3 * (screenWidth / 4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
						: R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopDownMenu_Right
						: R.style.Animations_PopDownMenu_Right);
			}

			break;
		}
	}

	protected void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
				: mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
				: mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();

		showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
				.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);
	}

	public interface OnActionItemClickListener {
		public abstract void onItemClick(int pos, ActionItem item);
	}
}
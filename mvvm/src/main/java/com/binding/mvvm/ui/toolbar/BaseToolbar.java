package com.binding.mvvm.ui.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binding.mvvm.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

public class BaseToolbar extends Toolbar {

    @IntDef({TITLE_ELLIPSIS, SUBTITLE_ELLIPSIS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EllipsisMode {
    }

    public static final int TITLE_ELLIPSIS = 0;//缩略主标题
    public static final int SUBTITLE_ELLIPSIS = 1;//缩略副标题

    private int mEllipsisMode;//缩略模式

    private Context mContext;
    private View mStatusBar;//状态栏
    private View mBackView;//返回按钮
    private View mBottomDivider;//状态栏底部分割线
    private LinearLayout mRootView;//根部局
    private TextView mTitleTextView;//标题
    private FrameLayout mLayoutCenter;//中心布局
    private LinearLayout mLayoutLeft, mLayoutRight;//左右布局
    private int mSubTextColorId = Color.BLACK;//副标题文本颜色
    private float mSubTextSize = 16;//副标题文本字体大小 sp


    public BaseToolbar(Context context) {
        this(context, null);
    }

    public BaseToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        //toolbar默认marginLeft ，所以定位到(0,0) 防止偏移
        this.setContentInsetsAbsolute(0, 0);
        this.mContext = context;
        View view = inflate(context, R.layout.base_toolbar_layout, this);
        mStatusBar = view.findViewById(R.id.mStatusBar);
        mBottomDivider = view.findViewById(R.id.mBottomDivider);
        mRootView = view.findViewById(R.id.mRootView);
        mTitleTextView = view.findViewById(R.id.mTitleTextView);
        mLayoutLeft = view.findViewById(R.id.mLayoutLeft);
        mLayoutRight = view.findViewById(R.id.mLayoutRight);
        mLayoutCenter = view.findViewById(R.id.mLayoutCenter);
    }

    /**
     * 根部局
     */
    public ViewGroup getRootView() {
        return mRootView;
    }


    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int result = 0; //获取状态栏高度的资源id
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 显示状态栏 ((此功能需配合沉浸式API>=19才会生效))
     * 默认为透明，保持和toolbar一样的颜色
     */
    public void setStatusBarTransparent() {
        setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 显示状态栏颜色 (此功能需配合沉浸式API>=19才会生效)
     *
     * @param colorId 状态栏颜色
     */
    public void setStatusBarColor(@ColorInt int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//android 19 4.4 以上才支持沉浸式
            mStatusBar.setVisibility(VISIBLE);
            mStatusBar.setBackgroundColor(colorId);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
            params.height = getStatusBarHeight();
        }
    }

    /**
     * 隐藏 statusBar
     */
    public void hideStatusBar() {
        mStatusBar.setVisibility(GONE);
    }

    /**
     * toolbar 和布局的分割线 (默认不显示)
     */
    public void setBottomDivider(@ColorInt int colorId, int height) {
        mBottomDivider.setVisibility(VISIBLE);
        mBottomDivider.setBackgroundColor(colorId);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBottomDivider.getLayoutParams();
        params.height = height;
    }

    /**
     * 隐藏 BottomDivider
     */
    public void hideBottomDivider() {
        mBottomDivider.setVisibility(GONE);
    }


    public void setBackButton(@DrawableRes int resId) {
        setBackButton(resId, (OnClickListener) null);
    }

    /**
     * 设置返回按钮 限制只能添加一个BackButton
     */
    public void setBackButton(@DrawableRes int resId, OnClickListener onClickListener) {
        if (resId == 0) {
            return;
        }
        if (mBackView != null) removeLeftView(mBackView);
        //返回键 默认调用onBackPressed
        if (onClickListener == null) {
            onClickListener = v -> {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();//调用activity的返回键
                }
            };
        }
        mBackView = createImageMenu(mContext, resId, onClickListener);
        int padding = dp2px(mContext, 12);
        mBackView.setPadding(padding * 2, 0, padding, 0);

        addLeftView(mBackView);
    }

    /**
     * 带有 图标和 文字 二种可选的 返回按钮 文字 颜色 大小 默认为
     */
    public void setBackButton(@DrawableRes int resId, CharSequence text) {
        setBackButton(resId, text, mSubTextColorId, mSubTextSize);
    }

    /**
     * 带有 图标和 文字 二种可选的 返回按钮
     */
    public void setBackButton(@DrawableRes int resId, CharSequence text, int textColorId, float textSize) {
        if (resId == 0) {
            return;
        }

        if (mBackView != null) removeLeftView(mBackView);//限制只能添加一个BackButton
        mBackView = createBackLayout(mContext, resId, text, textColorId, textSize, v -> {
            if (getContext() instanceof Activity)
                ((Activity) getContext()).onBackPressed();//调用activity的返回键
        });

        int padding = dp2px(mContext, 10);
        mBackView.setPadding(0, 0, padding, 0);

        addLeftView(mBackView);
    }


    /**
     * 隐藏返回按钮
     */
    public void hideBackButton() {
        if (mBackView != null) {
            mBackView.setVisibility(GONE);
        }
    }


    /**
     * 添加左边View
     */
    public void addLeftView(View view) {
        mLayoutLeft.addView(view);
    }

    public void addLeftView(View view, int index) {
        mLayoutLeft.addView(view, index);
    }

    public void addLeftView(View view, ViewGroup.LayoutParams params) {
        mLayoutLeft.addView(view, params);
    }

    public void addLeftView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutLeft.addView(view, index, params);
    }

    /**
     * 移除左边View
     */
    public void removeLeftView(View view) {
        mLayoutLeft.removeView(view);
    }

    public void removeLeftView(int index) {
        mLayoutLeft.removeViewAt(index);
    }

    public void removeAllLeftView() {
        mLayoutLeft.removeAllViews();
    }

    /**
     * 显示左边图形菜单按钮
     */
    public void addLeftImage(@DrawableRes int resId, OnClickListener listener) {
        ImageView imageMenu = createImageMenu(mContext, resId, listener);
        imageMenu.setImageResource(resId);
        imageMenu.setOnClickListener(listener);
        addLeftView(imageMenu);
    }

    /**
     * 显示左边文本菜单按钮
     */
    public void addLeftText(CharSequence text, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, mSubTextColorId, mSubTextSize, listener);
        addLeftView(textMenu);
    }

    public void addLeftText(CharSequence text, @ColorInt int colorId, float textSize, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, colorId, textSize, listener);
        addLeftView(textMenu);
    }

    public void addLeftText(@StringRes int resId, OnClickListener listener) {
        addLeftText(getContext().getText(resId), listener);
    }

    public void addLeftText(@StringRes int resId, @ColorInt int colorId, int textSize, OnClickListener listener) {
        addLeftText(getContext().getText(resId), colorId, textSize, listener);
    }

    public void addRightView(View view) {
        mLayoutRight.addView(view);
    }

    public void addRightView(View view, int index) {
        mLayoutRight.addView(view, index);
    }

    public void addRightView(View view, ViewGroup.LayoutParams params) {
        mLayoutRight.addView(view, params);
    }

    public void addRightView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutRight.addView(view, index, params);
    }

    public void removeRightView(View view) {
        mLayoutRight.removeView(view);
    }

    public void removeRightView(int index) {
        mLayoutRight.removeViewAt(index);
    }

    public void removeAllRightView() {
        mLayoutRight.removeAllViews();
    }

    public void addRightText(CharSequence text, @ColorInt int colorId, float textSize, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, colorId, textSize, listener);
        addRightView(textMenu);
    }

    public void addRightText(CharSequence text, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, mSubTextColorId, mSubTextSize, listener);
        addRightView(textMenu);
    }

    public void addRightText(@StringRes int text, OnClickListener listener) {
        addRightText(getContext().getText(text), listener);
    }

    public void addRightText(@StringRes int text, @ColorInt int colorId, float textSize, OnClickListener listener) {
        addRightText(getContext().getText(text), colorId, textSize, listener);
    }

    public void addRightImage(@DrawableRes int resId, OnClickListener listener) {
        ImageView imageMenu = createImageMenu(mContext, resId, listener);
        imageMenu.setImageResource(resId);
        imageMenu.setOnClickListener(listener);
        addRightView(imageMenu);
    }

    public void addCenterView(View view) {
        mLayoutCenter.addView(view);
    }

    public void addCenterView(View view, int index) {
        mLayoutCenter.addView(view, index);

    }

    public void addCenterView(View view, ViewGroup.LayoutParams params) {
        mLayoutCenter.addView(view, params);
    }

    public void addCenterView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutCenter.addView(view, index, params);
    }

    public void removeCenterView(View view) {
        mLayoutCenter.removeView(view);
    }

    public void removeCenterView(int index) {
        mLayoutCenter.removeViewAt(index);
    }

    public void removeAllCenterView() {
        mLayoutCenter.removeAllViews();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        keepTitleViewCenterForParent();
    }

    private void keepTitleViewCenterForParent() {
        int screenWidth = getScreenWidth(mContext);
        int margin = Math.max(mLayoutLeft.getWidth(), mLayoutRight.getWidth());

        if (mEllipsisMode == SUBTITLE_ELLIPSIS) {
            mTitleTextView.setVisibility(VISIBLE);
            if (mTitleTextView.getWidth() + margin * 2 > screenWidth
                    && mLayoutLeft.getWidth() != mLayoutRight.getWidth()) {//超出了屏幕宽度要做缩略
                //相等就不再设置，避免死循环
                ellipsisSubTitle();
            }
        } else {
            if (margin > screenWidth / 2f) {
                mTitleTextView.setVisibility(GONE);
                return;//超出屏幕一半，Title已经没有显示的地方了
            } else {
                mTitleTextView.setVisibility(VISIBLE);
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleTextView.getLayoutParams();
            //Log.d(TAG, "leftMargin" + params.leftMargin + " rightMargin" + params.rightMargin);

            if (margin == params.leftMargin && margin == params.rightMargin && margin != 0) {
                return;//相等就不再设置，避免死循环
            }
            //Log.d(TAG, "margin" + margin);
            ellipsisTitle(margin);
        }

    }


    /**
     * 缩略副标题 策略
     */
    private void ellipsisSubTitle() {
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        final RelativeLayout.LayoutParams subLeftParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        subLeftParams.addRule(RelativeLayout.LEFT_OF, R.id.mTitleTextView);
        subLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


        final RelativeLayout.LayoutParams subRightParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        subRightParams.addRule(RelativeLayout.RIGHT_OF, R.id.mTitleTextView);
        subRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        post(() -> {
            mTitleTextView.setLayoutParams(params);
            mTitleTextView.setGravity(Gravity.CENTER);
            mLayoutLeft.setLayoutParams(subLeftParams);
            mLayoutRight.setLayoutParams(subRightParams);
        });

    }

    /**
     * 缩略主标题 策略
     */
    private void ellipsisTitle(int margin) {
        final RelativeLayout.LayoutParams subLeftParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        subLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        final RelativeLayout.LayoutParams subRightParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        subRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//创建新的params
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.leftMargin = margin;
        params.rightMargin = margin;

        post(() -> {
            mTitleTextView.setLayoutParams(params);
            mTitleTextView.setGravity(Gravity.CENTER);
            mLayoutLeft.setLayoutParams(subLeftParams);
            mLayoutRight.setLayoutParams(subRightParams);

        });

    }


    /**
     * 设置 titleText
     */
    @Override
    public void setTitle(CharSequence titleText) {
        mTitleTextView.setText(titleText);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    /**
     * 设置缩略模式 是副标题缩略 还是主标题缩略(注意 缩略主标题可能会导致标题不显示，慎用)  默认副标题缩略{@link EllipsisMode}
     */
    public void setEllipsisMode(@EllipsisMode int ellipsisMode) {
        mEllipsisMode = ellipsisMode;
        requestLayout();
    }

    public int getEllipsisMode() {
        return mEllipsisMode;
    }

    /**
     * 获取title
     */
    @Override
    public CharSequence getTitle() {
        return mTitleTextView.getText();
    }

    /**
     * 设置标题 颜色
     */
    @Override
    public void setTitleTextColor(@ColorInt int colorId) {
        mTitleTextView.setTextColor(colorId);
    }

    /**
     * 设置标题字体大小
     */
    public void setTitleTextSize(float textSize) {
        mTitleTextView.setTextSize(textSize);
    }

    /**
     * 设置标题 加粗
     */
    public void setTitleBoldText(boolean bold) {
        mTitleTextView.getPaint().setFakeBoldText(bold);
    }

    /**
     * 设置全局副标题 文本颜色
     * <p>
     * 优先级 setSubTextColor < addLeftText(TextView)/addRightText(TextView)
     */
    public void setSubTextColor(@ColorInt int subTextColorId) {
        this.mSubTextColorId = subTextColorId;
        setChildTextColor(mLayoutLeft);
        setChildTextColor(mLayoutRight);
        setChildTextColor(mLayoutCenter);
    }

    /**
     * 设置全局副标题文本 字体大小
     * <p>
     * 优先级setSubTextColor < addLeftText(TextView)/addRightText(TextView)
     */
    public void setSubTextSize(float subTextSize) {
        this.mSubTextSize = subTextSize;
        setChildTextSize(mLayoutLeft);
        setChildTextSize(mLayoutRight);
        setChildTextSize(mLayoutCenter);
    }

    /**
     * 遍历设置字体大小
     */
    private void setChildTextSize(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                setChildTextSize(child);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(mSubTextSize);
        }
    }

    /**
     * 遍历设置字体颜色
     */
    private void setChildTextColor(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                setChildTextSize(child);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(mSubTextColorId);
        }
    }


    /**
     * 设置整个toolbar背景颜色
     */
    @Override
    public void setBackgroundColor(@ColorInt int colorId) {
        mRootView.setBackgroundColor(colorId);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        mRootView.setBackgroundResource(resId);
    }


    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public View getStatusBar() {
        return mStatusBar;
    }

    public View getBottomDivider() {
        return mBottomDivider;
    }

    public View getLeftView(int index) {
        return mLayoutLeft.getChildAt(index);
    }

    public View getRightView(int index) {
        return mLayoutRight.getChildAt(index);
    }

    public LinearLayout getLeftLayout() {
        return mLayoutLeft;
    }

    public LinearLayout getRightLayout() {
        return mLayoutRight;
    }

    public FrameLayout getCenterLayout() {
        return mLayoutCenter;
    }

    public static class Builder {
        private final Context mContext;
        private CharSequence titleText;
        private int titleTextResId;
        private int backResId;
        private int statusBarColorId,
                bottomDividerColorId = Color.BLACK,
                backgroundColorId = Color.BLACK,
                titleColorId = Color.BLACK,//均设置默认值
                subTextColorId = Color.BLACK;//均设置默认值
        private float titleTextSize = 18, subTextSize = 16;
        private boolean showStatusBar = false;//是否显示StatusBar
        private int bottomDividerHeight = 0;
        private ArrayList<View> leftViewList, rightViewList;
        private OnClickListener listener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setBackButton(@DrawableRes int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder setBackButton(@DrawableRes int backResId, OnClickListener listener) {
            this.backResId = backResId;
            this.listener = listener;
            return this;
        }


        public Builder invisibleBackButton() {
            this.backResId = 0;
            return this;
        }

        public void addCenterText() {

        }

        public Builder setBottomDivider(@ColorInt int bottomDividerColorId, int bottomDividerHeight) {
            this.bottomDividerColorId = bottomDividerColorId;
            this.bottomDividerHeight = bottomDividerHeight;
            return this;
        }

        /**
         * leftImage ScaleType默认为 ScaleType.CENTER
         */
        public Builder addLeftImage(@DrawableRes int imgResId, OnClickListener listener) {
            return addLeftImage(imgResId, ImageView.ScaleType.CENTER, listener);
        }

        public Builder addLeftImage(@DrawableRes int imgResId, ImageView.ScaleType scaleType, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createImageMenu(mContext, imgResId, scaleType, listener));
            return this;
        }

        /**
         * addRightImage ScaleType默认为 ScaleType.CENTER
         */
        public Builder addRightImage(@DrawableRes int imgResId, OnClickListener listener) {
            return addRightImage(imgResId, ImageView.ScaleType.CENTER, listener);
        }

        public Builder addRightImage(@DrawableRes int imgResId, ImageView.ScaleType scaleType, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createImageMenu(mContext, imgResId, scaleType, listener));
            return this;
        }

        /**
         * leftText 默认subTextColor Color.BLACK textSize 16sp
         */
        public Builder addLeftText(CharSequence text, OnClickListener listener) {
            return addLeftText(text, subTextColorId, subTextSize, listener);
        }


        public Builder addLeftText(CharSequence text, @ColorInt int textColorId, float textSize, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createTextMenu(mContext, text, textColorId, textSize, listener));
            return this;
        }

        /**
         * leftText 默认subTextColor Color.BLACK textSize 16sp
         */
        public Builder addLeftText(@StringRes int textResId, OnClickListener listener) {
            return addLeftText(mContext.getString(textResId), subTextColorId, subTextSize, listener);
        }

        public Builder addLeftText(@StringRes int textResId, @ColorInt int textColorId, float textSize, OnClickListener listener) {
            return addLeftText(mContext.getString(textResId), textColorId, textSize, listener);
        }


        /**
         * rightText 默认subTextColor Color.BLACK textSize 16sp
         */
        public Builder addRightText(CharSequence text, OnClickListener listener) {
            return addRightText(text, subTextColorId, subTextSize, listener);
        }

        public Builder addRightText(CharSequence text, @ColorInt int textColorId, float textSize, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createTextMenu(mContext, text, textColorId, textSize, listener));
            return this;
        }

        /**
         * rightText 默认subTextColor Color.BLACK textSize 16sp
         */
        public Builder addRightText(@StringRes int textResId, OnClickListener listener) {
            return addRightText(mContext.getString(textResId), listener);
        }

        public Builder addRightText(@StringRes int textResId, @ColorInt int textColorId, float textSize, OnClickListener listener) {
            return addRightText(mContext.getString(textResId), textColorId, textSize, listener);
        }

        /**
         * addView
         */
        public Builder addLeftView(View view) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(view);
            return this;
        }

        public Builder addRightView(View view) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(view);
            return this;
        }

        public Builder setTitle(CharSequence titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder setTitle(@StringRes int titleTextResId) {
            this.titleTextResId = titleTextResId;
            return this;
        }

        /**
         * setTitleTextColor  和 setAllTextColor 顺序 按最新设置的那个算
         */
        public Builder setTitleTextColor(@ColorInt int titleColorId) {
            this.titleColorId = titleColorId;
            return this;
        }

        public Builder setTitleTextSize(float titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        /**
         * 设置所有副标题文本颜色
         * 要在 添加副标题前调用，否则不起作用
         */
        public Builder setSubTextColor(@ColorInt int subTextColorId) {
            this.subTextColorId = subTextColorId;
            return this;
        }

        public Builder setSubTextSize(float subTextSize) {
            this.subTextSize = subTextSize;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int backgroundColorId) {
            this.backgroundColorId = backgroundColorId;
            return this;
        }

        public Builder setStatusBarColor(@ColorInt int statusBarColorId) {
            this.statusBarColorId = statusBarColorId;
            this.showStatusBar = true;
            return this;
        }

        public BaseToolbar build() {
            BaseToolbar toolbar = new BaseToolbar(mContext);

            toolbar.setBackButton(backResId, listener);

            if (TextUtils.isEmpty(titleText)) {
                if (titleTextResId > 0) toolbar.setTitle(titleTextResId);
                else toolbar.setTitle(null);
            } else toolbar.setTitle(titleText);

            toolbar.setTitleTextColor(titleColorId);
            toolbar.setTitleTextSize(titleTextSize);

            toolbar.setSubTextColor(subTextColorId);
            toolbar.setSubTextSize(subTextSize);

            if (leftViewList != null && !leftViewList.isEmpty()) {
                for (View view : leftViewList) {
                    toolbar.addLeftView(view);
                }
            }

            if (rightViewList != null && !rightViewList.isEmpty()) {
                for (View view : rightViewList) {
                    toolbar.addRightView(view);
                }
            }

            if (bottomDividerHeight > 0)
                toolbar.setBottomDivider(bottomDividerColorId, bottomDividerHeight);

            if (showStatusBar) toolbar.setStatusBarColor(statusBarColorId);

//            toolbar.setBackgroundColor(backgroundColorId);

            toolbar.setBackground(new ColorDrawable(backgroundColorId));
            return toolbar;
        }
    }


    /**
     * 创建文字菜单
     */
    public static TextView createTextMenu(Context context, CharSequence text, @ColorInt int textColorId, float textSize, OnClickListener listener) {
        TextView textMenu = new TextView(context);
        textMenu.setTextColor(textColorId);
        textMenu.setTextSize(textSize);
        textMenu.setGravity(Gravity.CENTER);
        int padding = dp2px(context, 5);
        textMenu.setPadding(padding, 0, padding, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textMenu.setLayoutParams(params);
        textMenu.setText(text);
        if (listener != null) textMenu.setOnClickListener(listener);
        return textMenu;
    }

    public static ImageView createImageMenu(Context context, @DrawableRes int imageResId, OnClickListener listener) {
        return createImageMenu(context, imageResId, ImageView.ScaleType.CENTER, listener);
    }

    /**
     * 创建图片菜单
     */
    public static ImageView createImageMenu(Context context, @DrawableRes int imageResId, ImageView.ScaleType scaleType, OnClickListener listener) {
        ImageView imageMenu = new ImageView(context);
        int padding = dp2px(context, 5);
        imageMenu.setPadding(padding, 0, padding, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        imageMenu.setScaleType(scaleType);
        imageMenu.setLayoutParams(params);
        imageMenu.setImageResource(imageResId);
        if (listener != null) imageMenu.setOnClickListener(listener);
        return imageMenu;
    }

    /**
     * 创建 带文字 和图案 的返回键
     */
    public static LinearLayout createBackLayout(Context context, @DrawableRes int imageResId, CharSequence text, @ColorInt int textColorId, float textSize_SP, OnClickListener listener) {
        LinearLayout backLayout = new LinearLayout(context);
        if (imageResId != 0) {
            ImageView iv = createImageMenu(context, imageResId, ImageView.ScaleType.CENTER_INSIDE, null);

            iv.setPadding(0, 0, 0, 0);
            backLayout.addView(iv);
        }
        if (!TextUtils.isEmpty(text)) {
            TextView tv = createTextMenu(context, text, textColorId, textSize_SP, null);
            tv.setPadding(0, 0, 0, 0);
            backLayout.addView(tv);
        }

        int padding = dp2px(context, 5);
        backLayout.setPadding(padding, 0, padding, 0);
        backLayout.setGravity(Gravity.CENTER);
        backLayout.setOnClickListener(listener);

        return backLayout;
    }

    private static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }
}

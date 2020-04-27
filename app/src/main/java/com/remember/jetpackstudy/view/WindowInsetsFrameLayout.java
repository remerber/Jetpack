package com.remember.jetpackstudy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WindowInsetsFrameLayout extends FrameLayout {
    public WindowInsetsFrameLayout(@NonNull Context context) {
        super(context);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WindowInsetsFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        requestApplyInsets();
    }
//    添加Fragment的过程可看作往容器布局添加子View的过程。当第一个Fra
//    gment被添加到容器布局时，容器布局找出fitSystemWindows为true的子Vi
//    ew，并为其paddingTop一个状态栏的高度，当其他Fragment随后被添加时，上
//    述的paddingTop适配已经被消费过一次，并不会再为其后添加的View进行适
//    配（默认行为），因此我们要自定义容器
//    布局View，使其每个子View都消费一次ViewGroup分发的WindowsInsets，相当于每个子Fragment都能适配状态栏
    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets windowInsets = super.dispatchApplyWindowInsets(insets);
        if (!insets.isConsumed()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                windowInsets = getChildAt(i).dispatchApplyWindowInsets(insets);
            }
        }
        return windowInsets;
    }
}


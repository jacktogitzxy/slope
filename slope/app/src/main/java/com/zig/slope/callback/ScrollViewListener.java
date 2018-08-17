package com.zig.slope.callback;

import com.zig.slope.view.ObservableScrollView;

/**
 * Created by 17120 on 2018/7/4.
 */

public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}

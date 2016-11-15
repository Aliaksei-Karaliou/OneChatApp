package com.github.AliakseiKaraliou.onechatapp.logic.utils.imageLoader;

import android.support.annotation.Nullable;

import java.util.Map;

public interface Cache<Input, Output> {
    void put(Input input, Output output);

    @Nullable
    Output get(Input input);

    Map<Input, Output> getCache();

    boolean contains(Input input);
}

package com.github.AliakseiKaraliou.onechatapp.logic.common;

import android.support.annotation.Nullable;

public interface IParser<Param, Result> {
    @Nullable
    Result parse(Param param);
}

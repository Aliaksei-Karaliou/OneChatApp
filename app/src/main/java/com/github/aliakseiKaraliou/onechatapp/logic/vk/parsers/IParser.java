package com.github.aliakseiKaraliou.onechatapp.logic.vk.parsers;

import android.support.annotation.Nullable;

public interface IParser<Param, Result> {
    @Nullable
    Result parse(Param param);
}

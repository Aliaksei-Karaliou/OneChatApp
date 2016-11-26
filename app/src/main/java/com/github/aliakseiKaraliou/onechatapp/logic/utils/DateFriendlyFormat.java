package com.github.aliakseiKaraliou.onechatapp.logic.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.github.aliakseiKaraliou.onechatapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFriendlyFormat {
    private static final int MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;
    private static final String TODAY_DATE_FORMAT = "HH:mm";
    private static final String DAY_MONTH_DATE_FORMAT = "dd MMM";

    public String convert(Context context, Date date) {
        final String s;
        long d = date.getTime();
        final SimpleDateFormat format;
        if (DateUtils.isToday(date.getTime())) {
            format = new SimpleDateFormat(TODAY_DATE_FORMAT, Locale.getDefault());
        } else if (DateUtils.isToday(date.getTime() + MILLISECONDS_IN_A_DAY)) {
            return context.getString(R.string.yesterday);
        } else {
            format = new SimpleDateFormat(DAY_MONTH_DATE_FORMAT, Locale.getDefault());
        }
        return format.format(date);
    }
}

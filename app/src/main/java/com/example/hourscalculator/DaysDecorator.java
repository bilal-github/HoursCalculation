package com.example.hourscalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.appcompat.content.res.AppCompatResources;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class DaysDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> _dates;
    private final Drawable _drawable;
    private Context _context;

    public DaysDecorator(Context context, HashSet<CalendarDay> dates) {
        _dates = new HashSet<>(dates);
        _drawable = AppCompatResources.getDrawable(context, R.drawable.days_ring);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return _dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new DotSpan(5, _color));
        view.setBackgroundDrawable(_drawable);
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.3f));
    }
}

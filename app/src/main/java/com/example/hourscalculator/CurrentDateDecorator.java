package com.example.hourscalculator;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.appcompat.content.res.AppCompatResources;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CurrentDateDecorator implements DayViewDecorator {

    private final Drawable _drawable;

    public CurrentDateDecorator(Context context) {
        _drawable = AppCompatResources.getDrawable(context, R.drawable.current_day_yellow_circle);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.setBackgroundDrawable(_drawable);
//        view.addSpan(new ForegroundColorSpan(Color.MAGENTA));
//        view.addSpan(new BackgroundColorSpan(Color.CYAN));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.3f));

    }
}

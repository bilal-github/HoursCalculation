package com.example.hourscalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.HashMap;
import java.util.HashSet;

public class CalendarFragment extends Fragment {

    private DBHelper _dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        _dbHelper = new DBHelper(view.getContext());
        MaterialCalendarView materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                .setMinimumDate(LocalDate.of(1900, Month.JANUARY, 1))
                .setMaximumDate(LocalDate.of(2100, Month.DECEMBER, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorator(new CurrentDateDecorator(getContext()));
        HashMap<CalendarDay, Double> allDays = _dbHelper.getAll();

        materialCalendarView.addDecorator(new DaysDecorator(getContext(), new HashSet<>(allDays.keySet())));
        materialCalendarView.setDateSelected(CalendarDay.today(), true);

        return view;
    }
}
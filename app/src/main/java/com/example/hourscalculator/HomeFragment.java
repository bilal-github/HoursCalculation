package com.example.hourscalculator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private TextView dateView, dateLabel, dateRequired, hoursRequired;
    private TextInputEditText hoursInput;

    private DBHelper dbHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Button btnDate = v.findViewById(R.id.btnDate);
        Button btnSubmit = v.findViewById(R.id.btnSubmit);

        dateView = v.findViewById(R.id.dateView);
        dateRequired = v.findViewById(R.id.dateRequired);
        hoursRequired = v.findViewById(R.id.hoursRequired);
        dateLabel = v.findViewById(R.id.dateLabel);
        hoursInput = v.findViewById(R.id.numOfHours);

        dbHelper = new DBHelper(v.getContext());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        btnDate.setOnClickListener(view -> datePicker.show(getChildFragmentManager(), "DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener((Long selection) -> {
            dateLabel.setVisibility(TextView.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
            dateView.setText(datePicker.getHeaderText());
            dateRequired.setVisibility(TextView.INVISIBLE);
        });

        btnSubmit.setOnClickListener((View view) -> {
            if (fieldsNotEmpty()) {
                String date = dateView.getText().toString();
                Float numOfHours = Float.parseFloat(Objects.requireNonNull(hoursInput.getText()).toString());
                if (dbHelper.dateExists(date)) {
                    Boolean isUpdate = updatePrompt(getContext());
                    Log.i("isUpdate", isUpdate.toString());
                } else {
                    if (dbHelper.insert(date, numOfHours)) {
                        Toast.makeText(getContext(), "Hours Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Hours Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        hoursInput.setOnFocusChangeListener((View view, boolean hasFocus) -> {
                    if (!hasFocus) {
                        if (Objects.requireNonNull(hoursInput.getText()).toString().isEmpty()) {
                            hoursRequired.setVisibility(TextView.VISIBLE);
                        } else {
                            hoursRequired.setVisibility(TextView.INVISIBLE);
                        }
                    }
                }
        );
        return v;
    }

    private Boolean fieldsNotEmpty() {
        if (dateView.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(hoursInput.getText()).toString().isEmpty()) {
            Toast.makeText(getContext(), "Please enter number of hours", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Boolean updatePrompt(Context context) {
        AtomicReference<Boolean> result = new AtomicReference<>(false);
        new AlertDialog.Builder(context)
                .setTitle("Update?")
                .setMessage("This date already exists. Do you want to update the hours?")
                .setPositiveButton("Yes", (dialog, i) -> {
                    updateButtonClicked(true);
                    result.set(true);
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, i) -> {
                    updateButtonClicked(false);
                    dialog.cancel();
                })
                .show();
        return result.get();

    }

    private void updateButtonClicked(boolean isUpdate) {
        String date = dateView.getText().toString();
        Float numOfHours = Float.parseFloat(Objects.requireNonNull(hoursInput.getText()).toString());

        if (isUpdate) {
            updateEntry(date, numOfHours);
        }
    }

    private void updateEntry(String date, Float numOfHours) {
        if (dbHelper.update(date, numOfHours)) {
            Toast.makeText(getContext(), "Hours Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Hours Not Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
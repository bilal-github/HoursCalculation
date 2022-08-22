package com.example.hourscalculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private Button btnDate, btnSubmit;
    private TextView dateView, dateLabel, dateRequired, hoursRequired;
    private TextInputEditText hoursInput;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDate = findViewById(R.id.btnDate);
        btnSubmit = findViewById(R.id.btnSubmit);

        dateView = findViewById(R.id.dateView);
        dateRequired = findViewById(R.id.dateRequired);
        hoursRequired = findViewById(R.id.hoursRequired);
        dateLabel = findViewById(R.id.dateLabel);
        hoursInput = findViewById(R.id.numOfHours);

        dbHelper = new DBHelper(this);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

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
                    Boolean isUpdate = updatePrompt();
                    Log.i("isUpdate", isUpdate.toString());
                } else {
                    if (dbHelper.insert(date, numOfHours)) {
                        Toast.makeText(MainActivity.this, "Hours Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Hours Not Inserted", Toast.LENGTH_SHORT).show();
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
    }

    private Boolean fieldsNotEmpty() {
        if (dateView.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Objects.requireNonNull(hoursInput.getText()).toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter number of hours", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Boolean updatePrompt() {
        AtomicReference<Boolean> result = new AtomicReference<>(false);
        AlertDialog builder = new AlertDialog.Builder(this)
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
            Toast.makeText(MainActivity.this, "Hours Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Hours Not Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
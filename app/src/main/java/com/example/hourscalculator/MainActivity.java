package com.example.hourscalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private Button btnDate, btnSubmit;
    private TextView dateView, dateLabel;
    private TextInputEditText hoursInput;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDate = findViewById(R.id.btnDate);
        btnSubmit = findViewById(R.id.btnSubmit);

        dateView = findViewById(R.id.dateView);
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
        });

        btnSubmit.setOnClickListener((View view) -> {
            String date = dateView.getText().toString();
            Float numOfHours = Float.parseFloat(Objects.requireNonNull(hoursInput.getText()).toString());
            if (!dbHelper.dateExists(date)) {
                Boolean isInserted = dbHelper.insert(date, numOfHours);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Hours Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Hours Not Inserted", Toast.LENGTH_SHORT).show();
                }
            } else {

            }

        });
}

    //create alert which takes in yes and no
    //if yes, update the hours
    //if no, do nothing
    private Boolean showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    return true;
                })
                .setNegativeButton("No", (dialog, id) -> {
                    return false;
                    dialog.cancel();
                });

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();

    }
}
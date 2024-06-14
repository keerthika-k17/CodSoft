package com.example.todoapp;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDueTime;
    private boolean isEditMode = false;
    private int taskPosition = -1;
    String amPm;
    int formattedHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getWindow().setStatusBarColor(ContextCompat.getColor(AddTaskActivity.this, R.color.blue));

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueTime = findViewById(R.id.editTextDueTime);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);

        // Check if this is an edit action
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("title") && intent.hasExtra("description") && intent.hasExtra("dueTime") && intent.hasExtra("position")) {
            isEditMode = true;
            taskPosition = intent.getIntExtra("position", -1);
            editTextTitle.setText(intent.getStringExtra("title"));
            editTextDescription.setText(intent.getStringExtra("description"));
            editTextDueTime.setText(intent.getStringExtra("dueTime"));
            buttonAddTask.setText("Update Task");
        }

        editTextDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String dueTime = editTextDueTime.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || dueTime.isEmpty()) { // Add check for new field
                    Toast.makeText(AddTaskActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("description", description);
                resultIntent.putExtra("dueTime", dueTime);
                if (isEditMode) {
                    resultIntent.putExtra("position", taskPosition);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CustomDialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute1) {
                if(hourOfDay >= 12)
                {
                    amPm = "PM";
                }
                else
                {
                    amPm = "AM";
                }
                formattedHour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                formattedHour = (formattedHour == 0) ? 12 : formattedHour;
                editTextDueTime.setText(String.format("%02d:%02d", formattedHour, minute1) + amPm);
            }
        } , hour, minute, false);
        timePickerDialog.show();
    }
}
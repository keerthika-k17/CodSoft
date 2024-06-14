package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements ToDoAdapter.OnItemClickListener {
    private static final int REQUEST_CODE_ADD_TASK = 1;
    private static final int REQUEST_CODE_EDIT_TASK = 2;
    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private List<ToDo> toDoList;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private TextView noTasksTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getWindow().setStatusBarColor(ContextCompat.getColor(FirstActivity.this, R.color.blue));

        recyclerView = findViewById(R.id.recyclerView);
        noTasksTextView = findViewById(R.id.no_task_textView);
        Button addButton = findViewById(R.id.addButton);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        toDoList = new ArrayList<>();
        loadToDoListFromDatabase();
        adapter = new ToDoAdapter(this, toDoList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
            }
        });
    }

    private void loadToDoListFromDatabase() {
        toDoList.clear();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TODO, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String task = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String dueTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DUE_TIME));
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED)) > 0;

            ToDo toDo = new ToDo(id, task, description, dueTime, completed);
            toDoList.add(toDo);
        }
        cursor.close();

        updateNoTasksView();
    }

    private void updateNoTasksView() {
        if (toDoList.isEmpty()) {
            noTasksTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTasksTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onEditClick(int position) {
        ToDo toDo = toDoList.get(position);
        Intent intent = new Intent(FirstActivity.this, AddTaskActivity.class);
        intent.putExtra("title", toDo.getTask());
        intent.putExtra("description", toDo.getDescription());
        intent.putExtra("dueTime", toDo.getDueTime());
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
    }


    @Override
    public void onDeleteClick(int position) {
        ToDo toDo = toDoList.get(position);
        database.delete(DatabaseHelper.TABLE_TODO, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(toDo.getId())});
        toDoList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, toDoList.size());
        updateNoTasksView();
    }

    public void onCompletedClick(int position, boolean completed)
    {
        ToDo toDo = toDoList.get(position);
        toDo.setCompleted(completed);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COMPLETED, completed ? 1 : 0);
        database.update(DatabaseHelper.TABLE_TODO, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(toDo.getId())});
        adapter.notifyItemChanged(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            String dueDate = data.getStringExtra("dueDate");
            String dueTime = data.getStringExtra("dueTime");

            if (requestCode == REQUEST_CODE_ADD_TASK) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_TASK, title);
                values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
                values.put(DatabaseHelper.COLUMN_DUE_TIME, dueTime);

                long id = database.insert(DatabaseHelper.TABLE_TODO, null, values);
                ToDo newToDo = new ToDo((int) id, title, description, dueTime, false);
                toDoList.add(newToDo);
                adapter.notifyItemInserted(toDoList.size() - 1);
                updateNoTasksView();
            }
            else if (requestCode == REQUEST_CODE_EDIT_TASK) {
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    ToDo toDo = toDoList.get(position);
                    toDo.setTask(title);
                    toDo.setDescription(description);
                    toDo.setDueTime(dueTime);

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_TASK, title);
                    values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
                    values.put(DatabaseHelper.COLUMN_DUE_TIME, dueTime);
                    database.update(DatabaseHelper.TABLE_TODO, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(toDo.getId())});

                    adapter.notifyItemChanged(position);
                    updateNoTasksView();
                }
            }
        }
    }

}


package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {
    private List<ToDo> toDoList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onCompletedClick(int position, boolean completed);
    }

    public ToDoAdapter(Context context, List<ToDo> toDoList, OnItemClickListener listener) {
        this.toDoList = toDoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ToDo toDo = toDoList.get(position);
        holder.toDoTextView.setText(toDo.getTask());
        holder.descriptionTextView.setText(toDo.getDescription());
        holder.dueTimeTextView.setText(toDo.getDueTime());

        holder.completedCheckbox.setOnCheckedChangeListener(null);
        holder.completedCheckbox.setChecked(toDo.isCompleted());

        holder.optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.todo_options_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                listener.onEditClick(position);
                                return true;
                            case R.id.action_delete:
                                listener.onDeleteClick(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });


        holder.completedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCompletedClick(position, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView toDoTextView, descriptionTextView, dueTimeTextView;
        TextView optionsMenu;
        CheckBox completedCheckbox;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoTextView = itemView.findViewById(R.id.todoTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dueTimeTextView = itemView.findViewById(R.id.dueTimeTextView);
            optionsMenu = itemView.findViewById(R.id.optionsMenu);
            completedCheckbox = itemView.findViewById(R.id.completedCheckBox);
        }
    }
}

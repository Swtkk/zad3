package com.example.todo;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "com.example.todo.task_id";
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflacja układu fragment_task_list.xml
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Inicjalizacja RecyclerView
        recyclerView = view.findViewById(R.id.task_recycler_view);
        Log.d("TaskListFragment", "RecyclerView is null: " + (recyclerView == null));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d("TaskListFragment", "LayoutManager set");

        // Ustawienie adaptera
        updateView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Odśwież widok listy po powrocie do fragmentu
        Log.d("TaskListFragment", "onResume called");
        updateView();
    }

    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        Log.d("TaskListFragment", "Tasks count in updateView: " + tasks.size());

        // Zawsze ustaw nowy adapter
        adapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(adapter);
        Log.d("TaskListFragment", "New adapter set");
        updateSubtitle();
    }

    public void updateSubtitle() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                todoTasksCount++;
            }
        }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null && appCompatActivity.getSupportActionBar() != null) {
            String subtitle = getString(R.string.subtitle_format, todoTasksCount);
            if (!subtitleVisible) {
                subtitle = null;
            }
            appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
        }


    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_task) {
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            TaskFragment fragment = TaskFragment.newInstance(task.getId());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu(); // Odświeżenie menu
            updateSubtitle();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTextView;
        private final TextView dateTextView;
        private Task task;
        private ImageView categoryIcon;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            categoryIcon = itemView.findViewById(R.id.task_category_icon);
            itemView.setOnClickListener(this);


//            CheckBox doneCheckBox = itemView.findViewById(R.id.task_done_checkbox);
//            doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                task.setDone(isChecked);
//                bind(task); // Odśwież wygląd elementu
//            });
        }

        public void bind(Task task) {
            this.task = task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());

            // Zmiana koloru tła w zależności od statusu ukończenia
            if (task.isDone()) {
                itemView.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_purple));
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                itemView.setBackgroundColor(itemView.getResources().getColor(android.R.color.transparent));
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            if (task.getCategory() == Category.Dom) {
                categoryIcon.setImageResource(R.drawable.ic_action_name);
            } else {

                categoryIcon.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                com.example.todo.TaskFragment fragment = com.example.todo.TaskFragment.newInstance(task.getId());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}

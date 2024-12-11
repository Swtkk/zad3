package com.example.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

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

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTextView;
        private final TextView dateTextView;
        private Task task;


        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
//            CheckBox doneCheckBox = itemView.findViewById(R.id.task_done_checkbox);
            itemView.setOnClickListener(this);
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
            } else {
                itemView.setBackgroundColor(itemView.getResources().getColor(android.R.color.transparent));
            }
        }

        @Override
        public void onClick(View view) {
            if (getActivity() != null) {
                com.example.todoapp.TaskFragment fragment = com.example.todoapp.TaskFragment.newInstance(task.getId());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}

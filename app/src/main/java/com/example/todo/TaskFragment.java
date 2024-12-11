package com.example.todoapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todo.R;
import com.example.todo.Task;
import com.example.todo.TaskStorage;

import java.util.Date;
import java.util.UUID;

public class TaskFragment extends Fragment {
    private static final String ARG_TASK_ID = "task_id";

    private Task task;
    private EditText nameField;
    private Button dateButton;
    private CheckBox doneCheckBox;

    public static TaskFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pobierz ID zadania z argumentów i znajdź odpowiednie zadanie w TaskStorage
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        task = TaskStorage.getInstance().getTask(taskId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // Pobieranie elementów widoku
        nameField = view.findViewById(R.id.task_name);
        dateButton = view.findViewById(R.id.task_date);
        doneCheckBox = view.findViewById(R.id.task_done);

        // Ustawienie początkowych wartości pól
        nameField.setText(task.getName());
        dateButton.setText("Data" + task.getDate().toString());
        dateButton.setEnabled(false); // Data jest tylko do odczytu
        doneCheckBox.setChecked(task.isDone());

        // Dodanie nasłuchiwacza dla pola tekstowego
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nie potrzebujemy obsługi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nie potrzebujemy obsługi
            }
        });

        // Dodanie nasłuchiwacza dla CheckBoxa
        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> task.setDone(isChecked));

        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}

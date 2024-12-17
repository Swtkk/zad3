package com.example.todo;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todo.Category;
import com.example.todo.R;
import com.example.todo.Task;
import com.example.todo.TaskStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {
    private static final String ARG_TASK_ID = "task_id";

    private Task task;
    private EditText nameField;
    private EditText dateField;
    private CheckBox doneCheckBox;
    private Spinner categorySpinner;

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
        Calendar calendar = Calendar.getInstance();
        // Pobieranie elementów widoku
        nameField = view.findViewById(R.id.task_name);
        dateField = view.findViewById(R.id.task_date);
        doneCheckBox = view.findViewById(R.id.task_done);
        categorySpinner = view.findViewById(R.id.task_categoryy);
        // Ustawienie początkowych wartości pól
        nameField.setText(task.getName());
//        dateField.setText("Data" + task.getDate().toString());
//        dateField.setEnabled(false); // Data jest tylko do odczytu
        doneCheckBox.setChecked(task.isDone());
        DatePickerDialog.OnDateSetListener date = (view12, year,month,day) ->{
            calendar.set(java.util.Calendar.YEAR, year);
            calendar.set(java.util.Calendar.MONTH, month);
            calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        dateField.setOnClickListener(view1 -> new DatePickerDialog(getContext(), date,calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show());

        setupDateFieldValue(task.getDate());

        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                task.setCategory(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());

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

    private void setupDateFieldValue(Date date) {
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
        dateField.setText(dateFormat.format(date));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}

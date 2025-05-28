package com.example.randomjava;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class RandomFragment extends Fragment {

    private EditText editTextLowerBound;
    private EditText editTextUpperBound;
    private Button buttonGenerate;
    private TextView textViewResult;

    public RandomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_random, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextLowerBound = view.findViewById(R.id.edit_random_lower);
        editTextUpperBound = view.findViewById(R.id.edit_random_upper);
        buttonGenerate = view.findViewById(R.id.random_button);
        textViewResult = view.findViewById(R.id.text_result_value);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomNumber();
            }
        });
    }

    private void generateRandomNumber() {
        String lowerBoundStr = editTextLowerBound.getText().toString();
        String upperBoundStr = editTextUpperBound.getText().toString();

        if (lowerBoundStr.isEmpty() || upperBoundStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both bounds", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int lowerBound = Integer.parseInt(lowerBoundStr);
            int upperBound = Integer.parseInt(upperBoundStr);

            if (lowerBound > upperBound) {
                Toast.makeText(getContext(), "Lower bound cannot be greater than upper bound", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate random number (inclusive)
            // For Random.nextInt(n), the range is 0 (inclusive) to n (exclusive).
            // So, to get a range [min, max] inclusive:
            // Random().nextInt((max - min) + 1) + min
            Random random = new Random();
            int randomNumber = random.nextInt((upperBound - lowerBound) + 1) + lowerBound;

            textViewResult.setText(String.valueOf(randomNumber));

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid integer numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
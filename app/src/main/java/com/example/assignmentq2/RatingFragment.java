package com.example.assignmentq2;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RatingFragment extends Fragment {

    private EditText etDishName;
    private Spinner spinnerDishType;
    private RatingBar ratingBar;
    private Button submitBtn;
    private TextView tvResult;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        etDishName = view.findViewById(R.id.etDishName);
        spinnerDishType = view.findViewById(R.id.spDishType);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitBtn = view.findViewById(R.id.submitButton);
        tvResult = view.findViewById(R.id.tvRatingResult);

        initSpinner();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating();
            }
        });

        return view;
    }

    void initSpinner(){
        String[] dishType = {"Entrée", "Appetizer", "Dessert", "Beverage", "Side Dish"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dishType);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDishType.setAdapter(adapter);
    }

    void submitRating(){
        String dishName = etDishName.getText().toString().trim();
        String dishType = spinnerDishType.getSelectedItem().toString();
        float rating = ratingBar.getRating();

        if(dishName.isEmpty()){
            Toast.makeText(getContext(), "Please enter a dish name", Toast.LENGTH_SHORT).show();
            return;
        }

        String result = "Dish: " + dishName + "\n" +
                "Type: " + dishType + "\n" +
                "Rating: " + rating + " stars";


        tvResult.setText(result);

        Toast.makeText(getContext(), "Rating Submitted", Toast.LENGTH_SHORT).show();


        viewModel.setLastRating(result);
    }

}
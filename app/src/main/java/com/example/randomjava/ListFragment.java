package com.example.randomjava;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Make sure you have these colors in your res/values/colors.xml or use Android defaults
// <color name="black">#FF000000</color>
// <color name="default_item_background">@android:color/transparent</color> <!-- Or your desired default -->
// <color name="highlight_color">#AED581</color> <!-- Example light green -->

public class ListFragment extends Fragment {

    // Data class to hold list item information
    private static class ListItem {
        String entryName;
        int weight;
        View view; // To store a reference to the inflated row view for highlighting

        ListItem(String entryName, int weight, View view) {
            this.entryName = entryName;
            this.weight = weight;
            this.view = view;
        }
    }

    private EditText editTextEntry;
    private EditText editTextWeight;
    private Button buttonAddToList;
    private Button buttonSelectRandom;
    private LinearLayout linearLayoutListItems; // Container for dynamic rows

    private final List<ListItem> itemList = new ArrayList<>();
    private final Random random = new Random();
    private View currentlyHighlightedView = null;
    private int defaultBackgroundColor;
    private int highlightBackgroundColor;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEntry = view.findViewById(R.id.edit_list_entry);
        editTextWeight = view.findViewById(R.id.edit_list_weight);
        buttonAddToList = view.findViewById(R.id.list_button_add);
        buttonSelectRandom = view.findViewById(R.id.list_button_random);
        linearLayoutListItems = view.findViewById(R.id.linear_layout_list_items);

        // Define default and highlight colors (load from colors.xml if preferred)
        // Using ContextCompat for better compatibility
        if (getContext() != null) {
            defaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.white); // Or some default like Color.TRANSPARENT
            highlightBackgroundColor = ContextCompat.getColor(getContext(), R.color.purple_200); // Your defined highlight color
        } else {
            // Fallback if context is somehow null (should not happen in onViewCreated)
            defaultBackgroundColor = Color.TRANSPARENT;
            highlightBackgroundColor = Color.YELLOW; // Basic fallback
        }


        buttonAddToList.setOnClickListener(v -> addListItem());
        buttonSelectRandom.setOnClickListener(v -> selectRandomWeightedItem());
    }

    private void addListItem() {
        String entryName = editTextEntry.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();

        if (TextUtils.isEmpty(entryName)) {
            editTextEntry.setError("Entry name cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(weightStr)) {
            editTextWeight.setError("Weight cannot be empty");
            return;
        }

        int weight;
        try {
            weight = Integer.parseInt(weightStr);
            if (weight <= 0) {
                editTextWeight.setError("Weight must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            editTextWeight.setError("Invalid number for weight");
            return;
        }

        // Inflate the list_item_row.xml
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = inflater.inflate(R.layout.list_item_row, linearLayoutListItems, false);

        // **CORRECTION HERE:** Find the TextViews *within* the inflated itemView
        TextView textItemEntry = itemView.findViewById(R.id.text_item_entry);
        TextView textItemWeight = itemView.findViewById(R.id.text_item_weight);

        // Now set the text on these specific TextViews
        textItemEntry.setText(entryName);
        textItemWeight.setText(String.valueOf(weight)); // Make sure to convert int to String
        itemView.setBackgroundColor(defaultBackgroundColor); // Set default background

        // Add to our data list and to the LinearLayout
        ListItem newItem = new ListItem(entryName, weight, itemView);
        itemList.add(newItem);
        linearLayoutListItems.addView(itemView);

        // Clear input fields
        editTextEntry.setText("");
        // editTextWeight.setText(""); // You might want to set a default weight like "1"
        editTextWeight.setText("1"); // Suggestion: Default to 1 for next entry
        editTextEntry.requestFocus(); // Optional: move focus back to the first field
    }

    private void selectRandomWeightedItem() {
        if (itemList.isEmpty()) {
            Toast.makeText(getContext(), "The list is empty. Add some items first.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear previous highlight
        if (currentlyHighlightedView != null) {
            currentlyHighlightedView.setBackgroundColor(defaultBackgroundColor);
        }

        int totalWeight = 0;
        for (ListItem item : itemList) {
            totalWeight += item.weight;
        }

        if (totalWeight <= 0) { // Should not happen if weights are positive
            Toast.makeText(getContext(), "Total weight is zero, cannot select.", Toast.LENGTH_SHORT).show();
            return;
        }

        int randomNumber = random.nextInt(totalWeight); // Generates a number from 0 to totalWeight-1

        ListItem selectedItem = null;
        int cumulativeWeight = 0;
        for (ListItem item : itemList) {
            cumulativeWeight += item.weight;
            if (randomNumber < cumulativeWeight) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            // Highlight the selected item's view
            selectedItem.view.setBackgroundColor(highlightBackgroundColor);
            currentlyHighlightedView = selectedItem.view;

            // Optional: Scroll to the selected item if it's not fully visible
            // This is a bit more complex and might require the ScrollView reference
            // and calculating the position of the item. For simplicity, I'm omitting it here,
            // but you can add it if needed.

            Toast.makeText(getContext(), "Selected: " + selectedItem.entryName, Toast.LENGTH_SHORT).show();
        } else {
            // This case should ideally not be reached if logic is correct and list is not empty
            Toast.makeText(getContext(), "Could not select an item.", Toast.LENGTH_SHORT).show();
        }
    }
}
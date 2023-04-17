package com.example.mygame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mygame.databinding.FragmentThirdBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fragment_third extends Fragment {
    //Global variables
    private final List<Integer> sequence = new ArrayList<>();
    private int round = 1;
    private int currentIndex = 0;

    private Button greenButton;
    private Button redButton;
    private Button yellowButton;
    private Button blueButton;
    private Button startButton;

    private final Handler handler = new Handler();
    private final Random random = new Random();
    private boolean isGameStarted = false;
    private TextView text;

    private static final int[] BUTTON_COLORS = {R.color.green, R.color.red, R.color.yellow, R.color.blue};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Simon", "Simon fragment created");
        View root = inflater.inflate(R.layout.fragment_third, container, false);
        FragmentThirdBinding binding = FragmentThirdBinding.inflate(inflater, container, false);
        ConstraintLayout layout = binding.getRoot();
        text = root.findViewById(R.id.round);
        text.setVisibility(View.GONE);

        greenButton = root.findViewById(R.id.green);
        redButton = root.findViewById(R.id.red);
        yellowButton = root.findViewById(R.id.yellow);
        blueButton = root.findViewById(R.id.blue);
        startButton = root.findViewById(R.id.startSimon);
        greenButton.setOnClickListener(view -> onButtonClick(0));
        redButton.setOnClickListener(view -> onButtonClick(1));
        yellowButton.setOnClickListener(view -> onButtonClick(2));
        blueButton.setOnClickListener(view -> onButtonClick(3));



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Simon", "Start button pressed");
                isGameStarted = true;
                startNewGame();
            }
        });

        return root;
    }

    private void startNewGame() {
        Log.d("Simon", "Starting new game");
        startButton.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        String roundText = String.valueOf(round - 1);
        text.setText(roundText);
        currentIndex = 0;

        // Generate a new sequence of random button presses
        sequence.add(random.nextInt(4));

        // Play the sequence
        handler.postDelayed(() -> {
            playSequence();
        }, 1800);
    }

    private void playSequence() {
        int delay = 1000;

        for (int i = 0; i < sequence.size(); i++) {
            int finalI = i;
            int buttonIndex = sequence.get(finalI);
            int buttonColor = BUTTON_COLORS[buttonIndex];

            // Light up the button for a short time
            handler.postDelayed(() -> {
                lightButton(buttonIndex);
            }, delay * finalI + 500);

            // Turn off the button after a short delay
            handler.postDelayed(() -> {
                unlightButton(buttonIndex);
            }, delay * (finalI + 1));
        }
    }

    private void onButtonClick(int buttonIndex) {
        if (isGameStarted) {
            lightButton(buttonIndex);
            handler.postDelayed(() -> {
                unlightButton(buttonIndex);
            }, 500);
            int expectedIndex = sequence.get(currentIndex);

            if (buttonIndex == expectedIndex) {
                currentIndex++;

                if (currentIndex == sequence.size()) {
                    // Round completed, start a new round
                    round++;
                    currentIndex = 0;
                    startNewGame();
                }
            } else {
                // Wrong button pressed, game over
                Toast.makeText(getContext(), "Game over", Toast.LENGTH_SHORT).show();
                isGameStarted = false;
                startButton.setVisibility(View.VISIBLE);
                sequence.clear();
                round = 1;
            }
        }
    }

    private void lightButton(int buttonIndex) {
        switch (buttonIndex) {
            case 0:
                greenButton.setBackgroundResource(R.drawable.rounded_button_lightgreen);
                break;
            case 1:
                redButton.setBackgroundResource(R.drawable.rounded_button_lightred);
                break;
            case 2:
                yellowButton.setBackgroundResource(R.drawable.rounded_button_lightyellow);
                break;
            case 3:
                blueButton.setBackgroundResource(R.drawable.rounded_button_lightblue);
                break;
            default:
                break;
        }
    }

    private void unlightButton(int buttonIndex) {
        switch (buttonIndex) {
            case 0:
                greenButton.setBackgroundResource(R.drawable.rounded_button_green);
                break;
            case 1:
                redButton.setBackgroundResource(R.drawable.rounded_button_red);
                break;
            case 2:
                yellowButton.setBackgroundResource(R.drawable.rounded_button_yellow);
                break;
            case 3:
                blueButton.setBackgroundResource(R.drawable.rounded_button_blue);
                break;
            default:
                break;
        }
    }
}
package com.example.mygame;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mygame.databinding.FragmentSecondBinding;

import java.util.Random;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;
    private boolean isCountdownStarted = false;

    private int backgroundColor = 0;

    private boolean isGameOver = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        countdownTextView = binding.countdownTextView;
        ConstraintLayout layout = (ConstraintLayout) binding.getRoot();
        Button restartButton = layout.findViewById(R.id.restart);
        restartButton.setVisibility(View.INVISIBLE);
        final boolean[] clickable = {true};
        layout.setOnTouchListener(new View.OnTouchListener() {
            final SparseArray<CircleView> circleViews = new SparseArray<>();
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                int pointerCount = motionEvent.getPointerCount();
                int loserPointerId = -1;
                long loserTime = 0;
                if (isGameOver) {
                    Button restartButton = layout.findViewById(R.id.restart);
                    restartButton.setVisibility(View.VISIBLE);
                    clickable[0] = false;
                    Log.d("TAG", "onTouch2: " + clickable[0]);
                    isGameOver = false;
                }
                if (!clickable[0]) {
                    restartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("TAG", "onClick: ");
                            layout.setBackgroundColor(Color.TRANSPARENT);
                            countdownTextView.setText("");
                            isCountdownStarted = false;
                            countdownTextView.setTextColor(Color.WHITE);
                            countdownTextView.setTextSize(96);
                            int pointerCount = motionEvent.getPointerCount();
                            restartButton.setVisibility(View.INVISIBLE);
                            clickable[0] = true;
                            isGameOver = false;
                        }
                    });
                    return true;
                } else {
                    Button restartButton = layout.findViewById(R.id.restart);
                    restartButton.setVisibility(View.INVISIBLE);
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            if (!isCountdownStarted) {
                                startCountdown(binding);
                            }
                        case MotionEvent.ACTION_POINTER_DOWN:
                            pointerCount = motionEvent.getPointerCount();
                            for (int i = 0; i < pointerCount; i++) {
                                int pointerId = motionEvent.getPointerId(i);
                                if (!isCircleExistForPointerIndex(layout, pointerId)) {
                                    float x = motionEvent.getX(i);
                                    float y = motionEvent.getY(i);
                                    CircleView circleView = new CircleView(getContext(), x, y, 80, pointerId);
                                    circleView.setPointerIndex(i);
                                    layout.addView(circleView);
                                    circleViews.put(pointerId, circleView);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            for (int i = 0; i < pointerCount; i++) {
                                int pointerId = motionEvent.getPointerId(i);
                                CircleView circleView = circleViews.get(pointerId);
                                if (circleView != null) {
                                    circleView.updatePosition(motionEvent.getX(i), motionEvent.getY(i));
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            int pointerIndex = motionEvent.getActionIndex();
                            int pointerId = motionEvent.getPointerId(pointerIndex);
                            if (backgroundColor == Color.YELLOW) {
                                int nbgorgées = new Random().nextInt(5) + 1;
                                int is_cul_sec = new Random().nextInt(50);
                                loserPointerId = pointerId;
                                if (is_cul_sec == 1) {
                                    countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois cul sec");
                                } else if (nbgorgées == 1)
                                    countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois " + nbgorgées + " gorgée");
                                else
                                    countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois " + nbgorgées + " gorgées");
                                countdownTextView.setTextColor(Color.BLACK);
                                countdownTextView.setTextSize(40);
                                countdownTextView.setGravity(Gravity.CENTER);
                                //remove all circles
                                CircleView loserCircleView = null;
                                for (int i = 0; i < circleViews.size(); i++) {
                                    int key = circleViews.keyAt(i);
                                    CircleView circleView1 = circleViews.get(key);
                                    if(key == loserPointerId)
                                    {
                                        loserCircleView = circleView1;
                                    }
                                    else {
                                        layout.removeView(circleView1);
                                    }
                                }
                                if(loserCircleView != null) {
                                    Handler handler = new Handler();
                                    CircleView finalLoserCircleView = loserCircleView;
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            layout.removeView(finalLoserCircleView);
                                        }
                                    }, 2000);
                                }
                                circleViews.clear();
                                clickable[0] = false;
                                isGameOver = true;
                                restartButton.setVisibility(View.VISIBLE);
                            }
                        case MotionEvent.ACTION_CANCEL:
                            //if last player to remove finger

                            pointerIndex = motionEvent.getActionIndex();
                            pointerId = motionEvent.getPointerId(pointerIndex);
                            CircleView circleView = circleViews.get(pointerId);
                            if (circleView != null) {
                                if (motionEvent.getPointerCount() == 1 && backgroundColor == Color.GREEN) {
                                    //do nothing
                                } else
                                    layout.removeView(circleView);
                                //circleViews.remove(pointerId);
                                //if last player to remove finger on green
                                if (motionEvent.getPointerCount() == 1 && backgroundColor == Color.GREEN) {
                                    loserPointerId = pointerId;
                                    int nbgorgées = new Random().nextInt(5) + 1;
                                    int is_cul_sec = new Random().nextInt(50);
                                    if (is_cul_sec == 1) {
                                        countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois cul sec");
                                    } else if (nbgorgées == 1)
                                        countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois " + nbgorgées + " gorgée");
                                    else
                                        countdownTextView.setText("Joueur " + (loserPointerId + 1) + " bois " + nbgorgées + " gorgées");
                                    countdownTextView.setTextColor(Color.BLACK);
                                    countdownTextView.setTextSize(40);
                                    countdownTextView.setGravity(Gravity.CENTER);
                                    //remove all circles
                                    for (int i = 0; i < circleViews.size(); i++) {
                                        int key = circleViews.keyAt(i);
                                        CircleView circleView1 = circleViews.get(key);
                                        Log.d("TAG", "onTouch: " + i);
                                        Log.d("TAG", "onTouch: " + loserPointerId);
                                        if (i == loserPointerId) {
                                            // remove loser's circle after 2 seconds
                                            Log.d("TAG", "onTouch: " + "remove loser's circle");
                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {
                                                    layout.removeView(circleView1);
                                                }
                                            }, 2000);
                                        } else {
                                            layout.removeView(circleView1);
                                        }
                                    }
                                    isGameOver = true;
                                    //clickable[0] = false;
                                    restartButton.setVisibility(View.VISIBLE);
                                    Log.d("TAG", "onTouch: " + "game over");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            }
        });
        return binding.getRoot();
    }

    private int getRemainingPlayersCount(SparseArray<CircleView> circleViews) {
        int count = 0;
        for (int i = 0; i < circleViews.size(); i++) {
            int key = circleViews.keyAt(i);
            CircleView circleView = circleViews.get(key);
            if (circleView != null) {
                count++;
            }
        }
        return count;
    }

    private int[] getRemainingPlayers(SparseArray<CircleView> circleViews) {
        int[] remainingPlayers = new int[getRemainingPlayersCount(circleViews)];
        int index = 0;
        for (int i = 0; i < circleViews.size(); i++) {
            int key = circleViews.keyAt(i);
            CircleView circleView = circleViews.get(key);
            if (circleView != null) {
                remainingPlayers[index] = key;
                index++;
            }
        }
        return remainingPlayers;
    }

    private void startCountdown(FragmentSecondBinding binding) {
        final boolean[] alreadycounted = {isCountdownStarted};
        isCountdownStarted = true;
        countDownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!alreadycounted[0]) {
                    countdownTextView.setText("" + ((millisUntilFinished / 1000) + 1));
                }
            }
            public void onFinish() {
                countdownTextView.setText("");
                Handler handler = new Handler();
                int color = Color.BLACK;

                backgroundColor = color;
                ConstraintLayout layout = (ConstraintLayout) binding.getRoot();
                int delay = (new Random().nextInt(8) + 1) * 1000;
                int random = new Random().nextInt(20);
                if(random == 0)
                    delay = 1000;
                else if (random == 1 || random == 2){
                    delay = 2000;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int newColor = new Random().nextBoolean() ? Color.YELLOW : Color.GREEN;
                        backgroundColor = newColor;
                        layout.setBackgroundColor(newColor);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isGameOver || backgroundColor == Color.GREEN) {
                                    return;
                                } else {
                                    layout.setBackgroundColor(Color.TRANSPARENT);
                                    startCountdown(binding);
                                }
                            }
                        }, 1000);
                    }
                }, (delay));
            }
        }.start();
    }


    private boolean isCircleExistForPointerIndex(ConstraintLayout layout, int pointerIndex) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof CircleView) {
                CircleView circleView = (CircleView) child;
                if (circleView.getPointerIndex() == pointerIndex) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
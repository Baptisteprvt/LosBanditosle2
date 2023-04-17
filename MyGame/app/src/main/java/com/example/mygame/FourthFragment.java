package com.example.mygame;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class FourthFragment extends Fragment {
    private ImageView mRoulette;
    private boolean mIsSpinning = false;
    private float mTargetRotation = 0;
    private float finalRotation = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Roulette");
        mRoulette = view.findViewById(R.id.roulette_wheel);
        Button spinButton = view.findViewById(R.id.spin_button);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsSpinning) {
                    mIsSpinning = true;
                    Random random = new Random();
                    mTargetRotation = abs(mRoulette.getRotation() + random.nextInt(3000) + 2000);
                    RotateAnimation rotateAnimation = new RotateAnimation(finalRotation, mTargetRotation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(5000);
                    rotateAnimation.setFillAfter(true);
                    mRoulette.startAnimation(rotateAnimation);
                    finalRotation = mTargetRotation;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsSpinning = false;
                        }
                    }, 5000); // set the duration of the spin here
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsSpinning) {
            if (abs(mRoulette.getRotation() - mTargetRotation) < 10) {
                mIsSpinning = false;
            }
        }
    }
}
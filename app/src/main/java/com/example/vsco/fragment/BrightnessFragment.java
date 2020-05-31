package com.example.vsco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.vsco.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrightnessFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private BrightnessFragmentListener listener;

    @BindView(R.id.seekbar_brightness)
    SeekBar seekBarBrightness;

    public BrightnessFragment() {
    }

    public void setListener(BrightnessFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brightness, container, false);

        ButterKnife.bind(this, view);

        // keeping brightness value b/w -100 / +100
        seekBarBrightness.setMax(200);
        seekBarBrightness.setProgress(100);

        seekBarBrightness.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_brightness) {
                // brightness values are b/w -100 to +100
                listener.onBrightnessChanged(progress - 100);
            }

        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.onEditStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.onEditCompleted();
    }

    public void resetControls() {
        seekBarBrightness.setProgress(100);

    }
    public interface BrightnessFragmentListener {
        void onBrightnessChanged(int brightness);

        void onEditStarted();

        void onEditCompleted();
    }
}

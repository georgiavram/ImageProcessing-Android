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

public class VignetteFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private VignetteFragmentListener listener;

    @BindView(R.id.seekbar_radius)
    SeekBar seekBarRadius;

    public VignetteFragment() {
    }

    public void setListener(VignetteFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vignette, container, false);

        ButterKnife.bind(this, view);
        seekBarRadius.setMax(2);
        seekBarRadius.setProgress(0);

        seekBarRadius.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_radius) {
                progress += 10;
                float floatVal = .10f * progress;
                listener.onVignetteChanged(floatVal);
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
        seekBarRadius.setProgress(100);

    }

    public interface VignetteFragmentListener {
        void onVignetteChanged(float radius);

        void onEditStarted();

        void onEditCompleted();
    }
}

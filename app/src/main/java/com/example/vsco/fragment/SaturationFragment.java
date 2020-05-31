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

public class SaturationFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private SaturationFragmentListener listener;

    @BindView(R.id.seekbar_saturation)
    SeekBar seekBarSaturation;

    public SaturationFragment() {
    }

    public void setListener(SaturationFragmentListener listener) {
        this.listener = listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saturation, container, false);

        ButterKnife.bind(this, view);
        seekBarSaturation.setMax(100);
        seekBarSaturation.setProgress(50);

        seekBarSaturation.setOnSeekBarChangeListener(this);


        return view;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_saturation) {
                progress += 10;
                float floatVal = .10f * progress;
                listener.onSaturationChanged(progress-50);
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

    public interface SaturationFragmentListener {
        void onSaturationChanged(float saturation);

        void onEditStarted();

        void onEditCompleted();
    }
}

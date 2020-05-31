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

public class RGBFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private RGBFragmentListener listener;
    @BindView(R.id.seekbar_R)
    SeekBar seekBarRed;
    @BindView(R.id.seekbar_G)
    SeekBar seekBarGreen;
    @BindView(R.id.seekbar_B)
    SeekBar seekBarBlue;

    public RGBFragment() {
    }

    public void setListener(RGBFragmentListener listener) {
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rgb, container, false);

        ButterKnife.bind(this, view);

        // keeping brightness value b/w -100 / +100
        seekBarRed.setMax(50);
        seekBarRed.setProgress(25);
        seekBarGreen.setMax(50);
        seekBarGreen.setProgress(25);
        seekBarBlue.setMax(50);
        seekBarBlue.setProgress(25);

        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_R) {
                listener.onRedChanged(progress - 25);
            }

            if (seekBar.getId() == R.id.seekbar_G) {

                listener.onGreenChanged(progress - 25);
            }

            if (seekBar.getId() == R.id.seekbar_B) {
                listener.onBlueChanged(progress - 25);
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
        seekBarBlue.setProgress(6);
        seekBarRed.setProgress(6);
        seekBarGreen.setProgress(6);
    }

    public interface RGBFragmentListener {
        void onRedChanged(int red);
        void onGreenChanged(int green);
        void onBlueChanged(int blue);
        void onEditStarted();
        void onEditCompleted();
    }
}

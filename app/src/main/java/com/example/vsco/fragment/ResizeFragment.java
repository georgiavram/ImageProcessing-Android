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

public class ResizeFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private ResizeFragmentListener listener;
    @BindView(R.id.seekbar_height)
    SeekBar seekBarHeight;
    @BindView(R.id.seekbar_width)
    SeekBar seekBarWidth;

    private int height;
    private int width;

    private int currentHeight;
    private int currentWidth;

    public ResizeFragment(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void setListener(ResizeFragmentListener listener) {
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resize, container, false);
        currentWidth = width;
        currentHeight = height;
        ButterKnife.bind(this, view);
        seekBarHeight.setMax(height);
        seekBarHeight.setProgress(height);
        seekBarWidth.setMax(width);
        seekBarWidth.setProgress(width);

        seekBarHeight.setOnSeekBarChangeListener(this);
        seekBarWidth.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_height) {
                currentHeight = progress;
                listener.onResize(currentWidth, currentHeight);
            }

            if (seekBar.getId() == R.id.seekbar_width) {
                currentWidth = progress;
                listener.onResize(currentWidth, currentHeight);
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
        seekBarWidth.setProgress(width);
        seekBarHeight.setProgress(height);
    }

    public interface ResizeFragmentListener {
        void onResize(int width, int height);
        void onEditStarted();
        void onEditCompleted();
    }
}

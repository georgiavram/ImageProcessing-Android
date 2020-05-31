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

public class BlurFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private BlurFragmentListener listener;

    @BindView(R.id.seekbar_blur)
    SeekBar seekBarBlur;

    public BlurFragment() {
    }

    public void setListener(BlurFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blur, container, false);

        ButterKnife.bind(this, view);
        seekBarBlur.setMax(3);
        seekBarBlur.setProgress(0);

        seekBarBlur.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_blur) {
                listener.onBlurChanged(2*progress+1);
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
        seekBarBlur.setProgress(100);

    }
    public interface BlurFragmentListener {
        void onBlurChanged(int blur);

        void onEditStarted();

        void onEditCompleted();
    }
}

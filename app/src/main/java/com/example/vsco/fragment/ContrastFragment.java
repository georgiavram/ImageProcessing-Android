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

public class ContrastFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private ContrastFragmentListener listener;
    @BindView(R.id.seekbar_contrast)
    SeekBar seekBarContrast;

    public ContrastFragment() {
    }
    public void setListener(ContrastFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contrast, container, false);

        ButterKnife.bind(this, view);

        seekBarContrast.setMax(20);
        seekBarContrast.setProgress(0);

        seekBarContrast.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress += 10;
        float floatVal = .10f * progress;
        listener.onContrastChanged(floatVal);
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
        seekBarContrast.setProgress(0);
    }
    public interface ContrastFragmentListener {
        void onContrastChanged(float contrast);
        void onEditStarted();
        void onEditCompleted();
    }
}

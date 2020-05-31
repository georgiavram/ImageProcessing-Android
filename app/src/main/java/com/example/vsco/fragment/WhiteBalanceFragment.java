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

public class WhiteBalanceFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private WhiteBalanceFragmentListener listener;

    @BindView(R.id.seekbar_balance)
    SeekBar seekBarBalance;

    @BindView(R.id.seekbar_tint)
    SeekBar seekBarTint;

    public WhiteBalanceFragment() {
    }

    public void setListener(WhiteBalanceFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_white_balance, container, false);

        ButterKnife.bind(this, view);
        seekBarBalance.setMax(200);
        seekBarBalance.setProgress(100);

        seekBarTint.setMax(200);
        seekBarTint.setProgress(100);

        seekBarTint.setOnSeekBarChangeListener(this);
        seekBarBalance.setOnSeekBarChangeListener(this);


        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_balance) {
                listener.onWBChanged(progress-100);
            }
            if (seekBar.getId() == R.id.seekbar_tint) {
                listener.onTintChanged(progress-100);
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
        seekBarBalance.setProgress(100);
        seekBarTint.setProgress(100);

    }
    public interface WhiteBalanceFragmentListener {
        void onWBChanged(int wb);
        void onTintChanged(int tint);
        void onEditStarted();
        void onEditCompleted();
    }
}

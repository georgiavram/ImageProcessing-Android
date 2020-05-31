package com.example.vsco.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vsco.MenuActivity;
import com.example.vsco.R;
import com.example.vsco.adapter.EffectsAdapter;
import com.example.vsco.editing.Effect;
import com.example.vsco.manager.EffectsManager;
import com.example.vsco.utils.BitmapUtils;
import com.example.vsco.utils.SpacesItemDecoration;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FiltersListFragment extends Fragment implements EffectsAdapter.EffectsAdapterListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    EffectsAdapter mAdapter;

    List<Effect> thumbnailItemList;

    FiltersListFragmentListener listener;

    private Bitmap thumbnail;

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public FiltersListFragment(Bitmap bitmap) {
        this.thumbnail = bitmap;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);

        ButterKnife.bind(this, view);

        thumbnailItemList = new ArrayList<>();
        mAdapter = new EffectsAdapter(getActivity(), thumbnailItemList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(mAdapter);

        prepareThumbnail(null);

        return view;
    }

    /**
     * Renders thumbnails in horizontal list
     * loads default image from Assets if passed param is null
     *
     * @param bitmap
     */
    public void prepareThumbnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage;

                if (bitmap == null) {
                    thumbImage = thumbnail;
                } else {
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }

                if (thumbImage == null)
                    return;

                EffectsManager.clearThumbs();
                thumbnailItemList.clear();

                // add normal bitmap first
                Effect thumbnailItem = new Effect();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = getString(R.string.filter_normal);
                EffectsManager.addThumb(thumbnailItem);

                Effect t6 = new Effect();
                t6.image = thumbImage;
                t6.filterName = "M5";
                EffectsManager.addThumb(t6);

                Effect t7 = new Effect();
                t7.image = thumbImage;
                t7.filterName = "HB1";
                EffectsManager.addThumb(t7);

                Effect t8 = new Effect();
                t8.image = thumbImage;
                t8.filterName = "X1";
                EffectsManager.addThumb(t8);

                Effect t9 = new Effect();
                t9.image = thumbImage;
                t9.filterName = "C1";
                EffectsManager.addThumb(t9);

                Effect t10 = new Effect();
                t10.image = thumbImage;
                t10.filterName = "KU8";
                EffectsManager.addThumb(t10);

                Effect t11 = new Effect();
                t11.image = thumbImage;
                t11.filterName = "P5";
                EffectsManager.addThumb(t11);

                Effect tI = new Effect();
                tI.image = thumbImage;
                tI.filterName = "Sepia";
                EffectsManager.addThumb(tI);

                Effect t2 = new Effect();
                t2.image = thumbImage;
                t2.filterName = "Grayscale";
                EffectsManager.addThumb(t2);

                Effect t3 = new Effect();
                t3.image = thumbImage;
                t3.filterName = "Sketch";
                EffectsManager.addThumb(t3);

                Effect t4 = new Effect();
                t4.image = thumbImage;
                t4.filterName = "Negative";
                EffectsManager.addThumb(t4);

                Effect t5 = new Effect();
                t5.image = thumbImage;
                t5.filterName = "Equalize";
                EffectsManager.addThumb(t5);

                Effect t12 = new Effect();
                t12.image = thumbImage;
                t12.filterName = "Canny";
                EffectsManager.addThumb(t12);

                thumbnailItemList.addAll(EffectsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        new Thread(r).start();
    }


    @Override
    public void onFilterSelected(Effect effect) {
        if (listener != null)
            listener.onFilterSelected(effect);
    }

    public interface FiltersListFragmentListener {
        void onFilterSelected(Effect effect);
    }
}
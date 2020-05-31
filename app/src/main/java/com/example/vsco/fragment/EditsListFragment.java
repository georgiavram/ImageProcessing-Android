package com.example.vsco.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

//import com.example.vsco.MainActivity;
import com.example.vsco.MenuActivity;
import com.example.vsco.R;
import com.example.vsco.adapter.EditItemAdapter;
import com.example.vsco.utils.BitmapUtils;
import com.example.vsco.utils.SpacesItemDecoration;
import com.example.vsco.viewmodel.EditItem;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.sasikanth.colorsheet.ColorSheet;

import static com.example.vsco.adapter.EditItemAdapter.*;


public class EditsListFragment extends Fragment implements EditItemAdapter.EditItemAdapterListener, BrightnessFragment.BrightnessFragmentListener, ContrastFragment.ContrastFragmentListener, RGBFragment.RGBFragmentListener, SaturationFragment.SaturationFragmentListener, BlurFragment.BlurFragmentListener, VignetteFragment.VignetteFragmentListener, WhiteBalanceFragment.WhiteBalanceFragmentListener, CropFragment.CropFragmentListener, ResizeFragment.ResizeFragmentListener {

    @BindView(R.id.recycler_view_edits)
    RecyclerView recyclerView;

    EditItemAdapter mAdapter;

    //EDIT ICON
    List<EditItem> editItems;

    EditsListFragmentListener listener;

    Bitmap currentImage;

    public void setCurrentImage(Bitmap currentImage) {
        this.currentImage = currentImage;
    }

    //    public void setListener(FiltersListFragmentListener listener) {
//        this.listener = listener;
//    }
public void setListener(EditsListFragmentListener listener) {
    this.listener = listener;
}
    public EditsListFragment(Bitmap img) {
    this.currentImage = img;
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
        View view = inflater.inflate(R.layout.fragment_edits_list, container, false);

        ButterKnife.bind(this, view);
        editItems = new ArrayList<>();
        mAdapter = new EditItemAdapter(getActivity(), editItems, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
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
                    thumbImage = BitmapUtils.getBitmapFromAssets(getActivity(), MenuActivity.IMAGE_NAME, 100, 100);
                } else {
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }

                if (thumbImage == null)
                    return;

                //ThumbnailsManager.clearThumbs();
                editItems.clear();
//                EditItem item = new EditItem();
//                item.editName = "Exposure";
//                item.image = getResources().getDrawable(R.drawable.ic_exposure, null);
//                editItems.add(item);

                EditItem item2 = new EditItem();
                item2.editName = "Saturation";
                item2.image = getResources().getDrawable(R.drawable.ic_saturation, null);
                editItems.add(item2);

                EditItem item3 = new EditItem();
                item3.editName = "Brightness";
                item3.image = getResources().getDrawable(R.drawable.ic_brightness, null);
                editItems.add(item3);

                EditItem item4  = new EditItem();
                item4.editName = "Contrast";
                item4.image = getResources().getDrawable(R.drawable.ic_contrast, null);
                editItems.add(item4);

                EditItem item5  = new EditItem();
                item5.editName = "RGB";
                item5.image = getResources().getDrawable(R.drawable.ic_rgb, null);
                editItems.add(item5);

                EditItem item6  = new EditItem();
                item6.editName = "Crop";
                item6.image = getResources().getDrawable(R.drawable.ic_crop, null);
                editItems.add(item6);

                EditItem item12  = new EditItem();
                item12.editName = "Resize";
                item12.image = getResources().getDrawable(R.drawable.ic_expand, null);
                editItems.add(item12);


                EditItem item7 = new EditItem();
                item7.editName = "Flip Vertically";
                item7.image = getResources().getDrawable(R.drawable.ic_flip_vertically, null);
                editItems.add(item7);

                EditItem item8 = new EditItem();
                item8.editName = "Rotate";
                item8.image = getResources().getDrawable(R.drawable.ic_rotate, null);
                editItems.add(item8);

                EditItem item9 = new EditItem();
                item9.editName = "Blur";
                item9.image = getResources().getDrawable(R.drawable.ic_blur, null);
                editItems.add(item9);

                EditItem item10 = new EditItem();
                item10.editName = "Vignette";
                item10.image = getResources().getDrawable(R.drawable.ic_vignette, null);
                editItems.add(item10);

                EditItem item11 = new EditItem();
                item11.editName = "WB";
                item11.image = getResources().getDrawable(R.drawable.ic_temperature, null);
                editItems.add(item11);

                EditItem item13 = new EditItem();
                item13.editName = "Split";
                item13.image = getResources().getDrawable(R.drawable.ic_two, null);
                editItems.add(item13);

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
    public void onEditSelected(EditItem item){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if(listener != null){
            switch(item.editName){
                case "Brightness":
                    BrightnessFragment childFragment = new BrightnessFragment();
                    childFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, childFragment).commit();
                    break;
                case "Contrast":
                    ContrastFragment contrastFragment = new ContrastFragment();
                    contrastFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, contrastFragment).commit();
                    break;
                case "RGB":
                    RGBFragment rgbFragment = new RGBFragment();
                    rgbFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, rgbFragment).commit();
                    break;
                case "Flip Vertically":
                    transaction.replace(R.id.fragment_container, new Fragment()).commit();
                    listener.onFlipVertically();
                    break;
                case "Crop":
                    CropFragment cropFragment = new CropFragment(currentImage.getHeight(),currentImage.getWidth());
                    cropFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, cropFragment).commit();
                    break;
                case "Resize":
                    ResizeFragment resizeFragment = new ResizeFragment(currentImage.getHeight(),currentImage.getWidth());
                    resizeFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, resizeFragment).commit();
                    break;
                case "Rotate":
                    transaction.replace(R.id.fragment_container, new Fragment()).commit();
                    listener.onRotate();
                    break;
                case "Saturation":
                    SaturationFragment saturationFragment = new SaturationFragment();
                    saturationFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, saturationFragment).commit();
                    break;
                case "Blur":
                    BlurFragment blurFragment = new BlurFragment();
                    blurFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, blurFragment).commit();
                    break;
                case "Vignette":
                    VignetteFragment vignetteFragment = new VignetteFragment();
                    vignetteFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, vignetteFragment).commit();
                    break;
                case "WB":
                    WhiteBalanceFragment whiteBalanceFragment = new WhiteBalanceFragment();
                    whiteBalanceFragment.setListener(this);
                    transaction.replace(R.id.fragment_container, whiteBalanceFragment).commit();
                    break;
                case "Split":
                    listener.splitTone();
                    break;
                default:
                    break;
            }
            listener.onEditSelected(item);
        }
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        listener.onBrightnessChanged(brightness);
    }

    @Override
    public void onContrastChanged(float contrast) {
        listener.onContrastChanged(contrast);
    }

    @Override
    public void onRedChanged(int red) {
        listener.onRedChanged(red);
    }

    @Override
    public void onGreenChanged(int green) {
        listener.onGreenChanged(green);
    }

    @Override
    public void onBlueChanged(int blue) {
        listener.onBlueChanged(blue);
    }

    @Override
    public void onSaturationChanged(float saturation) {
        listener.onSaturationChanged(saturation);
    }

    @Override
    public void onBlurChanged(int blur) {
        listener.onBlurChanged(blur);
    }

    @Override
    public void onVignetteChanged(float radius) {
        listener.onVignetteChanged(radius);
    }

    @Override
    public void onWBChanged(int wb) {
        listener.onWBChnaged(wb);
    }

    @Override
    public void onTintChanged(int tint) {
        listener.onTintChanged(tint);
    }

    @Override
    public void onCrop(int width, int height) {
        listener.onCrop(width, height);
    }

    @Override
    public void onResize(int width, int height) {
        listener.onResize(width, height);
    }

    @Override
    public void onEditStarted() {

    }


    @Override
    public void onEditCompleted() {

    }


    public interface EditsListFragmentListener {
        void onEditSelected(EditItem item);
        void onBrightnessChanged(int brightness);
        void onContrastChanged(float contrast);
        void onRedChanged(int red);
        void onGreenChanged(int green);
        void onBlueChanged(int blue);
        void onTintChanged(int tint);
        void onFlipVertically();
        void onCrop(int width, int height);
        void onResize(int width, int height);
        void onRotate();
        void onSaturationChanged(float saturation);
        void onBlurChanged(int blur);
        void onVignetteChanged(float radius);
        void onWBChnaged(int wb);
        void onEditStarted();
        void onEditCompleted();
        void splitTone();
    }

}
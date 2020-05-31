package com.example.vsco;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.vsco.editing.Effect;
import com.example.vsco.editing.Processing;
import com.example.vsco.fragment.EditsListFragment;
import com.example.vsco.fragment.FiltersListFragment;
import com.example.vsco.viewmodel.EditItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.vsco.utils.BitmapUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MenuActivity extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditsListFragment.EditsListFragmentListener {

    private static final String TAG = MenuActivity.class.getSimpleName();

    public static final String IMAGE_NAME = "pic.jpg";

    public static final int SELECT_GALLERY_IMAGE = 101;

    private Processing filter = new Processing();

    @BindView(R.id.image_preview)
    ImageView imagePreview;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    Bitmap originalImage;
    // to backup image with filter applied
    Bitmap filteredImage;

    // the final image after applying
    // brightness, saturation, contrast
    Bitmap finalImage;

    FiltersListFragment filtersListFragment;
    EditsListFragment editsListFragment;

    // modified image values
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;

    int redFinal = 0;
    int blueFinal = 0;
    int greenFinal = 0;

    int wbFinal = 0;
    int tintFinal = 0;

    // load native image filters library
    static {
        System.loadLibrary("edits");
        System.loadLibrary("opencv_java3");
        System.loadLibrary("NativeImageProcessor");
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        ButterKnife.bind(this);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getString(R.string.activity_title_main));
        imagePreview.setDrawingCacheEnabled(true);
//        loadImage();

        Bundle bundle = getIntent().getExtras();
        if(bundle.get("image") != null){
            Bitmap selected;
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            selected = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imagePreview.setImageBitmap(selected);
            originalImage = selected.copy(Bitmap.Config.ARGB_8888, true);
            assert originalImage != null;
            filteredImage = selected.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = selected.copy(Bitmap.Config.ARGB_8888, true);
        }
        else{
           Uri myUri = Uri.parse(bundle.getString("path"));
           System.out.println(myUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(bitmap);
        }
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // adding filter list fragment
        filtersListFragment = new FiltersListFragment(originalImage);
        filtersListFragment.setListener(this);

        editsListFragment = new EditsListFragment(originalImage);
        editsListFragment.setListener(this);
        //adjust


        adapter.addFragment(filtersListFragment, getString(R.string.presets));
        adapter.addFragment(editsListFragment, getString(R.string.edit));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFilterSelected(Effect effect) {
        // reset image controls
        resetControls();

        // applying the selected filter
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        Bitmap bmp32 = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
        Mat input = effect.process(effect.filterName, bmp32);
        Bitmap out = Bitmap.createBitmap(bmp32.getWidth(), bmp32.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, out);
        imagePreview.setImageBitmap(out);
        finalImage = out;
    }

    private void resetControls() {
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;
    }

    @Override
    public void onEditSelected(EditItem item) {
        if(item.editName.equals("Brightness")){

        }
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.brightnessFilter(input.nativeObj,brightness);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onContrastChanged(float contrast) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.contrastFilter(input.nativeObj,contrast);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onRedChanged(int red) {
        redFinal = red;
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.rgbFilter(input.nativeObj,redFinal, greenFinal, blueFinal);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onGreenChanged(int green) {
        greenFinal = green;
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.rgbFilter(input.nativeObj,redFinal, greenFinal, blueFinal);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onBlueChanged(int blue) {
        blueFinal = blue;
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.rgbFilter(input.nativeObj,redFinal, greenFinal, blueFinal);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onTintChanged(int tint) {
        tintFinal = tint;
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.wbFilter(input.nativeObj,wbFinal, tintFinal);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);

    }

    @Override
    public void onFlipVertically() {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.flipVertically(input.nativeObj);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
        finalImage = img_bitmap;
    }

    @Override
    public void onCrop(int width, int height) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC4);
        Mat out = new Mat(height, width, CvType.CV_8UC4);
        if(width == finalImage.getWidth() && height == finalImage.getHeight()){
            imagePreview.setImageBitmap(finalImage);
        }
        else{
            Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, input);
            if(width > 0 && height > 0 ){
                Processing.crop(input.nativeObj, out.nativeObj, width, height);
                Bitmap img_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(out, img_bitmap);
                imagePreview.setImageBitmap(img_bitmap);
            }
        }


    }

    @Override
    public void onResize(int width, int height) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC4);
        Mat out = new Mat(height, width, CvType.CV_8UC4);
        if(width == finalImage.getWidth() && height == finalImage.getHeight()){
            imagePreview.setImageBitmap(finalImage);
        }
        else{
            Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, input);
            if(width > 0 && height > 0 ){
                Processing.resize(input.nativeObj, out.nativeObj, width, height);
                Bitmap img_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(out, img_bitmap);
                imagePreview.setImageBitmap(img_bitmap);
            }
        }
    }


    @Override
    public void onRotate() {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC4);
        Mat output = new Mat(finalImage.getWidth(), finalImage.getHeight(), CvType.CV_8UC4);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        System.out.println(input);
        System.out.println(output);
        Processing.rotate(input.nativeObj, output.nativeObj);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getHeight(), finalImage.getWidth(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
        finalImage = img_bitmap;
    }

    @Override
    public void onSaturationChanged(float saturation) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.saturationFilter(input.nativeObj, saturation);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onBlurChanged(int blur) {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC4);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.blurFilter(input.nativeObj, blur);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onVignetteChanged(float radius) {
        System.out.println("vignette");
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.vignetteFilter(input.nativeObj, radius);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onWBChnaged(int wb) {
        System.out.println(wb + "   "+tintFinal);
        wbFinal = wb;
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.wbFilter(input.nativeObj,wbFinal, tintFinal);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {

    }

    @Override
    public void splitTone() {
        Mat input = new Mat(finalImage.getHeight(), finalImage.getWidth(), CvType.CV_8UC3);
        Bitmap bmp32 = finalImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, input);
        Processing.splitFilter(input.nativeObj);
        Bitmap img_bitmap = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, img_bitmap);
        imagePreview.setImageBitmap(img_bitmap);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // load the default image from assets on app launch
    private void loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, IMAGE_NAME, 300, 300);
        assert originalImage != null;
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        imagePreview.setImageBitmap(originalImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_open) {
            openImageFromGallery();
            return true;
        }

        if (id == R.id.action_save) {
            saveImageToGallery();
            return true;
        }

        if (id == R.id.action_apply) {
            finalImage = ((BitmapDrawable)imagePreview.getDrawable()).getBitmap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY_IMAGE) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

            // clear bitmap memory
            originalImage.recycle();
            finalImage.recycle();
            finalImage.recycle();

            originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(originalImage);
            bitmap.recycle();
            editsListFragment.setCurrentImage(originalImage);

            // render selected image thumbnails
            filtersListFragment.prepareThumbnail(originalImage);
        }
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_GALLERY_IMAGE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /*
     * saves image to camera gallery
     * */
    private void saveImageToGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            final String path = BitmapUtils.insertImage(getContentResolver(), finalImage, System.currentTimeMillis() + "_profile.jpg", null);
                            if (!TextUtils.isEmpty(path)) {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Image saved to gallery!", Snackbar.LENGTH_LONG)
                                        .setAction("OPEN", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openImage(path);
                                            }
                                        });

                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Unable to save image!", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    // opening image in default image viewer app
    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

}
//
// Created by gavra on 5/23/2020.
//

#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <cmath>
#include <queue>

using namespace std;
using namespace cv;

extern "C"{

int min(int a, int b){
     if(a < b) return a;
     else return b;
 }

int max(int a, int b){
    if(a < b) return b;
    else return a;
}

float minF(float a, float b){
    if(a < b) return a;
    else return b;
}

float maxF(float a, float b){
    if(a < b) return b;
    else return a;
}

void applyBrightness(Mat_<cv::Vec4b> &in, int factor){
     for(int i=0; i<in.rows; i++){
         for(int j=0; j<in.cols; j++){
             if(factor >= 0){
                 in(i,j)[0] = min( 255, in(i,j)[0]+factor);
                 in(i,j)[1] = min( 255, in(i,j)[1]+factor);
                 in(i,j)[2] = min( 255, in(i,j)[2]+factor);
             }
             else{
                 in(i,j)[0] = max( 0, in(i,j)[0]+factor);
                 in(i,j)[1] = max( 0, in(i,j)[1]+factor);
                 in(i,j)[2] = max( 0, in(i,j)[2]+factor);
             }
         }
     }
 }


void
Java_com_example_vsco_editing_Processing_brightnessFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        int factor){
        cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
        applyBrightness(img, factor);
 }

 void applyContrast(Mat_<cv::Vec4b> src, float contrast){
     for(int i=0; i<src.rows; i++){
         for(int j=0; j<src.cols; j++){
             src(i,j)[0] = min( 255, src(i,j)[0]*contrast);
             src(i,j)[1] = min( 255, src(i,j)[1]*contrast);
             src(i,j)[2] = min( 255, src(i,j)[2]*contrast);
         }
     }
 }
void applyGamma(Mat_<cv::Vec4b> src, float contrast){
    for(int i=0; i<src.rows; i++){
        for(int j=0; j<src.cols; j++){
            src(i,j)[0] = min( 255, (int) 255*pow(src(i,j)[0]/255.0, contrast));
            src(i,j)[1] = min( 255, (int) 255*pow(src(i,j)[1]/255.0, contrast));
            src(i,j)[2] = min( 255, (int) 255*pow(src(i,j)[2]/255.0, contrast));
        }
    }
}


void
Java_com_example_vsco_editing_Processing_contrastFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        float contrast){
        cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
//        applyContrast(img, contrast);
        applyGamma(img, contrast);
 }

 void applyRGB(Mat_<cv::Vec4b> &src, int red, int green, int blue){
     for(int i=0; i<src.rows; i++){
         for(int j=0; j<src.cols; j++){
             if(blue > 0){
                 src(i,j)[2] = min( 255, src(i,j)[2]+blue);
             }
             else{
                 src(i,j)[2] = max( 0, src(i,j)[2]+blue);

             }
             if(red > 0){
                 src(i,j)[0] = min( 255, src(i,j)[0]+red);
             }
             else{
                 src(i,j)[0] = max( 0, src(i,j)[0]+red);
             }
             if(green > 0){
                 src(i,j)[1] = min( 255, src(i,j)[1]+green);
             }
             else{
                 src(i,j)[1] = max( 0, src(i,j)[1]+green);
             }

         }
     }
 }
void
Java_com_example_vsco_editing_Processing_rgbFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        int red,
        int green,
        int blue
        ){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applyRGB(img, red, green, blue);
}

void applyFlipVertically(Mat_<cv::Vec4b> src){
    for (int i = 0; i < src.rows; i++) {
        for (int j = 0; j < src.cols/2; j++) {
            Vec4b aux = src(i, j);
            src(i, j) = src(i, src.cols - j);
            src(i, src.cols - j) = aux;
        }
    }
 }
void

Java_com_example_vsco_editing_Processing_flipVertically(
        JNIEnv* env,
        jclass clazz,
        jlong in
){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applyFlipVertically(img);
}
void applyCrop(Mat_<cv::Vec4b> &src, Mat_<cv::Vec4b> &dst, int height, int width){
     if(width == src.cols && height == src.rows) { dst = src; return;}
    for (int i = 0; i < dst.rows; i++) {
        for (int j = 0; j < dst.cols; j++) {
            dst(i, j) = src(src.rows / 2 - (height/2) + i, src.cols / 2 - (width/2) + j);
        }
    }
}
void
Java_com_example_vsco_editing_Processing_crop(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        jlong out,
        int width,
        int height
){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    cv::Mat_<cv::Vec4b>& dst = *(cv::Mat_<cv::Vec4b>*) out;
    applyCrop(img, dst, height, width);
//    img = dst;
}

void applyRotate(Mat_<cv::Vec4b>& src, Mat_<cv::Vec4b>& dst) {
    for(int i=0; i< src.rows; i++){
        for(int j=0; j<src.cols; j++){
            dst(src.cols-1-j,i) = src(i,j);
        }
    }
}
void
Java_com_example_vsco_editing_Processing_rotate(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        jlong out
        ){
        cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
        cv::Mat_<cv::Vec4b>& dst = *(cv::Mat_<cv::Vec4b>*) out;
        applyRotate(img, dst);
}
void applySaturation(Mat_<cv::Vec4b> &img, float saturation){
    float r, g, b,M,m,C;
    Mat_<float> H_norm(img.rows, img.cols);
    Mat_<float> S_norm(img.rows, img.cols);
    Mat_<float> V_norm(img.rows, img.cols);
    Mat_<Vec3b> hsv(img.rows, img.cols);
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            float H, S, V;
            //r = 0; g = 0; b = 0; M = 0; m = 0; C = 0;
            r = (float) img(i, j)[0] / 255.0;
            g = (float) img(i, j)[1] / 255.0;
            b = (float) img(i, j)[2] / 255.0;
            M = maxF(maxF(r, g), b);
            m = minF(minF(r, g), b);
            C = M - m;
            //value
            V = M;
            //saturation
            if (V != 0) {
                S = (float) C / V;
            }
            else {
                S = 0;
            }
            //hue
            if (C != 0) {
                if (M == r) H = 60 * ((g - b) / C);
                if (M == g) H = 120 + (60 * (b - r) / C);
                if (M == b) H = 240 + (60 * (r - g) / C);
            }
            else {
                H = 0;
            }
            if (H < 0) {
                H = H + 360;
            }

            H_norm(i, j) = H *180/360;
            S_norm(i, j) = S * 255;
            V_norm(i, j) = V * 255;

            hsv(i, j)[2] = V_norm(i, j);
            hsv(i, j)[1] =  max(0,min(255, S_norm(i, j) + saturation));
            hsv(i, j)[0] = H_norm(i, j);
        }
    }
    Mat_<Vec3b> d;
    cvtColor(hsv, d, CV_HSV2RGB);
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i,j)[0]= d(i,j)[0];
            img(i,j)[1] = d(i,j)[1];
            img(i,j)[2] = d(i,j)[2];
        }
    }


 }
void
Java_com_example_vsco_editing_Processing_saturationFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        float saturation
){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applySaturation(img, saturation);
}
bool isInside(Mat img, int row, int col) {
    if (row < img.rows && col < img.cols && row >= 0 && col >=0) {
        return true;
    }
    else {
        return false;
    }
}
void convolution(Mat_<float> &filter, Mat_<Vec4b> &img, Mat_<Vec4b> &output) {

    output.create(img.size());
    memcpy(output.data, img.data, img.rows * img.cols * sizeof(Vec4b));

    int scalingCoeff = 1;
    bool isHighPass = false;
    for (int i = 0; i < filter.rows; i++) {
        for (int j = 0; j < filter.cols; j++) {
            if (filter(i, j) < 0) {
                isHighPass = true;
            }
        }
    }
    if (isHighPass) {
    }
    else {
        float sum=0;
        for (int i = 0; i < filter.rows; i++) {
            for (int j = 0; j < filter.cols; j++) {
                sum += filter(i,j);
            }
        }
        scalingCoeff = sum;
    }

    if (isHighPass) {
        int w = filter.cols;
        int k = filter.cols / 2;
        for (int i = 0; i < output.rows; i++) {
            for (int j = 0; j < output.cols; j++) {
                float sumRed = 0, sumGreen=0, sumBlue=0;
                for (int u = 0; u < filter.rows; u++) {
                    for (int v = 0; v < filter.cols; v++) {
                        if (isInside(img, i + u - k, j + v - k)) {;
                            sumRed += filter(u, v)* img(i + u - k, j + v - k)[0];
                            sumGreen += filter(u, v)* img(i + u - k, j + v - k)[1];
                            sumBlue += filter(u, v)* img(i + u - k, j + v - k)[2];
                        }
                    }
                }
                sumRed = max(0,min(255, sumRed));
                sumGreen = max(0,min(255, sumGreen));
                sumBlue= max(0,min(255, sumBlue));

                output(i, j)[0] = sumRed;
                output(i, j)[1] = sumGreen;
                output(i, j)[2] = sumBlue;
            }
        }
    }
    else {
        int w = filter.cols;
        int k = filter.cols / 2;
        for (int i = 0; i < output.rows; i++) {
            for (int j = 0; j < output.cols; j++) {
                float sumRed = 0, sumGreen=0, sumBlue=0;
                for (int u = 0; u < filter.rows; u++) {
                    for (int v = 0; v < filter.cols; v++) {
                        if (isInside(img, i + u - k, j + v - k)) {
                            sumRed += filter(u, v)* img(i + u - k, j + v - k)[0];
                            sumGreen += filter(u, v)* img(i + u - k, j + v - k)[1];
                            sumBlue += filter(u, v)* img(i + u - k, j + v - k)[2];
                        }

                    }
                }
                output(i, j)[0] = sumRed / scalingCoeff;
                output(i, j)[1] = sumGreen / scalingCoeff;
                output(i, j)[2] = sumBlue / scalingCoeff;
            }
        }
    }
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i,j)[0]= output(i,j)[0];
            img(i,j)[1] = output(i,j)[1];
            img(i,j)[2] = output(i,j)[2];
        }
    }

}
void applyBlur(Mat_<cv::Vec4b>& img, int blur){
    float gaussianFilterData[9] = { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
    float laPlaceFilterData[9] = { -1, -1, -1, -1, 8, -1, -1, -1, -1 };
    Mat_<float> gaussianFilter(3, 3, gaussianFilterData);
    Mat_<float> laPlaceFilter(3, 3, laPlaceFilterData);
    Mat_<Vec4b> out(img.rows, img.cols);
    float meanFilterData[blur];
    fill_n(meanFilterData, blur*blur, 1);
    Mat_<float> meanFilter(blur, blur, meanFilterData);
    convolution(gaussianFilter, img, out);
 }



// Helper function to calculate the distance between 2 points.
double dist(CvPoint a, CvPoint b)
{
    return sqrt(pow((double) (a.x - b.x), 2) + pow((double) (a.y - b.y), 2));
}

// Helper function that computes the longest distance from the edge to the center point.
double getMaxDisFromCorners(const cv::Size& imgSize, const cv::Point& center)
{
    // given a rect and a line
    // get which corner of rect is farthest from the line

    std::vector<cv::Point> corners(4);
    corners[0] = cv::Point(0, 0);
    corners[1] = cv::Point(imgSize.width, 0);
    corners[2] = cv::Point(0, imgSize.height);
    corners[3] = cv::Point(imgSize.width, imgSize.height);

    double maxDis = 0;
    for (int i = 0; i < 4; ++i)
    {
        double dis = dist(corners[i], center);
        if (maxDis < dis)
            maxDis = dis;
    }

    return maxDis;
}

// Helper function that creates a gradient image.
// firstPt, radius and power, are variables that control the artistic effect of the filter.
void generateGradient(cv::Mat& mask, float r)
{
    cv::Point firstPt = cv::Point(mask.size().width/2, mask.size().height/2);
    double radius = r;
    double power = 0.8;

    double maxImageRad = radius * getMaxDisFromCorners(mask.size(), firstPt);

    mask.setTo(cv::Scalar(1));
    for (int i = 0; i < mask.rows; i++)
    {
        for (int j = 0; j < mask.cols; j++)
        {
            double temp = dist(firstPt, cv::Point(j, i)) / maxImageRad;
            temp = temp * power;
            double temp_s = pow(cos(temp), 4);
            mask.at<double>(i, j) = temp_s;
        }
    }
}

void applyVignette(Mat_<cv::Vec4b> &src, float radius){
    cv::Mat_<double> maskImg(src.rows, src.cols);
    generateGradient(maskImg,radius);
    cv::Mat labImg(src.size(), CV_8UC3);
    cv::Mat_<Vec3b> temp(src.rows, src.cols);
    for(int i=0; i<src.rows;i++){
        for(int j=0; j<src.cols; j++){
            for(int c=0; c<3;c++){
                temp(i,j)[c] = src(i,j)[c];
            }
        }
    }
    cv::cvtColor(temp, labImg, CV_RGB2Lab);

    for (int row = 0; row < labImg.size().height; row++)
    {
        for (int col = 0; col < labImg.size().width; col++)
        {
            cv::Vec3b value = labImg.at<cv::Vec3b>(row, col);
            value.val[0] *= maskImg.at<double>(row, col);
            labImg.at<cv::Vec3b>(row, col) =  value;
        }
    }

    cv::Mat output;
    cv::cvtColor(labImg, temp, CV_Lab2RGB);
    for(int i=0; i<src.rows;i++){
        for(int j=0; j<src.cols; j++){
            for(int c=0; c<3;c++){
                 src(i,j)[c] = temp(i,j)[c];
            }
        }
    }
 }
extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_vignetteFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        float radius
){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applyVignette(img, radius);
}

void applyWB(Mat_<Vec4b>& img, int wb, int tint){
     for(int i=0; i<img.rows;i++){
         for(int j=0; j<img.cols; j++){
             img(i,j)[0] = max(0,min(255, img(i,j)[0] + wb));
             img(i,j)[2] = max(0,min(255, img(i,j)[2] - wb));
             img(i,j)[1] = max(0,min(255, img(i,j)[1] + tint));
         }
     }
 }
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_wbFilter(
        JNIEnv* env,
        jclass clazz,
        jlong in,
        int wb,
        int tint
){
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applyWB(img, wb, tint);
}

void applyGrayscale(Mat_<Vec4b> &img){
     int sum=0;
    for(int i=0; i<img.rows;i++){
      for(int j=0; j<img.cols; j++){
          sum = (img(i,j)[0] + img(i,j)[1] + img(i,j)[2])/3;
          img(i,j)[0] = sum;
          img(i,j)[1] = sum;
          img(i,j)[2] = sum;
      }
    }
 }
}

extern "C" void
Java_com_example_vsco_editing_Processing_blurFilter(JNIEnv *env, jclass clazz, jlong in, int blur) {
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applyBlur(img, blur);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_grayscaleFilter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement grayscaleFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyGrayscale(img);
}extern "C"

void applySepia(Mat_<Vec4b> &img) {
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            int R = img(i, j)[0];
            int G = img(i, j)[1];
            int B = img(i, j)[2];

            img(i, j)[0] = max(0, min(255,0.393 * R + 0.769 * G + 0.189 * B));
            img(i, j)[1] = max(0, min(255,0.349 * R + 0.686 * G + 0.168 * B));
            img(i, j)[2] = max(0, min(255,0.272 * R + 0.534 * G + 0.131 * B));
        }

    }
}
extern "C" JNIEXPORT void JNICALL
    Java_com_example_vsco_editing_Processing_sepiaFilter(JNIEnv *env, jclass clazz, jlong input) {
        // TODO: implement sepiaFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applySepia(img);
    }

int findThreshold(Mat_<Vec4b> &img){
    int maxI = 0, minI = 256;
    int h[256] = {0};
    for (int i = 0; i < img.rows; i++)
    {
        for (int j = 0; j < img.cols; j++) {
            h[img(i, j)[0]]++;

            if (maxI < img(i, j)[0]) {
                maxI = img(i, j)[0];
            }
            if (minI > img(i, j)[0]) {
                minI = img(i, j)[0];
            }
        }
    }
    int T = (minI + maxI) / 2;
    int Tk = T;
    do {
        T = Tk;
        int N1 = 0, N2 = 0;
        for (int g = minI; g <= T; g++) {
            N1 += h[g];
        }
        for (int g = T + 1; g <= maxI; g++) {
            N2 += h[g];
        }
        float meanG1, meanG2;
        int sum = 0;
        for (int g = minI; g <= T; g++) {
            sum += g * h[g];
        }
        meanG1 = sum / N1;
        sum = 0;
        for (int g = T + 1; g <= maxI; g++) {
            sum += g * h[g];
        }
        meanG2 = sum / N2;
        Tk = (meanG1 + meanG2) / 2;
    } while (abs(Tk - T) > 0.1);
    return Tk;
}
void applySketch(Mat_<Vec4b> &img){
    applyGrayscale(img);
   int Tk = findThreshold(img);
    for(int i=0; i<img.rows; i++){
        for(int j=0; j<img.cols; j++){
            if(img(i,j)[0] > Tk){
                img(i,j)[0] = 255;
                img(i,j)[1] = 255;
                img(i,j)[2] = 255;
            }
            else{
                img(i,j)[0] = 0;
                img(i,j)[1] = 0;
                img(i,j)[2] = 0;
            }
        }
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_sketchFilter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement sketchFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applySketch(img);
}extern "C"

void applyNegative(Mat_<Vec4b> &img){
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i, j)[0] = 255 - img(i,j)[0];
            img(i, j)[1] = 255 - img(i,j)[1];
            img(i, j)[2] = 255 - img(i,j)[2];
        }

    }
}
extern "C" JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_negativeFilter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement negativeFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyNegative(img);
}


void sobel(Mat_<float>& filter, Mat_<uchar>& img, Mat_<int>& output) {
    int w = filter.cols;
    int k = w / 2;

    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            float sum = 0;
            for (int u = 0; u < w; u++) {
                for (int v = 0; v < w; v++) {
                    if (isInside(img, i + u - k, j + v - k)) {
                        sum += filter.at<float>(u, v) * img(i + u - k, j + v - k);
                    }
                }
            }
            output(i, j) = sum;
        }
    }
}
#define PI CV_PI
void gaussian2d(Mat_<Vec4b> &img) {
    int w = 3;
    w = 5;
    //w = 7;
    int c = w / 2;
    float dev = (float)w / 6.0;

    Mat_<float> filter(w, w);
    for (int x = 0; x < w; x++) {
        for (int y = 0; y < w; y++) {
            filter.at<float>(x, y) = exp(-(pow(x - c, 2) + pow(y - c, 2)) / (2 * dev * dev)) / (2 * PI * dev * dev);
        }
    }
    Mat_<Vec4b> dest(img.rows, img.cols);
    convolution(filter, img, dest);
}


void convolution2(Mat_<float>& filter, Mat_<Vec4b>& img, Mat_<uchar>& output) {

    output.create(img.size());
    memcpy(output.data, img.data, img.rows * img.cols * sizeof(uchar));

    float scalingCoeff = 1;
    float additionFactor = 0;

    bool isHighPass = false; //false for low pass, true
    float neg = 0, pos = 0, nsum = 0, psum = 0;
    for (int i = 0; i < filter.rows; i++) {
        for (int j = 0; j < filter.cols; j++) {
            if (filter.at<float>(i, j) < 0) {
                neg++;
                nsum += filter.at<float>(i, j);
                isHighPass = true;
            }
            else {
                pos++;
                psum += filter.at<float>(i, j);
            }
        }
    }
    float C = nsum + psum;
    if (isHighPass) {
        scalingCoeff = 1 / (2 * max(psum, abs(nsum)));
        additionFactor = 0;
    }
    else {
        additionFactor = 0;
        float sum = 0;
        for (int i = 0; i < filter.rows; i++) {
            for (int j = 0; j < filter.cols; j++) {
                sum += filter.at<float>(i, j);
            }
        }
        scalingCoeff = sum;
    }

    if (isHighPass) {
        int w = filter.cols;
        int k = filter.cols / 2;
        for (int i = 0; i < output.rows; i++) {
            for (int j = 0; j < output.cols; j++) {
                float sum = 0;
                for (int u = 0; u < filter.rows; u++) {
                    for (int v = 0; v < filter.cols; v++) {
                        if (isInside(img, i + u - k, j + v - k)) {
                            sum += filter.at<float>(u, v) * img.at<Vec4b>(i + u - k, j + v - k)[0];
                        }

                    }

                }
                if (sum > 255) {
                    sum = 255;
                }
                else {
                    if (sum < 0) {
                        sum = 0;
                    }
                }
                output.at<uchar>(i, j) = sum;
            }
        }
    }
    else {
        int w = filter.cols;
        int k = filter.cols / 2;
        for (int i = 0; i < output.rows; i++) {
            for (int j = 0; j < output.cols; j++) {
                int sum = 0;
                for (int u = 0; u < filter.rows; u++) {
                    for (int v = 0; v < filter.cols; v++) {
                        if (isInside(img, i + u - k, j + v - k)) {
                            sum += filter.at<float>(u, v) * img.at<Vec4b>(i + u - k, j + v - k)[0];
                        }

                    }

                }
                output.at<uchar>(i, j) = sum / scalingCoeff;
                output.at<uchar>(i, j) += additionFactor;
            }
        }
    }

    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i,j)[0] = output.at<uchar>(i,j);
            img(i,j)[1] = output.at<uchar>(i,j);
            img(i,j)[2] = output.at<uchar>(i,j);
        }
    }

}

#define NON_EDGE 0
#define WEAK_EDGE 128
#define STRONG_EDGE 255

int ni8[] = { -1,-1,-1,0,0,1,1,1 };
int nj8[] = { -1,0,1,-1,1,-1,0,1 };
void cannyEdge(Mat_<Vec4b> src) {

    applyGrayscale(src);
    int w = 3;
    int c = w / 2;
    float dev = 0.5;
    Mat_<uchar> out = src;
    Mat_<float> filter(w, w);
    for (int x = 0; x < w; x++) {
        for (int y = 0; y < w; y++) {
            filter.at<float>(x, y) =
                    exp(-(pow(x - c, 2) + pow(y - c, 2)) / (2 * dev * dev)) / (2 * PI * dev * dev);
        }
    }
    convolution2(filter, src, out);

    Mat_<int> sobelx(src.rows, src.cols);
    Mat_<int> sobely(src.rows, src.cols);

    float sobelxData[9] = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
    float sobelyData[9] = {1, 2, 1, 0, 0, 0, -1, -2, -1};

    Mat_<float> sobelfx(3, 3, sobelxData);
    Mat_<float> sobelfy(3, 3, sobelyData);
    sobel(sobelfx, out, sobelx);
    sobel(sobelfy, out, sobely);

    Mat_<uchar> modul(src.rows, src.cols);
    Mat_<float> directie(src.rows, src.cols);

    for (int x = 0; x < src.rows; x++) {
        for (int y = 0; y < src.cols; y++) {
            modul.at<uchar>(x, y) =
                    sqrt(pow(sobelx.at<int>(x, y), 2) + pow(sobely.at<int>(x, y), 2)) /
                    (4 * sqrt(2));
            directie.at<float>(x, y) = atan2(sobely.at<int>(x, y), sobelx.at<int>(x, y)) + PI;
        }
    }
    Mat_<uchar> copyModul = modul;

    for (int x = 1; x < src.rows - 1; x++) {
        for (int y = 1; y < src.cols - 1; y++) {
            float dir = directie.at<float>(x, y);
            uchar n, np;
            float p = PI / 8;
            if ((dir > p && dir <= 3 * p) || (dir > 9 * p && dir <= 11 * p)) {
                //green
                n = copyModul.at<uchar>(x - 1, y + 1);
                np = copyModul.at<uchar>(x + 1, y - 1);
            } else {
                if ((dir > 3 * p && dir <= 5 * p) ||( (dir > 11 * p && dir <= 13 )* p)) {
                    //red
                    n = copyModul.at<uchar>(x - 1, y);
                    np = copyModul.at<uchar>(x + 1, y);
                } else {
                    if ((dir > 5 * p && dir <= 7 * p) || (dir > 13 * p && dir <= 15 * p)) {
                        //yellow
                        n = copyModul.at<uchar>(x + 1, y + 1);
                        np = copyModul.at<uchar>(x - 1, y - 1);
                    } else {
                        //blue
                        n = copyModul.at<uchar>(x, y + 1);
                        np = copyModul.at<uchar>(x, y - 1);
                    }
                }
            }
            if (modul.at<uchar>(x, y) < n || modul.at<uchar>(x, y) < np) {
                modul.at<uchar>(x, y) = 0;
            }
        }
    }
    int hist[256] = {0};
    for (int i = 1; i < src.rows - 1; i++) {
        for (int j = 1; j < src.cols - 1; j++) {
            hist[modul.at<uchar>(i, j)]++;
        }
    }
    int zeroGradientModulePixels = hist[0];
    float p = 0.1;
    int nrNonEdgePixels = (1.0 - p) * ((src.rows - 2) * (src.cols - 2) - zeroGradientModulePixels);
    int sum = 0;
    int thH;
    float thL;

    for (int i = 1; i < 256; i++) {
        sum += hist[i];
        if (sum > nrNonEdgePixels) {
            thH = i;
            i = 256;
        }
    }
    thL = (float) thH * 0.4;

    for (int i = 0; i < src.rows; i++) {
        for (int j = 0; j < src.cols; j++) {
            if (modul.at<uchar>(i, j) <= thL) {
                modul.at<uchar>(i, j) = NON_EDGE;
            } else {
                if (modul.at<uchar>(i, j) >= thH) {
                    modul.at<uchar>(i, j) = STRONG_EDGE;
                } else {
                    modul.at<uchar>(i, j) = WEAK_EDGE;
                }
            }
        }
    }

    Mat_<uchar> lab(modul.rows, modul.cols, (uchar) 0);
    int label = 0;
    for (int i = 1; i < modul.rows - 1; i++) {
        for (int j = 1; j < modul.cols - 1; j++) {
            if (modul.at<uchar>(i, j) == STRONG_EDGE && lab.at<uchar>(i, j) == 0) {
                label += 1;

                queue<std::tuple<int, int>> Q;
                lab.at<uchar>(i, j) = label;
                Q.push({i, j});
                while (!Q.empty()) {
                    std::tuple<int, int> q;
                    q = Q.front();
                    Q.pop();
                    for (int k = 0; k < 8; k++) {
                        int newi = ni8[k] + std::get<0>(q);
                        int newj = nj8[k] + std::get<1>(q);
                        if (isInside(modul, newi, newj) &&
                            modul.at<uchar>(newi, newj) == WEAK_EDGE &&
                            lab.at<uchar>(newi, newj) == 0) {
                            modul.at<uchar>(newi, newj) = STRONG_EDGE;
                            lab.at<uchar>(newi, newj) = label;
                            Q.push({newi, newj});
                        }
                    }
                }
            }
        }
    }
    for (int i = 0; i < modul.rows; i++) {
        for (int j = 0; j < modul.cols; j++) {
            if (modul.at<uchar>(i, j) == WEAK_EDGE) {
                modul.at<uchar>(i, j) = NON_EDGE;
            }
        }
    }

    for (int i = 0; i < modul.rows; i++) {
        for (int j = 0; j < modul.cols; j++) {
            src(i,j)[0] = modul.at<uchar>(i,j);
            src(i,j)[1] = modul.at<uchar>(i,j);
            src(i,j)[2] = modul.at<uchar>(i,j);
        }
    }
}

void histogram_equalization(Mat_<Vec4b> &img) {
    applyGrayscale(img);
    int h[256] = {0};
    float p[256] = {0};
    float cp[256] = {0};
    //histogram

    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            h[img(i, j)[0]]++;
        }
    }

    //pdf
    int M = img.rows * img.cols;
    for (int i = 0; i <= 255; i++) {
        p[i] = (float) h[i] / M;
    }

    //cpdf
    float sum = 0;
    for (int k = 0; k < 256; k++) {
        sum = 0;
        for (int j = 0; j <= k; j++) {
            sum += p[j];
        }
        cp[k] = sum;
    }

    //transformation
    Mat_<uchar> dst(img.rows, img.cols);
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            dst(i, j) = (float) 255 * cp[img(i, j)[0]];
        }
    }

    int hh[256] = {0};
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i,j)[0] = dst(i,j);
            img(i,j)[1] = dst(i,j);
            img(i,j)[2] = dst(i,j);
        }
    }
}

void applyFade(Mat_<Vec4b> &img, int min, int max){
    int GIminR = 255, GImaxR = 0;
    int GIminG = 255, GImaxG = 0;
    int GIminB = 255, GImaxB = 0;
    int GOmin = min,GOmax = max;
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            if (GIminR > img(i, j)[0]) {
                GIminR = img(i, j)[0];
            }
            if (GImaxR < img(i, j)[0]) {
                GImaxR = img(i, j)[0];
            }

            if (GIminG > img(i, j)[1]) {
                GIminG = img(i, j)[1];
            }
            if (GImaxG < img(i, j)[1]) {
                GImaxG = img(i, j)[1];
            }

            if (GIminB > img(i, j)[2]) {
                GIminB = img(i, j)[2];
            }
            if (GImaxB < img(i, j)[2]) {
                GImaxB = img(i, j)[2];
            }
        }
    }
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i, j)[0] = GOmin + (img(i, j)[0] - GIminR)* ((GOmax - GOmin) / (GImaxR - GIminR));
            img(i, j)[1] = GOmin + (img(i, j)[1] - GIminG)* ((GOmax - GOmin) / (GImaxG - GIminG));
            img(i, j)[2] = GOmin + (img(i, j)[2] - GIminB)* ((GOmax - GOmin) / (GImaxB - GIminB));
        }
    }
}

void applyNoise(Mat_<Vec4b> &img){
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            img(i, j)[0] |= rand() % 255 +1;
            img(i, j)[1] |= rand() % 255 +1;
            img(i, j)[2] |= rand() % 255 +1;
        }
    }

}

void applyResize(Mat_<cv::Vec4b> &img, Mat_<cv::Vec4b> &dst, int newHeight, int newWidth){
    float ratioHeight = img.rows / newHeight;
    float ratioWidth = img.cols / newWidth;
    for (int i = 0; i < newHeight; i++) {
        for (int j = 0; j < newWidth; j++) {
           dst(i, j) = img((int)(i * ratioHeight), (int)( j * ratioWidth));
        }
    }

}

void applySplit(Mat_<cv::Vec4b> &img){
    int Th = findThreshold(img);
    for (int i = 0; i < img.rows; i++) {
        for (int j = 0; j < img.cols; j++) {
            if(img(i,j)[2] < Th){
                img(i,j)[2] = min (255, img(i,j)[2] + 50);
            }
            else{
                img(i,j)[0] = min (255, img(i,j)[0] + 50);
            }
        }
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_equalizeFilter(JNIEnv *env, jclass clazz, jlong input) {
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    histogram_equalization(img);

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_M5Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement M5Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyBrightness(img, 50);
    applyRGB(img, 50, 15, 0);
    applySaturation(img, -10);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_HB1Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement HB1Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyBrightness(img, 30);
    applyRGB(img, 30, 40, 60);
    applySaturation(img, 10);

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_X1Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement X1Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyGrayscale(img);
    applyContrast(img, 0.1);
    applyFade(img, 50,200);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_resize(JNIEnv *env, jclass clazz, jlong in, jlong out,
                                          jint width, jint height) {
    // TODO: implement resize()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    cv::Mat_<cv::Vec4b>& dst = *(cv::Mat_<cv::Vec4b>*) out;
    applyResize(img, dst, height, width);

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_C1Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement C1Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyBrightness(img, 80);
    applySaturation(img, 20);

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_KU8Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement KU8Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applyBrightness(img, 50);
    applySaturation(img, -20);
    applyRGB(img, 10, 15, 5);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_P5Filter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement P5Filter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    applySaturation(img, -50);
    applyRGB(img, 40, 0, 40);
    applyFade(img, 0, 255);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_cannyFilter(JNIEnv *env, jclass clazz, jlong input) {
    // TODO: implement cannyFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) input;
    cannyEdge(img);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_vsco_editing_Processing_splitFilter(JNIEnv *env, jclass clazz, jlong in) {
    // TODO: implement splitFilter()
    cv::Mat_<cv::Vec4b>& img = *(cv::Mat_<cv::Vec4b>*) in;
    applySplit(img);
}
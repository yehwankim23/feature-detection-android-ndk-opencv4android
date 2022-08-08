#include <jni.h>

#include <opencv2/opencv.hpp>

static const int NUMBER_OF_FEATURES = 1000;
static cv::Ptr<cv::Feature2D> DETECTOR = cv::SIFT::create(NUMBER_OF_FEATURES);

extern "C"
JNIEXPORT void JNICALL
Java_com_example_feature_1detection_1android_1ndk_1opencv4android_MainActivity_drawKeypoints(
        JNIEnv *env, jobject thiz, jlong mat) {
    cv::Mat &frame_rgba = *(cv::Mat *) mat;

    cv::Mat frame_gray;
    cv::cvtColor(frame_rgba, frame_gray, cv::COLOR_RGBA2GRAY);

    std::vector<cv::KeyPoint> keypoints;
    DETECTOR->detect(frame_gray, keypoints);

    cv::drawKeypoints(frame_rgba, keypoints, frame_rgba);
}

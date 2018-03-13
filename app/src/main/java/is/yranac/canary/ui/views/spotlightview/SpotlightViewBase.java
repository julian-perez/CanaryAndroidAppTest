package is.yranac.canary.ui.views.spotlightview;

/**
 * Shimmer
 * User: romainpiel
 * Date: 10/03/2014
 * Time: 17:33
 */
public interface SpotlightViewBase {
    float getGradientX();
    void setGradientX(float gradientX);
    boolean isShimmering();
    void setShimmering(boolean isShimmering);
    boolean isSetUp();
    void setAnimationSetupCallback(SpotlightViewHelper.AnimationSetupCallback callback);
    int getPrimaryColor();
    void setPrimaryColor(int primaryColor);
    int getReflectionColor();
    void setReflectionColor(int reflectionColor);
}

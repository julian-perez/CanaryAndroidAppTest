package is.yranac.canary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;

import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.services.database.AvatarDatabaseService;

public class ImageUtils {

    private static final String LOG_TAG = "ImageUtils";

    public static void loadAvatar(final String customerId,
                                  final ImageView avatarImageView) {
        avatarImageView.setVisibility(View.GONE);

        Avatar avatar = AvatarDatabaseService.getAvatarFromCustomerId(Integer.parseInt(customerId));
        if (avatar != null)
            loadAvatar(avatarImageView, avatar.thumbnailUrl());
    }

    public static void loadAvatar(
            final ImageView avatarImageView, String imageUrl) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrl, avatarImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                avatarImageView.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void downloadUrl(String strUrl, ImageLoadingListener loadingListener) {


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(strUrl, loadingListener);

    }


    public static Bitmap getBitmap(Uri selectedImageUri, Context context, boolean rotate) {
        Bitmap avatarBitmap;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(selectedImageUri);
            if (inputStream == null)
                return null;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;
            avatarBitmap = BitmapFactory.decodeStream(inputStream, null, opts);
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        if (rotate) {
            return rotateBitmap(avatarBitmap, selectedImageUri);
        }

        return avatarBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, Uri selectedImageUri) {
        try {
            ExifInterface exif = new ExifInterface(selectedImageUri.getPath());

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(bitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(bitmap, 270);
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }

        } catch (Exception ignore) {
        }

        return bitmap;

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {

        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}


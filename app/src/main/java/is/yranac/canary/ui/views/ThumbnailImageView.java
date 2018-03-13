package is.yranac.canary.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.ref.WeakReference;

import is.yranac.canary.model.thumbnail.Thumbnail;
import is.yranac.canary.services.api.EntryAPIService;
import is.yranac.canary.services.database.ThumbnailDatabaseService;
import is.yranac.canary.util.ImageUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by michaelschroeder on 3/1/17.
 */

public class ThumbnailImageView extends AppCompatImageView {
    public ThumbnailImageView(Context context) {
        super(context);
    }

    public ThumbnailImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbnailImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadThumbnail(final Thumbnail thumbnail) {
        loadThumbnail(thumbnail.id, thumbnail.imageUrl());
    }

    public void loadThumbnail(final long thumbnail, String imageUrl) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        final WeakReference<ThumbnailImageView> weakReference = new WeakReference<>(this);
        imageLoader.displayImage(
                imageUrl, this, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        if (failReason.getType() == FailReason.FailType.IO_ERROR) {
                            weakReference.get().reloadThumbnail(thumbnail);
                        }
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
    }

    private void reloadThumbnail(long thumbnail) {
        final WeakReference<ThumbnailImageView> weakReference = new WeakReference<>(this);
        EntryAPIService.getNewThumbnail(thumbnail, new Callback<Thumbnail>() {
            @Override
            public void success(Thumbnail thumbnail, Response response) {
                ThumbnailDatabaseService.updateThumbnailUrl(thumbnail);
                ImageLoader.getInstance().displayImage(thumbnail.imageUrl(), weakReference.get());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

}

package is.yranac.canary.services.api;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.model.avatar.Avatar;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class AvatarAPIServices {

    public static void uploadUserAvatarBitmap(final Bitmap avatarBitmap, final String customerUri, final Callback<Avatar> callback) {
        final String imagePath = CanaryApplication.getContext().getCacheDir().getAbsolutePath() + "/avatar.png";
        try {
            FileOutputStream fileOutStream = new FileOutputStream(imagePath);
            avatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutStream);
            fileOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadUserAvatar(imagePath, customerUri, callback);
    }

    public static void uploadUserAvatar(String imagePath, String customerUri, Callback<Avatar> callback) {
        TypedFile typedFile = new TypedFile("image/png", new File(imagePath));
        TypedString customerUriTypedString = new TypedString(customerUri);
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        AvatarService avatarService = restAdapter.create(AvatarService.class);
        avatarService.createAvatar(typedFile, customerUriTypedString, callback);
    }

    public interface AvatarService {
        @Multipart
        @POST(Constants.AVATAR_URI)
        void createAvatar(
                @Part("image") TypedFile typedFile,
                @Part("customer") TypedString customerUriTypedString,
                Callback<Avatar> callback
        );
    }

}

package is.yranac.canary.services.api;

import is.yranac.canary.Constants;
import is.yranac.canary.model.comment.CommentCreate;
import is.yranac.canary.retrofit.RetroFitAdapterFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Schroeder on 8/11/14.
 */
public class CommentAPIServices {

    public static void createComment(final String comment, final String entryUri, Callback<Void> callback){
        RestAdapter restAdapter = RetroFitAdapterFactory.getNewDefaultAdapter(true);
        CommentService commentService = restAdapter.create(CommentService.class);
        CommentCreate commentCreate = new CommentCreate(comment, entryUri);
        commentService.postComment(commentCreate, callback);

    }

    public interface CommentService {

        @POST(Constants.COMMENT_URI)
        void postComment(
                @Body CommentCreate commentCreate,
                Callback<Void> callback);
    }
}

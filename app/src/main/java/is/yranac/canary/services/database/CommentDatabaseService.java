package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.Constants;
import is.yranac.canary.contentproviders.CanaryCommentContentProvider;
import is.yranac.canary.model.comment.Comment;
import is.yranac.canary.util.Utils;

/**
 * The type Comment database service.
 */
public class CommentDatabaseService {
    /**
     * The constant TAG.
     */
    private static final String TAG = "CommentDatabaseService";

    /**
     * The constant contentResolver.
     */
    private static ContentResolver contentResolver = CanaryApplication.getContext()
            .getContentResolver();

    /**
     * Insert comments.
     *
     * @param comments the comments
     * @return the int
     */
    public static void insertComments(List<Comment> comments) {
        if (comments == null)
            return;
        for (Comment comment : comments) {
            updateOrInsertComment(comment);
        }
    }

    public static List<Comment> getCommentsForEntry(long entryid) {
        List<Comment> comments = new ArrayList<>();

        String where = CanaryCommentContentProvider.COLUMN_ENTRY_ID + " LIKE ?";
        String[] whereArgs = {String.valueOf(entryid)};

        Cursor cursor = contentResolver.query(CanaryCommentContentProvider.CONTENT_URI, null, where, whereArgs, null);

        while (cursor.moveToNext()) {
            comments.add(getCommentFromCursor(cursor));
        }
        cursor.close();

        return comments;
    }

    /**
     * Update or insert comment into comment table.
     *
     * @param comment the Comment to insert
     */
    public static void updateOrInsertComment(Comment comment) {
        if (comment.id == 0)
            return;
        ContentValues values = createContentValuesFromComment(comment);
        contentResolver.insert(CanaryCommentContentProvider.CONTENT_URI, values);
}

    /**
     * Create content values from Comment object.
     *
     * @param comment the comment
     * @return the content values ready for database insertion
     */
    public static ContentValues createContentValuesFromComment(Comment comment) {
        ContentValues values = new ContentValues();
        values.put(CanaryCommentContentProvider.COLUMN_ID, comment.id);
        values.put(CanaryCommentContentProvider.COLUMN_BODY, comment.body == null ? "" : comment.body);
        values.put(CanaryCommentContentProvider.COLUMN_CREATED, comment.created == null ? 0 : comment.created.getTime());
        values.put(CanaryCommentContentProvider.COLUMN_CUSTOMER_URI, comment.customerUri == null ? "" : comment.customerUri);
        values.put(CanaryCommentContentProvider.COLUMN_ENTRY_ID, Utils.getLongFromResourceUri(comment.entryUri));
        values.put(CanaryCommentContentProvider.COLUMN_MODIFIED, comment.modified == null ? 0 : comment.modified.getTime());
        return values;
    }

    /**
     * Creates an Entry object from supplied cursor.
     *
     * @param cursor the cursor
     * @return the comment from cursor
     */
    public static Comment getCommentFromCursor(Cursor cursor) {
        Comment comment = new Comment();
        comment.body = cursor.getString(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_BODY));
        comment.created = new Date(cursor.getLong(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_CREATED)));
        comment.modified = new Date(cursor.getLong(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_MODIFIED)));
        comment.customerUri = cursor.getString(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_CUSTOMER_URI));
        comment.entryUri = cursor.getString(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_ENTRY_ID));
        comment.id = cursor.getInt(cursor.getColumnIndex(CanaryCommentContentProvider.COLUMN_ID));
        comment.resourceUri = Utils.buildResourceUri(Constants.COMMENT_URI, comment.id);
        return comment;
    }

}

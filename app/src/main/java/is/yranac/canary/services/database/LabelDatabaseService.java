package is.yranac.canary.services.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import is.yranac.canary.CanaryApplication;
import is.yranac.canary.contentproviders.CanaryLabelContentProvider;
import is.yranac.canary.model.label.Label;

/**
 * The type Label database service.
 */
public class LabelDatabaseService {
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
     * Set the labels for an entry.  Any current labels are deleted before the new ones are inserted into the database
     *
     * @param entryId the entry id
     * @param labels  the labels for the entry
     * @return the number of labels inserted
     */
    public static void setLabelsForEntryId(long entryId, List<Label> labels) {


        deleteLabelsForEntryId(entryId);

        if (labels == null)
            return;

        for (Label label : labels) {
            if (label == null)
                continue;
            label.entryId = entryId;
            contentResolver.insert(CanaryLabelContentProvider.CONTENT_URI, createContentValuesFromLabel(label));

            UserTagsDatabaseService.insertTag(label.name, false);
        }
    }

    /**
     * Sets labels for an entry using a supplied list of Strings.  Any current labels are deleted before the new ones are inserted into the database
     *
     * @param entryId         the entry id
     * @param labelStringList the label string list
     */
    public static void setLabelsForEntryFromStringList(long entryId, List<String> labelStringList) {

        List<Label> labels = new ArrayList<>();
        for (String labelString : labelStringList) {
            Label label = new Label();
            label.entryId = entryId;
            label.name = labelString.toLowerCase(Locale.getDefault());
            labels.add(label);
        }
        setLabelsForEntryId(entryId, labels);
    }

    /**
     * Gets label display text for entry id.
     *
     * @param entryId the entry id
     * @return the label display text for entry id
     */
    public static String getLabelDisplayTextForEntryId(long entryId) {
        List<Label> labels = getLabelsForEntryId(entryId);

        return createLabelString(labels);
    }

    public static String createLabelString(List<Label> labels) {
        if (labels.isEmpty()) {
            return null;
        }
        Label firstLabel = labels.get(0);

        StringBuilder sb = new StringBuilder(firstLabel.name);

        for (Label label : labels) {

            if (label.name.equalsIgnoreCase(firstLabel.name)){
                continue;
            }

            sb.append(", ");
            String name = label.name;

            sb.append(name);
        }

        return sb.toString();
    }

    /**
     * Gets labels from entry id.
     *
     * @param entryId the entry id
     * @return the labels from entry id
     */
    public static List<Label> getLabelsForEntryId(long entryId) {
        List<Label> labels = new ArrayList<>();

        String where = CanaryLabelContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = new String[]{String.valueOf(entryId)};

        Cursor cursor = contentResolver.query(CanaryLabelContentProvider.CONTENT_URI, null, where, whereArgs, null);

        if (cursor == null)
            return labels;
        while (cursor.moveToNext()) {
            labels.add(getLabelFromCursor(cursor));
        }
        cursor.close();

        return labels;
    }

    public static List<String> getLabelsForEntryIdAsStringList(long entryId) {
        List<String> entryLabelStringList = new ArrayList<>();

        String where = CanaryLabelContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = new String[]{String.valueOf(entryId)};

        Cursor cursor = contentResolver.query(CanaryLabelContentProvider.CONTENT_URI, null, where, whereArgs, null);


        if (cursor == null)
            return entryLabelStringList;

        while (cursor.moveToNext()) {
            entryLabelStringList.add(getLabelFromCursor(cursor).name);
        }
        cursor.close();

        return entryLabelStringList;
    }

    /**
     * Delete labels for entry.
     *
     * @param entryId the entry id
     */
    public static void deleteLabelsForEntryId(long entryId) {
        String where = CanaryLabelContentProvider.COLUMN_ENTRY_ID + " == ?";
        String[] whereArgs = {String.valueOf(entryId)};

        contentResolver.delete(CanaryLabelContentProvider.CONTENT_URI, where, whereArgs);

    }

    /**
     * Create content values from label.
     *
     * @param label the label
     * @return the content values
     */
    public static ContentValues createContentValuesFromLabel(Label label) {
        ContentValues values = new ContentValues();

        values.put(CanaryLabelContentProvider.COLUMN_ID, label.id);
        values.put(CanaryLabelContentProvider.COLUMN_ENTRY_ID, label.entryId);
        values.put(CanaryLabelContentProvider.COLUMN_NAME, label.name);

        return values;
    }

    /**
     * Gets label from cursor.
     *
     * @param cursor the cursor
     * @return the label from cursor
     */
    public static Label getLabelFromCursor(Cursor cursor) {
        Label label = new Label();

        label.name = cursor.getString(cursor.getColumnIndex(CanaryLabelContentProvider.COLUMN_NAME));
        label.id = cursor.getInt(cursor.getColumnIndex(CanaryLabelContentProvider.COLUMN_ID));
        label.entryId = cursor.getLong(cursor.getColumnIndex(CanaryLabelContentProvider.COLUMN_ENTRY_ID));

        return label;
    }


    public static void deleteLabel(Label label){

    }
    public static  List<Label>  getLabels() {
        Set<Label> labels = new HashSet<>();

        Cursor cursor = contentResolver.query(CanaryLabelContentProvider.CONTENT_URI, null, null, null, null);

        if (cursor == null)
            return new ArrayList<>();

        while (cursor.moveToNext()) {
            labels.add(getLabelFromCursor(cursor));
        }
        cursor.close();

        return new ArrayList<>(labels);
    }
}

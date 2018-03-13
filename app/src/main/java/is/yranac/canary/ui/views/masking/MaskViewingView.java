package is.yranac.canary.ui.views.masking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import is.yranac.canary.R;
import is.yranac.canary.messages.DeviceMaskCount;
import is.yranac.canary.messages.RefreshOptionButtons;
import is.yranac.canary.messages.masking.ShowMessageRequest;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.masking.CVMask;
import is.yranac.canary.model.masking.CVVertex;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.util.Log;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.UserUtils;

import static is.yranac.canary.ui.views.masking.MaskViewingView.MessageType.MASKING_MAX_REACHED;

/**
 * Created by sergeymorozov on 12/6/16.
 */

public class MaskViewingView extends View {

    protected static final int maxMasksPerDevice = 4;

    //if this ever changes, this needs to be the length of list of drawVertices of the selected mask
    private static final int maxPoints = 8;

    private static int maskingBorderColor;
    private static int maskingFillColor;
    private static int maskBorderWidth;
    private static int dragPointRadiusLarge;
    private static int dragPointRadiusSmall;

    protected Device deviceToMask;
    protected DeviceMasks existingDeviceMasks;

    protected int viewWidth;
    protected int viewHeight;

    protected int selectedMaskIndex;

    protected ArrayList<DrawMask> editableRegions;

    protected Path maskingPath;
    protected Paint
            regularPointPaint,
            selectedPointPaint,
            maskingBorderPaint,
            maskingFillPaint;


    public enum MessageType {
        MASKING_INITIAL,
        MASKING_MAX_REACHED
    }


    public MaskViewingView(Context context) {
        super(context);
    }

    public MaskViewingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpViewing();
    }

    public void setDeviceMasks(DeviceMasks deviceMasks, Device device) {
        this.existingDeviceMasks = deviceMasks;
        this.deviceToMask = device;

        updateDeviceMasks(existingDeviceMasks);

        invalidate();
    }

    public void setUpViewing() {
        maskingPath = new Path();


        regularPointPaint = new Paint();
        regularPointPaint.setStyle(Paint.Style.FILL);
        regularPointPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        selectedPointPaint = new Paint();
        selectedPointPaint.setStyle(Paint.Style.FILL);
        selectedPointPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        maskingBorderPaint = new Paint();
        maskingBorderPaint.setStrokeWidth(getMaskBorderWidth());
        maskingBorderPaint.setAntiAlias(true);
        maskingBorderPaint.setStyle(Paint.Style.STROKE);
        maskingBorderPaint.setColor(getMaskingBorderColor());

        maskingFillPaint = new Paint();
        maskingFillPaint.setAntiAlias(true);
        maskingFillPaint.setStyle(Paint.Style.FILL);
        maskingFillPaint.setColor(getMaskingFillColor());

        selectedMaskIndex = -1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.viewHeight = h;
        this.viewWidth = w;

        if (this.existingDeviceMasks == null || this.existingDeviceMasks.deviceMasks == null)
            return;

        updateDeviceMasks(this.existingDeviceMasks);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < getEditableDeviceMasks().size(); i++) {
            DrawMask mask = getEditableDeviceMasks().get(i);
            prepareToDrawMask(mask, canvas, i == getSelectedMaskIndex());
        }

        canvas.drawPath(maskingPath, maskingFillPaint);
        canvas.drawPath(maskingPath, maskingBorderPaint);
    }

    protected void updateDisplay(MaskDrawingView.MessageType messageType) {
        String message = null;
        switch (messageType) {
            case MASKING_INITIAL:
                message = getResources().getString(R.string.masking_default_message);
                break;
            case MASKING_MAX_REACHED:
                message = getResources().getString(R.string.masking_max_masks_reached);
                break;
            default:
                return;
        }

        TinyMessageBus.post(new ShowMessageRequest(message));
    }

    protected void updateDeviceMasks(DeviceMasks newMasks) {

        if (newMasks == null)
            this.existingDeviceMasks = new DeviceMasks();
        else
            this.existingDeviceMasks = newMasks;

        this.editableRegions = new ArrayList<>();

        for (CVMask mask : this.existingDeviceMasks.deviceMasks) {
            getEditableDeviceMasks().add(new DrawMask(mask));
        }

        if (getEditableDeviceMasks().size() >= maxMasksPerDevice) {
            updateDisplay(MASKING_MAX_REACHED);
            TinyMessageBus.post(new RefreshOptionButtons(null, null, false));
        }

        TinyMessageBus.post(new DeviceMaskCount(getEditableDeviceMasks().size()));

        resetPaths();
        invalidate();
    }

    protected void resetPaths() {
        if (maskingPath != null)
            maskingPath.reset();
    }

    private void prepareToDrawMask(DrawMask mask, Canvas canvas, boolean isSelected) {

        if (mask == null || canvas == null)
            return;

        //drawing shaded region
        for (int i = 0; i < mask.drawVertices.size(); i++) {
            DrawVertex point = mask.drawVertices.get(i);

            if (i == 0)
                maskingPath.moveTo(point.x, point.y);
            else
                maskingPath.lineTo(point.x, point.y);

            if (i == mask.drawVertices.size() - 1)
                maskingPath.close();
        }

        //drawing selected drawVertices
        if (isSelected) {
            for (int i = 0; i < mask.drawVertices.size(); i++) {
                DrawVertex point = mask.drawVertices.get(i);

                if (!point.isOdd())
                    canvas.drawCircle(point.x, point.y, getDragPointRadiusSmall(), regularPointPaint);
                else {
                    if (point.lockedTo.isEmpty())
                        canvas.drawCircle(point.x, point.y, getDragPointRadiusLarge(), regularPointPaint);
                    else
                        canvas.drawCircle(point.x, point.y, getDragPointRadiusLarge(), selectedPointPaint);
                }
            }
        }

    }

    private int getSelectedMaskIndex() {
        return selectedMaskIndex;
    }

    private int getMaskBorderWidth() {
        if (maskBorderWidth == 0)
            maskBorderWidth =
                    (int) getContext().getResources().getDimension(R.dimen.masking_border_width);
        return maskBorderWidth;
    }

    private int getMaskingBorderColor() {
        if (maskingBorderColor == 0) {
            maskingBorderColor = ContextCompat.getColor(getContext(), R.color.white);
        }
        return maskingBorderColor;
    }

    private int getMaskingFillColor() {
        if (maskingFillColor == 0) {
            maskingFillColor = ContextCompat.getColor(getContext(), R.color.white_thirty);
        }
        return maskingFillColor;
    }


    protected int getDragPointRadiusLarge() {
        if (dragPointRadiusLarge == 0)
            dragPointRadiusLarge =
                    (int) getContext().getResources().getDimension(R.dimen.masking_point_radius_large);
        return dragPointRadiusLarge;
    }

    protected int getDragPointRadiusSmall() {
        if (dragPointRadiusSmall == 0)
            dragPointRadiusSmall =
                    (int) getContext().getResources().getDimension(R.dimen.masking_point_radius_small);
        return dragPointRadiusSmall;
    }

    protected List<DrawMask> getEditableDeviceMasks() {
        if (this.editableRegions == null) {
            this.editableRegions = new ArrayList<>();
        }
        return this.editableRegions;
    }

    private float convertPercentToPosition(double percent, boolean isHeight) {
        if (isHeight)
            return (float) (this.viewHeight * percent);
        else
            return (float) (this.viewWidth * percent);
    }

    private float convertPositionToPercent(float position, boolean isHeight) {
        float percent;
        if (isHeight)
            percent = (position / this.viewHeight);
        else
            percent = (position / this.viewWidth);

        percent = UserUtils.formatFloatDecimalPlaces(percent, 4);
        return percent;
    }

    public DeviceMasks getMasksToSave(String deviceURL) {
        DeviceMasks masks = new DeviceMasks();
        int currentSize = 0;
        if (existingDeviceMasks != null && existingDeviceMasks.deviceMasks != null)
            currentSize = existingDeviceMasks.deviceMasks.size();

        if (getEditableDeviceMasks().size() == 0) {
            if (existingDeviceMasks.deviceMasks.size() != 0) {
                // we deleted all masks. Need to submit a list with one mask w/o any verteces to
                // delete all masks on the cloud.
                CVMask mask = existingDeviceMasks.deviceMasks.get(0);
                mask.vertices = new ArrayList<>();
                masks.deviceMasks.add(mask);
            }
            this.existingDeviceMasks = new DeviceMasks();
        } else {
            for (int i = 0; i < getEditableDeviceMasks().size(); i++) {
                CVMask existingMask = null;
                if (i < currentSize) {
                    existingMask = existingDeviceMasks.deviceMasks.get(i);
                }

                DrawMask region = getEditableDeviceMasks().get(i);

                CVMask newMask = region.convertToMask(existingMask, deviceURL, i);
                masks.deviceMasks.add(newMask);
            }

            this.existingDeviceMasks = masks;
        }

        return masks;
    }

    protected class DrawMask {
        protected boolean isNew;
        ArrayList<DrawVertex> drawVertices;
        ArrayList<DrawVertex> originalDrawVertices;
        Boolean isLocked;
        boolean overlapsOptions;

        DrawMask(CVMask mask) {
            this.drawVertices = new ArrayList<>();
            this.originalDrawVertices = new ArrayList<>();

            if (mask == null || mask.vertices == null)
                return;

            for (int i = 0; i < mask.vertices.size(); i++) {
                CVVertex v = mask.vertices.get(i);
                drawVertices.add(new DrawVertex(v, i));
                originalDrawVertices.add(new DrawVertex(v, i));
            }
        }

        public boolean hasChanged() {
            if (isNew) {
                return true;
            }

            if (drawVertices == null || originalDrawVertices == null)
                return false;

            if (drawVertices.size() != originalDrawVertices.size())
                return false;

            for (int i = 0; i < drawVertices.size(); i++) {
                DrawVertex currentVertex = drawVertices.get(i);
                DrawVertex originalVertex = originalDrawVertices.get(i);

                if (!currentVertex.equals(originalVertex)) {
                    Log.e("hasChanged", " returning true becasue vertecies arent equal");
                    return true;
                }
            }

            return false;
        }

        public boolean containsPoint(float x, float y) {
            if (this.drawVertices == null || this.drawVertices.isEmpty())
                return false;

            float minX = drawVertices.get(0).x;
            float maxX = drawVertices.get(0).x;
            float minY = drawVertices.get(0).y;
            float maxY = drawVertices.get(0).y;


            for (int i = 1; i < drawVertices.size(); i++) {
                DrawVertex q = drawVertices.get(i);
                minX = Math.min(q.x, minX);
                maxX = Math.max(q.x, maxX);
                minY = Math.min(q.y, minY);
                maxY = Math.max(q.y, maxY);
            }


            if (x < minX || x > maxX || y < minY || y > maxY) {
                return false;
            }

            // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
            boolean inside = false;
            for (int i = 0, j = drawVertices.size() - 1; i < drawVertices.size(); j = i++) {

                if ((drawVertices.get(i).y > y) != (drawVertices.get(j).y > y) &&
                        x < (drawVertices.get(j).x - drawVertices.get(i).x) * (y - drawVertices.get(i).y)
                                / (drawVertices.get(j).y - drawVertices.get(i).y) + drawVertices.get(i).x) {

                    inside = !inside;
                }

            }

            return inside;
        }

        public CVMask convertToMask(CVMask originalMask, String deviceURL, int index) {
            CVMask newMask = new CVMask();

            //TODO: doesnt matter right now, we need to rethink convention for
            // naming masks when we get there
            newMask.name = "Mask" + index;
            if (originalMask != null) {
                newMask.deviceURL = originalMask.deviceURL;
                newMask.maskIn = originalMask.maskIn;
            } else {
                newMask.deviceURL = deviceURL;
            }

            for (DrawVertex point : this.drawVertices) {
                CVVertex v = new CVVertex();
                v.x = UserUtils.formatFloatDecimalPlaces(convertPositionToPercent(point.x, false), 4);
                v.y = UserUtils.formatFloatDecimalPlaces(convertPositionToPercent(point.y, true), 4);
                newMask.vertices.add(v);
            }

            return newMask;
        }
    }

    protected class DrawVertex {
        ArrayList<Integer> lockedTo;
        int indexInRegion;
        float x;
        float y;

        DrawVertex(float x, float y) {
            this.lockedTo = new ArrayList<>();
            this.indexInRegion = -1;
            this.x = x;
            this.y = y;
        }

        DrawVertex(CVVertex vertex, int indexInRegion) {
            this.lockedTo = new ArrayList<>();
            this.indexInRegion = indexInRegion;

            //convert percentages to actual values
            this.x = convertPercentToPosition(vertex.x, false);
            this.y = convertPercentToPosition(vertex.y, true);

            if (isOdd()) {
                this.lockedTo.add(normalizeIndex(indexInRegion - 1));
                this.lockedTo.add(normalizeIndex(indexInRegion + 1));
            }
        }

        void updateMidpoint(DrawVertex otherPoint, DrawVertex pointToUpdate) {

            float deltaX = (otherPoint.x + x) / 2;
            float deltaY = (otherPoint.y + y) / 2;

            pointToUpdate.x = deltaX;
            pointToUpdate.y = deltaY;
        }


        int normalizeIndex(int requestedIndex) {

            int modifiedIndex;
            int maxIndex = maxPoints - 1;

            if (requestedIndex > maxIndex)
                modifiedIndex = 0;
            else if (requestedIndex < 0)
                modifiedIndex = maxIndex;
            else
                modifiedIndex = requestedIndex;

            return modifiedIndex;
        }

        boolean isOdd() {
            return this.indexInRegion % 2 != 0;
        }

        void resetLocks(DrawMask region) {
            this.lockedTo = new ArrayList<>();
            region.isLocked = null;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            DrawVertex v = (DrawVertex) object;
            return v.x == this.x && v.y == this.y;
        }
    }

    public enum UserAction {
        USER_DRAGGING_VERTEX,
        USER_DRAGGING_MASK,
        NONE
    }
}

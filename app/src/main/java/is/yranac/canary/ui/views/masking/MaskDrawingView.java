package is.yranac.canary.ui.views.masking;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import is.yranac.canary.R;
import is.yranac.canary.messages.DeviceMaskCount;
import is.yranac.canary.messages.RefreshOptionButtons;
import is.yranac.canary.messages.SwitchUserModeRequest;
import is.yranac.canary.messages.masking.AddNewMask;
import is.yranac.canary.messages.masking.DeleteMaskRequest;
import is.yranac.canary.model.device.Device;
import is.yranac.canary.model.masking.CVMask;
import is.yranac.canary.model.masking.CVVertex;
import is.yranac.canary.model.masking.DeviceMasks;
import is.yranac.canary.util.TinyMessageBus;
import is.yranac.canary.util.ga.GoogleAnalyticsHelper;

import static is.yranac.canary.model.MaskingViewController.UserMode;
import static is.yranac.canary.model.MaskingViewController.UserMode.HIDE_OPTIONS;
import static is.yranac.canary.model.MaskingViewController.UserMode.SHOW_OPTIONS;
import static is.yranac.canary.model.MaskingViewController.UserMode.TOGGLE;
import static is.yranac.canary.ui.views.masking.MaskDrawingView.MoveRestriction.MOVE_ANYWHERE;
import static is.yranac.canary.ui.views.masking.MaskDrawingView.MoveRestriction.MOVE_HORIZONTALLY;
import static is.yranac.canary.ui.views.masking.MaskDrawingView.MoveRestriction.MOVE_VERTICALLY;
import static is.yranac.canary.ui.views.masking.MaskViewingView.MessageType.MASKING_INITIAL;
import static is.yranac.canary.ui.views.masking.MaskViewingView.MessageType.MASKING_MAX_REACHED;
import static is.yranac.canary.ui.views.masking.MaskViewingView.UserAction.NONE;
import static is.yranac.canary.ui.views.masking.MaskViewingView.UserAction.USER_DRAGGING_MASK;
import static is.yranac.canary.ui.views.masking.MaskViewingView.UserAction.USER_DRAGGING_VERTEX;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_ADD;
import static is.yranac.canary.util.ga.AnalyticsConstants.ACTION_MASKING_DELETE;
import static is.yranac.canary.util.ga.AnalyticsConstants.CATEGORY_MASKING;
import static is.yranac.canary.util.ga.AnalyticsConstants.PROPERTY_MASKING_MANAGE_MASK;

/**
 * Created by sergeymorozov on 9/28/16.
 */

public class MaskDrawingView extends MaskViewingView {

    private static final float thirtyEightPercentOfScreen = 0.38f;
    private static final float sixstyTwoPercentOfScreen = 0.62f;
    private static final float sixstyFourPercentOfScreen = 0.646f;
    private static final float fourtyFivePercentOfScreen = 0.4480f;
    private static final float quarterOfScreen = 0.25f;
    private static final float halfOfScreen = 0.50f;
    private static final long clickMinimum = 200;

    private static final int topLeftPointIndex = 0;
    protected static final int topMidPointIndex = 1;
    private static final int topRightPointIndex = 2;
    protected static final int midRightPointIndex = 3;
    private static final int lowRightPointIndex = 4;
    protected static final int lowMidPointIndex = 5;
    private static final int lowLeftPointIndex = 6;
    protected static final int midLeftPointIndex = 7;


    private static final float fatFingerAllowance = 5f;
    private DrawVertex touchAnchor;
    private DrawVertex draggedPoint;
    private UserAction userActionInProgress;
    private boolean showingOptions;
    private float borderTrigger;
    private long timeSinceLastClick;
    private boolean wasSelected;
    private int lastSelectedMaskIndex;

    protected enum MoveRestriction {
        MOVE_VERTICALLY,
        MOVE_HORIZONTALLY,
        MOVE_ANYWHERE
    }

    public MaskDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawing();
        lastSelectedMaskIndex = -1;
        updateDisplay(MASKING_INITIAL);
    }

    public MaskDrawingView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setUpDrawing();
        View parent = (View) getParent();

        int parentHeight = parent.getHeight();

        int margin = (h - parentHeight) / 2;

        borderTrigger = getResources().getDimension(R.dimen.masking_border_trigger) - margin;
    }

    @Subscribe
    public void deleteMask(DeleteMaskRequest event) {
        if (selectedMaskIndex < 0)
            return;

        GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING, ACTION_MASKING_DELETE, PROPERTY_MASKING_MANAGE_MASK, deviceToMask.uuid, deviceToMask.getLocationId(), 0);

        getEditableDeviceMasks().remove(selectedMaskIndex);
        setSelectedMaskIndex(getEditableDeviceMasks().size() - 1);

        if (selectedMaskIndex < 0) {
            userActionInProgress = NONE;
        }
        TinyMessageBus.post(new RefreshOptionButtons(getEnableDelete(), checkEnableSave(), true));

        if (getEditableDeviceMasks().size() <= maxMasksPerDevice)
            updateDisplay(MASKING_INITIAL);

        resetPaths();
        invalidate();
        TinyMessageBus.post(new DeviceMaskCount(getEditableDeviceMasks().size()));
    }

    @Subscribe
    public void addNewMask(AddNewMask event) {
        if (getEditableDeviceMasks().size() >= maxMasksPerDevice) {
            updateDisplay(MASKING_MAX_REACHED);
            TinyMessageBus.post(new RefreshOptionButtons(true, checkEnableSave(), false));
            return;
        }

        float offset = getEditableDeviceMasks().size();
        final float offsetPixels = 5;

        float offsetTotal = (offset * offsetPixels) / 100f;

        CVMask mask = new CVMask();
        //TODO: lets think this naming schema through
        mask.name = "Mask " + getEditableDeviceMasks().size() + 1;
        mask.deviceURL = this.deviceToMask.resourceUri;
        GoogleAnalyticsHelper.trackEvent(CATEGORY_MASKING, ACTION_MASKING_ADD, PROPERTY_MASKING_MANAGE_MASK, deviceToMask.uuid, deviceToMask.getLocationId(), 0);

        mask.vertices = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CVVertex v = new CVVertex();
            switch (i) {
                case 0:
                    v.setX(thirtyEightPercentOfScreen + offsetTotal);
                    v.setY(quarterOfScreen + offsetTotal);
                    break;
                case 1:
                    v.setX(halfOfScreen + offsetTotal);
                    v.setY(quarterOfScreen + offsetTotal);
                    break;
                case 2:
                    v.setX(sixstyTwoPercentOfScreen + offsetTotal);
                    v.setY(quarterOfScreen + offsetTotal);
                    break;
                case 3:
                    v.setX(sixstyTwoPercentOfScreen + offsetTotal);
                    v.setY(fourtyFivePercentOfScreen + offsetTotal);
                    break;
                case 4:
                    v.setX(sixstyTwoPercentOfScreen + offsetTotal);
                    v.setY(sixstyFourPercentOfScreen + offsetTotal);
                    break;
                case 5:
                    v.setX(halfOfScreen + offsetTotal);
                    v.setY(sixstyFourPercentOfScreen + offsetTotal);
                    break;
                case 6:
                    v.setX(thirtyEightPercentOfScreen + offsetTotal);
                    v.setY(sixstyFourPercentOfScreen + offsetTotal);
                    break;
                case 7:
                    v.setX(thirtyEightPercentOfScreen + offsetTotal);
                    v.setY(fourtyFivePercentOfScreen + offsetTotal);
                    break;
            }
            mask.vertices.add(v);
        }

        DrawMask region = new DrawMask(mask);
        region.isNew = true;
        getEditableDeviceMasks().add(region);
        setSelectedMaskIndex(getEditableDeviceMasks().indexOf(region));

        if (getEditableDeviceMasks().size() >= maxMasksPerDevice) {
            updateDisplay(MASKING_MAX_REACHED);
            TinyMessageBus.post(new RefreshOptionButtons(null, checkEnableSave(), false));
        }

        TinyMessageBus.post(new RefreshOptionButtons(true, checkEnableSave(), null));
        resetPaths();
        invalidate();

        TinyMessageBus.post(new DeviceMaskCount(getEditableDeviceMasks().size()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouched = event.getX();
        float yTouched = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                timeSinceLastClick = new Date().getTime();
                wasSelected = selectedMaskIndex > -1;

                //we touched down, and there's already a selected mask, lets check it's
                //vertecies first
                if (selectedMaskIndex > -1) {
                    DrawMask selectedMask = getEditableDeviceMasks().get(selectedMaskIndex);
                    draggedPoint = getSelectedVertex(selectedMask, xTouched, yTouched);

                    if (draggedPoint != null) {

                        userActionInProgress = USER_DRAGGING_VERTEX;
                        touchAnchor = new DrawVertex(xTouched, yTouched);
                        break;
                    }
                }

                //if we have no masks, no need to check anything else
                if (getEditableDeviceMasks().isEmpty())
                    break;

                int newSelectedMaskIndex = -1;
                for (int i = getEditableDeviceMasks().size() - 1; i >= 0; i--) {
                    DrawMask mask = getEditableDeviceMasks().get(i);
                    if (mask.containsPoint(xTouched, yTouched)) {
                        newSelectedMaskIndex = i;
                    }
                }
                setSelectedMaskIndex(newSelectedMaskIndex);

                if (selectedMaskIndex > -1) {
                    //we touched down, and it's inside the mask

                    touchAnchor = new DrawVertex(xTouched, yTouched);
                    userActionInProgress = USER_DRAGGING_MASK;
                } else {
                    //we touched down and it's outside any mask
                    userActionInProgress = NONE;
                    touchAnchor = null;
                }
                TinyMessageBus.post(new RefreshOptionButtons(getEnableDelete(), checkEnableSave(), null));

                resetPaths();
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                switch (userActionInProgress) {
                    case USER_DRAGGING_VERTEX:
                        if (draggedPoint == null)
                            break;

                        DrawMask selectedRegion = getSelectedRegion();
                        if (selectedRegion == null)
                            break;

                        //point can be locked
                        if (selectedRegion.isNew && draggedPoint.isOdd() && !draggedPoint.lockedTo.isEmpty()) {

                            ArrayList<DrawVertex> lockedPoints = new ArrayList<>();
                            lockedPoints.add(draggedPoint);

                            for (Integer index : draggedPoint.lockedTo) {
                                lockedPoints.add(selectedRegion.drawVertices.get(index));
                            }

                            MoveRestriction restriction;
                            //need to re calculate mid - point positions based on anchors
                            DrawVertex sideAStart, sideAAnchor, sideAMid, sideBStart, sideBAnchor, sideBMid;
                            switch (draggedPoint.indexInRegion) {
                                case 1:
                                    sideAStart = selectedRegion.drawVertices.get(topLeftPointIndex);
                                    sideAAnchor = selectedRegion.drawVertices.get(lowLeftPointIndex);
                                    sideAMid = selectedRegion.drawVertices.get(midLeftPointIndex);
                                    sideBStart = selectedRegion.drawVertices.get(topRightPointIndex);
                                    sideBAnchor = selectedRegion.drawVertices.get(lowRightPointIndex);
                                    sideBMid = selectedRegion.drawVertices.get(midRightPointIndex);
                                    restriction = MOVE_VERTICALLY;
                                    break;
                                case 3:
                                    sideAStart = selectedRegion.drawVertices.get(topRightPointIndex);
                                    sideAAnchor = selectedRegion.drawVertices.get(topLeftPointIndex);
                                    sideAMid = selectedRegion.drawVertices.get(topMidPointIndex);
                                    sideBStart = selectedRegion.drawVertices.get(lowRightPointIndex);
                                    sideBAnchor = selectedRegion.drawVertices.get(lowLeftPointIndex);
                                    sideBMid = selectedRegion.drawVertices.get(lowMidPointIndex);
                                    restriction = MOVE_HORIZONTALLY;
                                    break;
                                case 5:
                                    sideAStart = selectedRegion.drawVertices.get(lowRightPointIndex);
                                    sideAAnchor = selectedRegion.drawVertices.get(topRightPointIndex);
                                    sideAMid = selectedRegion.drawVertices.get(midRightPointIndex);
                                    sideBStart = selectedRegion.drawVertices.get(lowLeftPointIndex);
                                    sideBAnchor = selectedRegion.drawVertices.get(topLeftPointIndex);
                                    sideBMid = selectedRegion.drawVertices.get(midLeftPointIndex);
                                    restriction = MOVE_VERTICALLY;
                                    break;
                                case 7:
                                    sideAStart = selectedRegion.drawVertices.get(lowLeftPointIndex);
                                    sideAAnchor = selectedRegion.drawVertices.get(lowRightPointIndex);
                                    sideAMid = selectedRegion.drawVertices.get(lowMidPointIndex);
                                    sideBStart = selectedRegion.drawVertices.get(topLeftPointIndex);
                                    sideBAnchor = selectedRegion.drawVertices.get(topRightPointIndex);
                                    sideBMid = selectedRegion.drawVertices.get(topMidPointIndex);
                                    restriction = MOVE_HORIZONTALLY;
                                    break;
                                default:
                                    return false;
                            }

                            moveVertecies(restriction, lockedPoints, xTouched, yTouched);

                            if (!sideAMid.lockedTo.isEmpty())
                                sideAStart.updateMidpoint(sideAAnchor, sideAMid);
                            if (!sideBMid.lockedTo.isEmpty())
                                sideBStart.updateMidpoint(sideBAnchor, sideBMid);

                            resetPaths();
                            invalidate();

                            touchAnchor.x = xTouched;
                            touchAnchor.y = yTouched;

                        } else {

                            //reset locked drawVertices if needed and move the point
                            if (!draggedPoint.isOdd())
                                resetLockedPoints(draggedPoint);

                            ArrayList<DrawVertex> dragged = new ArrayList<>();
                            dragged.add(draggedPoint);

                            moveVertecies(MOVE_ANYWHERE, dragged, xTouched, yTouched);

                            resetPaths();
                            invalidate();


                            touchAnchor.x = xTouched;
                            touchAnchor.y = yTouched;
                        }

                        break;
                    case USER_DRAGGING_MASK:
                        if (selectedMaskIndex < 0 || touchAnchor == null)
                            break;

                        DrawMask selectedMask = getEditableDeviceMasks().get(selectedMaskIndex);
                        moveVertecies(MOVE_ANYWHERE, selectedMask.drawVertices, xTouched, yTouched);

                        resetPaths();
                        invalidate();

                        touchAnchor.x = xTouched;
                        touchAnchor.y = yTouched;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchAnchor = null;
                draggedPoint = null;

                if (isClick()) {
                    if (selectedMaskIndex > -1) {
                        if (lastSelectedMaskIndex == selectedMaskIndex) {
                            //toggle options
                            changeUserViewingMode(TOGGLE);
                        } else {
                            DrawMask mask = getSelectedRegion();
                            if (mask.overlapsOptions) {
                                //hide actions
                                changeUserViewingMode(HIDE_OPTIONS);
                            } else {
                                //show actions
                                changeUserViewingMode(SHOW_OPTIONS);
                            }
                        }
                    } else {
                        if (wasSelected) {
                            //show actions
                            changeUserViewingMode(SHOW_OPTIONS);
                        } else {
                            //toggle actions
                            changeUserViewingMode(TOGGLE);
                        }
                    }
                } else {
                    if (selectedMaskIndex > -1) {
                        DrawMask mask = getSelectedRegion();
                        if (mask.overlapsOptions) {
                            //hide actions
                            changeUserViewingMode(HIDE_OPTIONS);
                        } else {
                            //show actions
                            changeUserViewingMode(SHOW_OPTIONS);
                        }
                    }
                }

                userActionInProgress = NONE;
                TinyMessageBus.post(new RefreshOptionButtons(null, checkEnableSave(), null));
                break;
        }

        return true;
    }

    private boolean isClick() {
        return timeSinceLastClick > 0 && ((new Date().getTime() - timeSinceLastClick) < clickMinimum);
    }

    public boolean checkEnableSave() {
        boolean canSave = false;
        if (existingDeviceMasks == null || existingDeviceMasks.deviceMasks == null)
            return false;

        if (getEditableDeviceMasks().size() != existingDeviceMasks.deviceMasks.size()) {
            canSave = true;
        } else {
            for (DrawMask mask : getEditableDeviceMasks()) {
                if (mask.hasChanged()) {
                    canSave = true;
                    break;
                }
            }
        }

        return canSave;
    }

    private void moveVertecies(MoveRestriction restriction, List<DrawVertex> points, float xTouched, float yTouched) {
        float dx = restriction != MOVE_VERTICALLY ? xTouched - touchAnchor.x : 0f;
        float dy = restriction != MOVE_HORIZONTALLY ? yTouched - touchAnchor.y : 0f;

        boolean willOverlapOptions = false;

        //checking if the new position is within bounds
        for (int i = 0; i < points.size(); i++) {
            DrawVertex v = points.get(i);
            float newX = v.x + dx, newY = v.y + dy;

            //view boundry check
            if (newX <= 0f || newX >= (float) this.viewWidth || newY <= 0f || newY >= (float) this.viewHeight) {
                if (newX <= 0f || newX >= (float) this.viewWidth)
                    dx = 0f;
                if (newY <= 0f || newY >= (float) this.viewHeight)
                    dy = 0f;
                getSelectedRegion().overlapsOptions = isOverlayedByOptions(v);
            }

            //checking if we need to hide/show options
            if (!willOverlapOptions) {
                willOverlapOptions = isOverlayedByOptions(v);
            }
        }

        //Actually moving polygon
        for (DrawVertex v : points) {
            float newX = v.x + dx, newY = v.y + dy;

            v.x = newX;
            v.y = newY;
        }

        if (userActionInProgress == USER_DRAGGING_VERTEX) {
            //handling vertex drag differently - other verticies might be out of bounds
            willOverlapOptions = toggleOptionsForVertex();
        }

        if (willOverlapOptions) {
            //mask moved into the options zone for the first time
            changeUserViewingMode(HIDE_OPTIONS);
            if (!getSelectedRegion().overlapsOptions)
                getSelectedRegion().overlapsOptions = true;
        } else if (!willOverlapOptions && getSelectedRegion().overlapsOptions) {
            //mask moved out of the options zone for the first time
            changeUserViewingMode(SHOW_OPTIONS);
            getSelectedRegion().overlapsOptions = false;
        }
    }

    @Override
    public void setDeviceMasks(DeviceMasks deviceMasks, Device device) {
        super.setDeviceMasks(deviceMasks, device);
        
        TinyMessageBus.post(new RefreshOptionButtons(getEnableDelete(), checkEnableSave(), getEditableDeviceMasks().size() < maxMasksPerDevice));
    }

    private boolean toggleOptionsForVertex() {
        DrawMask selectedRegion = getSelectedRegion();
        if (selectedRegion == null)
            return false;

        boolean needToHideOptions = false;
        for (DrawVertex v : selectedRegion.drawVertices) {
            needToHideOptions = isOverlayedByOptions(v);
            if (needToHideOptions) {
                break;
            }
        }
        return needToHideOptions;
    }

    private boolean isOverlayedByOptions(DrawVertex vertex) {
        return (vertex.y + getBorderTrigger() >= this.viewHeight) || vertex.y <= getBorderTrigger();
    }

    private void setUpDrawing() {
        touchAnchor = null;
        setSelectedMaskIndex(-1);
        userActionInProgress = NONE;
        showingOptions = true;

        if (existingDeviceMasks != null)
            updateDeviceMasks(existingDeviceMasks);

        //if we dont have any masks, add a mask right away
        //or if there's only one mask, select it right away
        if (getEditableDeviceMasks().isEmpty() && !isInEditMode()) {
            TinyMessageBus.post(new AddNewMask());
        }

        if (!isInEditMode())
            TinyMessageBus.post(new RefreshOptionButtons(getEnableDelete(), checkEnableSave(), getEditableDeviceMasks().size() < maxMasksPerDevice));
    }

    private boolean getEnableDelete(){
        return selectedMaskIndex > -1;
    }

    private DrawVertex getSelectedVertex(DrawMask selectedMask, float xTouched, float yTouched) {
        if (selectedMask == null)
            return null;

        DrawVertex touched = null;

        for (DrawVertex vertex : selectedMask.drawVertices) {
            if ((vertex.x - xTouched) * (vertex.x - xTouched) +
                    (vertex.y - yTouched) * (vertex.y - yTouched)
                    <= ((getDragPointRadiusLarge() * getDragPointRadiusLarge()) * fatFingerAllowance)) {
                touched = vertex;
            }
        }

        return touched;
    }

    private void resetLockedPoints(DrawVertex point) {
        DrawMask region = getEditableDeviceMasks().get(selectedMaskIndex);

        for (int i = point.indexInRegion - 1; i <= point.indexInRegion + 1; i++) {
            DrawVertex p;
            if (i < 0)
                p = region.drawVertices.get(region.drawVertices.size() - 1);
            else if (i > region.drawVertices.size() - 1) {
                p = region.drawVertices.get(0);
            } else
                p = region.drawVertices.get(i);
            p.resetLocks(region);
        }
    }

    private DrawMask getSelectedRegion() {
        DrawMask selectedRegion = null;
        if (selectedMaskIndex >= 0)
            selectedRegion = getEditableDeviceMasks().get(selectedMaskIndex);
        return selectedRegion;
    }

    private void setSelectedMaskIndex(int index) {
        lastSelectedMaskIndex = new Integer(selectedMaskIndex);
        selectedMaskIndex = index;
    }

    private void changeUserViewingMode(UserMode mode) {
        if (mode != TOGGLE) {
            showingOptions = mode == SHOW_OPTIONS;
            TinyMessageBus.post(new SwitchUserModeRequest(mode));
            return;
        }

        if (showingOptions) {
            TinyMessageBus.post(new SwitchUserModeRequest(HIDE_OPTIONS));
            showingOptions = false;
        } else {
            TinyMessageBus.post(new SwitchUserModeRequest(SHOW_OPTIONS));
            showingOptions = true;
        }
    }

    private float getBorderTrigger() {
        return borderTrigger;
    }
}

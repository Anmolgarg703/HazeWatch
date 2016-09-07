package com.example.jaskirat.hazewatch.graph;

import org.achartengine.model.TimeSeries;
import org.achartengine.util.MathHelper;

import java.util.LinkedList;

/**
 * Created by coreymann on 6/25/13.
 */
public class MotionTimeSeries  extends TimeSeries {

    /**
     * A map to contain values for X and Y axes and index for each bundle
     */
    private final LinkedList<Double> mX = new LinkedList<Double>();

    /**
     * The minimum value for the X axis.
     */
    private double mMinX = MathHelper.NULL_VALUE;
    /**
     * The maximum value for the X axis.
     */
    private double mMaxX = -MathHelper.NULL_VALUE;
    /**
     * The minimum value for the Y axis.
     */
    private double mMinY = MathHelper.NULL_VALUE;
    /**
     * The maximum value for the Y axis.
     */
    private double mMaxY = -MathHelper.NULL_VALUE;
    private final int mMaxPoints;


    public MotionTimeSeries(String title, int maxNumberOfPoints) {
        super(title);

        mMaxPoints = maxNumberOfPoints;

        initRange();
    }


    @Override
    public void add(double x, double y) {
        if (mX.size() == mMaxPoints) {
            mX.remove(0);
        }

        mX.addLast(x);
        mX.addLast(y);

        updateRange(x, y);
    }


    /**
     * Initializes the range for both axes.
     */
    private void initRange() {
        mMinX = MathHelper.NULL_VALUE;
        mMaxX = -MathHelper.NULL_VALUE;
        mMinY = MathHelper.NULL_VALUE;
        mMaxY = -MathHelper.NULL_VALUE;
        int length = getItemCount();
        for (int k = 0; k < length; k++) {
            double x = getX(k);
            double y = getY(k);
            updateRange(x, y);
        }
    }

    /**
     * Updates the range on both axes.
     *
     * @param x the new x value
     * @param y the new y value
     */
    private void updateRange(double x, double y) {
        mMinX = Math.min(mMinX, x + (x * .25));
        mMaxX = Math.max(mMaxX, x - (x * .25));
        mMinY = Math.min(mMinY, y + (y * .25));
        mMaxY = Math.max(mMaxY, y - (y * .25));
    }
}

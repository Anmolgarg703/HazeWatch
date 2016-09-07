package com.example.jaskirat.hazewatch.graph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.R;
import com.variable.framework.node.reading.MotionReading;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.NumberFormat;

/**
 * Created by coreymann on 6/13/13.
 */
public class MotionGraph {
    private static final int MAX_POINTS = 250;

    public static final int X_INDEX = 0;
    public static final int Y_INDEX = 1;
    public static final int Z_INDEX = 2;


    private final MotionTimeSeries mXSeries        = new MotionTimeSeries(" x ", MAX_POINTS);
    private final MotionTimeSeries mYSeries        = new MotionTimeSeries(" y ", MAX_POINTS);
    private final MotionTimeSeries mZSeries        = new MotionTimeSeries(" z" , MAX_POINTS);
    private final XYMultipleSeriesDataset mDataSet = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer;
    private Float mScale;
    /**
     *
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     */
    public MotionGraph(String chartTitle, String xAxisTitle, String yAxisTitle, float scale, boolean showLegend){
        mScale = scale;

        mDataSet.addSeries(X_INDEX, mXSeries);
        mDataSet.addSeries(Y_INDEX, mYSeries);
        mDataSet.addSeries(Z_INDEX, mZSeries);

        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setShowGridY(true);
        mRenderer.setAntialiasing(false);
        mRenderer.setYAxisAlign(Paint.Align.LEFT, 0);
        mRenderer.setXLabelsAlign(Paint.Align.LEFT);

        //Setting Chart Title and Size
        mRenderer.setChartTitle(chartTitle);
        mRenderer.setChartTitleTextSize(35f);

        //Coloring
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setGridColor(Color.LTGRAY);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYAxisColor(Color.BLACK);

        //User Input Control
        mRenderer.setClickEnabled(false);
        mRenderer.setPanEnabled(false);
        mRenderer.setZoomEnabled(false, false);

        //Colors And Padding
        mRenderer.setLabelsTextSize(32);

        //Initialize Renders
        XYSeriesRenderer rendererGreen = new XYSeriesRenderer();
            rendererGreen.setChartValuesFormat(NumberFormat.getNumberInstance());
            rendererGreen.setShowLegendItem(true);
            rendererGreen.setLineWidth(3f);
            rendererGreen.setColor(Color.GREEN);
        mRenderer.addSeriesRenderer(X_INDEX, rendererGreen);

        XYSeriesRenderer rendererRed = new XYSeriesRenderer();
            rendererRed.setChartValuesFormat(NumberFormat.getNumberInstance());
            rendererRed.setShowLegendItem(true);
            rendererRed.setLineWidth(3f);
            rendererRed.setColor(Color.RED);

        mRenderer.addSeriesRenderer(Y_INDEX, rendererRed);

        XYSeriesRenderer rendererWhite = new XYSeriesRenderer();
            rendererWhite.setChartValuesFormat(NumberFormat.getNumberInstance());
            rendererWhite.setShowLegendItem(true);
            rendererWhite.setLineWidth(3f);
            rendererWhite.setColor(Color.BLUE);
        mRenderer.addSeriesRenderer(Z_INDEX, rendererWhite);

        displayLegend(showLegend);
    }

    /**
     * Returns a GraphicalView that represents the latest contents in the data set.
     * @return
     */
    public GraphicalView getView(Context context){
       GraphicalView v = ChartFactory.getTimeChartView(context, mDataSet, mRenderer, "H:mm:ss");
        v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.graph_background));
        return v;
    }

    public void displayLegend(boolean show){ mRenderer.setShowLegend(show);}
    /**
     * The percentage scale controls the Y range. The scale is used to determine, MaxY * scale + MaxY and MinY * scale + MinY.
     * @param scale - percentage value from 0 - 1
     */
    public void setPercentageScale(float scale){
        mScale = scale;

    }

    /**
     * Updates the renderer's scaling for the y axis. The min and max are scaled by half of the set scale and the max Y point found.
     */
    public void scale(){
        double maxY = mXSeries.getMaxY();
               maxY = Math.max(maxY, mYSeries.getMaxY());
               maxY = Math.max(maxY, mZSeries.getMaxY());


        double minY = mXSeries.getMinY();
               minY = Math.min(minY, mYSeries.getMinY());
               minY = Math.min(minY, mZSeries.getMinY());

        double halfScale = mScale / 2.0;
        double scaleValue = maxY * halfScale;

        if(maxY == 0.0 && minY == 0.0){
            scaleValue = 0.025;
        }
        mRenderer.setRange(new double[] { mXSeries.getMinX(), mXSeries.getMaxX(), minY - scaleValue,  maxY + scaleValue});

    }

    public void addPoint(MotionReading<Double> reading){
        if(mXSeries.getItemCount() >= MAX_POINTS){
            mXSeries.remove(0);
            mYSeries.remove(0);
            mZSeries.remove(0);
        }

        //Add Points to Series.
        mXSeries.add(reading.getTimeStamp(), reading.getX());
        mYSeries.add(reading.getTimeStamp(), reading.getY());
        mZSeries.add(reading.getTimeStamp(), reading.getZ());

        scale();
    }
}

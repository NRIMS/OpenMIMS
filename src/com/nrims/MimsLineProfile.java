/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nrims;


import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

/**
 *
 * @author cpoczatek
 */
public class MimsLineProfile extends JFrame{

    private JFreeChart chart;
    private ChartPanel chartPanel;
    
    public MimsLineProfile() {
        super("Dynamic Line Profile");
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        this.pack();
        this.setVisible(true);
    }

    /*
    
    private void setupLineProfile() {
        // Create arbitrary dataset
        XYSeries dataset = new XYSeries();
        double[] values = new double[100];
        for (int i = 0; i < 100; i++) {
            values[i] = 10*i;
        }
        
        XYSeries series = new XYSeries("Average Size");
        series.add(20.0, 10.0);
        series.add(40.0, 20.0);
        series.add(70.0, 50.0);
        XYDataset xyDataset = new XYSeriesCollection(series);
        chart = ChartFactory.createLineChart("Sample XY Chart", // Title
                "Height", // X-Axis label
                "Weight", // Y-Axis label
                xyDataset, // Dataset
                true // Show legend
                );

        
        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(350, 250);
        this.add(chartPanel);
    }
    
    */
    
    
        /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        
        XYSeries series1 = new XYSeries("N");
        series1.add(1.0, 2.0);
        series1.add(2.0, 2.0);
        series1.add(3.0, 2.0);
        series1.add(4.0, 2.0);
        series1.add(5.0, 2.0);
        series1.add(6.0, 2.0);
        series1.add(7.0, 2.0);
        series1.add(8.0, 2.0);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "",      // chart title
            "L",                      // x axis label
            "P",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }
    
    public void updateData(double[] newdata, String name) {
        if (newdata == null) {
            return;
        }
        
        XYSeries series = new XYSeries(name);
        for(int i = 0; i< newdata.length; i++) {
            series.add(i, newdata[i]);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        org.jfree.chart.plot.XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDataset(dataset);

        chart.fireChartChanged();
    }
    
}

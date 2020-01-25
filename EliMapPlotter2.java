import java.awt.Font;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EliMapPlotter2 extends JFrame implements Runnable {

	private String title = "Mapa recorrido por el robot";
	private String xAxisLabel = "Valor X Odométrico";
	private String yAxisLabel = "Valor Y Odométrico";

	private XYSeriesCollection path;

	private JFreeChart chart;

	public EliMapPlotter2() {

		path = new XYSeriesCollection(new XYSeries("Control de obstaculos"));

		chart = createChart(); // creo el chart

		ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
		setContentPane(panel);

		pack(); // Ajustar la ventana al tamaño del chart
		setLocationRelativeTo(null); // Centrar
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // mostrar

	}

	public void update(double x, double y) {

		path.getSeries(0).add(x, y); // Agrego una nueva posición X e Y a la
										// ruta

		chart.getXYPlot().setDataset(path); // Actualiza en tiempo de ejecución
											// el chart con el nuevo DataSet

	}

	private JFreeChart createChart() {

		/*
		 * final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		 * renderer.setSeriesLinesVisible(0, false);
		 * renderer.setSeriesShapesVisible(1, false);
		 */
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
		renderer1.setSeriesLinesVisible(0, false);
		NumberAxis rangeAxis1 = new NumberAxis(yAxisLabel);
		XYPlot subplot1 = new XYPlot(path, null, rangeAxis1, renderer1);
		subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		XYTextAnnotation annotation = new XYTextAnnotation("Hello!", 50.0,
				10000.0);
		annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
		annotation.setRotationAngle(Math.PI / 4.0);
		subplot1.addAnnotation(annotation);

		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis(
				xAxisLabel));
		plot.setGap(10.0);
		plot.add(subplot1, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		// return
		// ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,newDataSet,
		// PlotOrientation.VERTICAL,true,false,false);
		return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
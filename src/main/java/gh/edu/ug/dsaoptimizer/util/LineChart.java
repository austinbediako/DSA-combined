package gh.edu.ug.dsaoptimizer.util;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal line-chart PNG renderer using only JDK classes (java.awt +
 * javax.imageio) -- no external plotting library dependency needed.
 * The brief explicitly allows built-in utilities for "plotting export"
 * (section 8.2); this class is plumbing, not assessed core logic, so
 * java.util.List is fine here.
 */
public final class LineChart {

    private static final Color[] DEFAULT_COLORS = {
            new Color(0x1f77b4), new Color(0xff7f0e), new Color(0x2ca02c),
            new Color(0xd62728), new Color(0x9467bd), new Color(0x8c564b)
    };

    private final String title;
    private final String xLabel;
    private final String yLabel;
    private final List<Series> series = new ArrayList<>();

    public LineChart(String title, String xLabel, String yLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public LineChart addSeries(String name, double[] x, double[] y) {
        series.add(new Series(name, x, y, DEFAULT_COLORS[series.size() % DEFAULT_COLORS.length]));
        return this;
    }

    public void saveTo(Path pngPath) throws IOException {
        int width = 900;
        int height = 600;
        int marginLeft = 90;
        int marginBottom = 70;
        int marginTop = 60;
        int marginRight = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Series s : series) {
            for (double v : s.x) {
                minX = Math.min(minX, v);
                maxX = Math.max(maxX, v);
            }
            for (double v : s.y) {
                maxY = Math.max(maxY, v);
            }
        }
        if (maxX == minX) maxX = minX + 1;
        if (maxY == 0) maxY = 1;

        int plotWidth = width - marginLeft - marginRight;
        int plotHeight = height - marginTop - marginBottom;

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString(title, marginLeft, 30);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // axes
        g.drawLine(marginLeft, marginTop, marginLeft, marginTop + plotHeight);
        g.drawLine(marginLeft, marginTop + plotHeight, marginLeft + plotWidth, marginTop + plotHeight);

        // y axis ticks
        int yTicks = 5;
        for (int i = 0; i <= yTicks; i++) {
            double value = maxY * i / yTicks;
            int py = marginTop + plotHeight - (int) (plotHeight * i / (double) yTicks);
            g.drawLine(marginLeft - 5, py, marginLeft, py);
            String label = formatNumber(value);
            g.drawString(label, marginLeft - 10 - g.getFontMetrics().stringWidth(label), py + 5);
        }

        // x axis ticks (use the first series' x values as tick positions)
        if (!series.isEmpty()) {
            double[] xs = series.get(0).x;
            for (double v : xs) {
                int px = marginLeft + (int) (plotWidth * (v - minX) / (maxX - minX));
                g.drawLine(px, marginTop + plotHeight, px, marginTop + plotHeight + 5);
                String label = formatNumber(v);
                g.drawString(label, px - g.getFontMetrics().stringWidth(label) / 2,
                        marginTop + plotHeight + 20);
            }
        }

        g.drawString(xLabel, marginLeft + plotWidth / 2 - g.getFontMetrics().stringWidth(xLabel) / 2,
                height - 15);

        Graphics2D gRotated = (Graphics2D) g.create();
        gRotated.rotate(-Math.PI / 2);
        gRotated.drawString(yLabel, -(marginTop + plotHeight / 2 + g.getFontMetrics().stringWidth(yLabel) / 2), 20);
        gRotated.dispose();

        // series lines + legend
        int legendY = marginTop;
        for (Series s : series) {
            g.setColor(s.color);
            g.setStroke(new BasicStroke(2f));
            int prevPx = -1, prevPy = -1;
            for (int i = 0; i < s.x.length; i++) {
                int px = marginLeft + (int) (plotWidth * (s.x[i] - minX) / (maxX - minX));
                int py = marginTop + plotHeight - (int) (plotHeight * s.y[i] / maxY);
                if (prevPx != -1) {
                    g.drawLine(prevPx, prevPy, px, py);
                }
                g.fillOval(px - 3, py - 3, 6, 6);
                prevPx = px;
                prevPy = py;
            }
            g.fillRect(marginLeft + plotWidth - 150, legendY, 12, 12);
            g.setColor(Color.BLACK);
            g.drawString(s.name, marginLeft + plotWidth - 133, legendY + 11);
            legendY += 20;
        }

        g.dispose();
        ImageIO.write(image, "png", pngPath.toFile());
    }

    private static String formatNumber(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.format("%.2f", value);
    }

    private static final class Series {
        final String name;
        final double[] x;
        final double[] y;
        final Color color;

        Series(String name, double[] x, double[] y, Color color) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}

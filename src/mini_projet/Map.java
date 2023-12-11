/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mini_projet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.google.GoogleMapsTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;


public class Map extends JXMapViewer implements Serializable, PropertyChangeListener {

    public static final String PROP_POSITION = "position";
    private static final long serialVersionUID = 1L;

    private GeoPosition currentPosition;
    private final Image image;

    public Map() {
        currentPosition = new GeoPosition(11.636895, 104.883817);
        setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        initMap();
        image = new ImageIcon(getClass().getResource("/mini_projet/pin.png")).getImage();
        addPropertyChangeListener(this); // Listen to own property changes
    }

    private void initMap() {
        setAddressLocation(currentPosition);
        setZoom(12);
        MouseInputListener mm = new PanMouseInputListener(this);
        addMouseListener(mm);
        addMouseMotionListener(mm);
        addMouseWheelListener(new ZoomMouseWheelListenerCenter(this));
    }

    public GeoPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GeoPosition newPosition) {
        GeoPosition oldPosition = currentPosition;
        currentPosition = newPosition;
        setAddressLocation(currentPosition);
        firePropertyChange(PROP_POSITION, oldPosition, currentPosition);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PROP_POSITION)) {
            repaint(); // Repaint map when the current position changes
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = getWidth() / 2 - 12;
        int y = getHeight() / 2 - 24;
        g2.drawImage(image, x, y, null);
        Area area = new Area(new Rectangle.Double(0, 0, getWidth(), getHeight()));
        area.subtract(new Area(new RoundRectangle2D.Double(5, 5, getWidth() - 10, getHeight() - 10, 20, 20)));
        g2.setColor(new Color(255, 255, 255));
        g2.fill(area);
        g2.dispose();
    }

    public void zoomIn() {
        int currentZoom = getZoom();
        setZoom(currentZoom + 1);
    }

    public void zoomOut() {
        int currentZoom = getZoom();
        setZoom(currentZoom - 1);
    }



   
}
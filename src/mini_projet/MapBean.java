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
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

public class MapBean extends JXMapViewer implements Serializable, PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private static final String FILENAME = "map.ser"; // Serialization file name

    public static final String PROP_POSITION = "position";

    private GeoPosition currentPosition;
    
    private final Image image;

   public MapBean() {
    currentPosition = new GeoPosition(11.636895, 104.883817);
    try {
        setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
    } catch (Exception e) {
        e.printStackTrace();
    }
    initMap();
    image = new ImageIcon(getClass().getResource("/mini_projet/pin.png")).getImage();
    addPropertyChangeListener(this); // Listen to own property changes
    
    // Load serialized map when the object is created
    loadSerializedMap();
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



   @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PROP_POSITION)) {
            repaint(); // Repaint map when the current position changes
            serializeMap(); // Serialize the map after any change in position
        }
    }
private void serializeMap() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
        out.writeInt(getZoom()); // Serialize the zoom state
        out.writeObject(currentPosition); // Serialize the position
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void deserializeMap() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
        setZoom(in.readInt()); // Deserialize the zoom state
        setCurrentPosition((GeoPosition) in.readObject()); // Deserialize the position
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}


    public void loadSerializedMap() {
        deserializeMap();
    }
}
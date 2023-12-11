package mini_projet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jxmapviewer.viewer.GeoPosition;

public class MapButtonsBean extends JPanel {

    private MapBean map;
    private JLabel positionLabel;
    private JTextField latTextField;
    private JTextField lonTextField;
    private JButton chargerButton;

    public MapButtonsBean() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        map = new MapBean();
        positionLabel = new JLabel("Current Position: " + map.getCurrentPosition());

        latTextField = new JTextField(10);
        lonTextField = new JTextField(10);

        chargerButton = new JButton("Charger");
        chargerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double lat = Double.parseDouble(latTextField.getText());
                    double lon = Double.parseDouble(lonTextField.getText());
                    map.setCurrentPosition(new GeoPosition(lat, lon));
                    positionLabel.setText("Current Position: " + map.getCurrentPosition());
                    map.repaint(); // Refresh the map display after setting new coordinates
                } catch (NumberFormatException ex) {
                    positionLabel.setText("Invalid coordinates!");
                }
            }
        });

        JButton agrandirButton = new JButton("Réduire (-)");
        agrandirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map.setZoom(map.getZoom() + 1);
            }
        });

        JButton reduireButton = new JButton("Agrandir (+)");
        reduireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map.setZoom(map.getZoom() - 1);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Latitude:"));
        inputPanel.add(latTextField);
        inputPanel.add(new JLabel("Longitude:"));
        inputPanel.add(lonTextField);
        inputPanel.add(chargerButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agrandirButton);
        buttonPanel.add(reduireButton);

        add(inputPanel, BorderLayout.NORTH);
        add(map, BorderLayout.CENTER);
        add(positionLabel, BorderLayout.SOUTH); // Add positionLabel only to SOUTH
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

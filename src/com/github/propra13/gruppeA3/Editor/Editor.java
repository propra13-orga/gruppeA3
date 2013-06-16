package com.github.propra13.gruppeA3.Editor;

import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Room;

/* @author CK
 * Hauptklasse des Map-Editors
 */
public class Editor extends JTabbedPane {
  
	private static final long serialVersionUID = 1L;
  
	private Room activeRoom;
	private JPanel activeTab;
	private String mapName = "Map02";
	@SuppressWarnings("unused")
	private LinkedList<JPanel> roomTabs = new LinkedList<JPanel>();

	public Editor() {
		super(JTabbedPane.TOP);
    
		/*//zu bearbeitende Map wählen
		JFileChooser fc = new JFileChooser(); 
		fc.setCurrentDirectory(new java.io.File("./data/maps"));
		fc.setDialogTitle("Karte wählen");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			mapName = fc.getSelectedFile().getName();
			System.out.println("Karte: "+mapName);
		}*/
		
		// init Map
		try {
			Map.initialize(mapName);
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i < Map.mapRooms.length; i++) {
			String title = ""+i;
			addTab(title, new RoomTab(Map.getMapRoom(i)));
		}
		
		repaint();
		setVisible(true);
  }
} 
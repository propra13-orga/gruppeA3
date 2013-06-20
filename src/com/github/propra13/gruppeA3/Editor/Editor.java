package com.github.propra13.gruppeA3.Editor;

import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.GameStatus;

/* @author CK
 * Hauptklasse des Map-Editors
 */
public class Editor extends JTabbedPane {
  
	private static final long serialVersionUID = 1L;
	
	public static Editor editor;
	
	private String mapName;
	@SuppressWarnings("unused")
	private LinkedList<JPanel> roomTabs = new LinkedList<JPanel>();
	public LinkedList<Link> mapLinks = new LinkedList<Link>();

	public Editor(String mapName) {
		super(JTabbedPane.TOP);
		
		editor = this;
		this.mapName = mapName;
		
		// init Map
		try {
			Map.initialize(this.mapName);
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			e.printStackTrace();
		}
		
		//Menü-Tab
		addTab("Menü", new Menu(this));
		
		//Raum-Tabs
		for(int i=0; i < Map.mapRooms.length; i++) {
			String title = ""+i;
			addTab(title, new RoomTab(Map.getMapRoom(i)));
		}
		setSelectedIndex(1);
		repaint();
		setVisible(true);
	}
	
	/** Beendet den Editor und holt Hauptmenü wieder aus der Versenkung
	 */
	public void exit() {

		MenuStart.gameStatus = GameStatus.MAINMENU;
		setVisible(false);
		Game.Menu.remove(this);
		Game.Menu.setVisible(true);
	}

	public void notify(Link link) {
		System.out.println("Link gefunden! "+link.targetFields[0].pos+", "+link.targetFields[1].pos+", Link: "+link);
		mapLinks.add(link);
	}
} 
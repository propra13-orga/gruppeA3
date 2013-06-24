package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;


public class RoomTab extends JPanel implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JPopupMenu dropdown;
	
	private Field affectedField;
	public Room room;
	private int lastFieldType = 2; //speichert letzte Feldkonvertierung für Linksklick
	
	// Dropdown-Menüitems
	private JMenu type;
	private JMenuItem type1;
	private JMenuItem type2;
	
	private JMenuItem addLink;
	private JMenuItem changeLink;
	
	private LinkedList<JMenuItem> removeCandidates = new LinkedList<JMenuItem>();

	public RoomTab(Room room) {
		setBackground(Color.GREEN);
		this.room = room;
		
		// Kontextmenü mit Standardfeldern versehen
		dropdown = new JPopupMenu();
		
		type = new JMenu("Ändere Feldtyp ...");
		dropdown.add(type);
		
		type1 = new JMenuItem("Ändere Typ: Boden");
		type1.addActionListener(this);
		type.add(type1);
		
		type2 = new JMenuItem("Ändere Typ: Wand");
		type2.addActionListener(this);
		type.add(type2);
		
		// Nicht-Standardfelder initialisieren
		addLink = new JMenuItem("Erzeuge Link");
		addLink.addActionListener(this);
		changeLink = new JMenuItem("Ändere Link");
		changeLink.addActionListener(this);
		
		addMouseListener(this);
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		MenuStart.paintRoom(g2d, room, this);
		
	}
	
	// Feld-Edit-Methoden
	public void changeFieldType(int type) {
		//Simple Felder; Boden und Wand
		if(type == 1 || type == 2) {
			affectedField.type = type;
			lastFieldType = type;
			affectedField.link = null;
		}
		
		repaint();
	}
	
	/**
	 * Hebt ein Feld visuell hervor.
	 * @param field Hervorzuhebendes Feld
	 */
	public void highlightField(Field field) {
		
	}
	
	/**
	 * Ruft den Link-Editor mit einem neuen Link auf
	 */
	public void addLink() {
		System.out.println("AddLink gedrückt");
		Editor.editor.linkEditor.showWindow(affectedField);
	}
	
	/**
	 * Ruft den Link-Editor mit dem Link des Felds auf
	 */
	public void changeLink() {
		System.out.println("ChangeLink gedrückt");
		Editor.editor.linkEditor.showWindow(affectedField.link);
	}
	
	// Zeigt das Kontextmenü an der Cursor-Position
	@SuppressWarnings("deprecation")
	private void dropdown(MouseEvent e) {
		System.out.println("Klick: "+e.getX()+":"+e.getY());
		
		// Räume Dropdown-Menü auf (removeCandidates)
		Iterator<JMenuItem> iter = removeCandidates.iterator();
		JMenuItem testItem;
		while(iter.hasNext()) {
			testItem = iter.next();
			if(testItem != null) {
				System.out.println("Räume weg: "+testItem.getLabel());
				dropdown.remove(testItem);
				iter.remove();
			}
		}
		
		// Link-Optionen
		System.out.println("affectedField.link: "+affectedField.link);
		if(affectedField.link != null) {
			dropdown.add(changeLink);
			removeCandidates.add(changeLink);
		}
		// Falls Feld am Raumrand, Link zulassen
		else if( (affectedField.pos.x == Map.ROOMWIDTH -1 || affectedField.pos.x == 0) ||
				 (affectedField.pos.y == Map.ROOMHEIGHT -1 || affectedField.pos.y == 0) ) {
			dropdown.add(addLink);
			removeCandidates.add(addLink);
		}
		
		dropdown.show(this, e.getX(), e.getY());
	}
	
	/**
	 * Implementierte ActionListener-Methode
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.type1)
            changeFieldType(1);
		else if(e.getSource() == this.type2) 
			changeFieldType(2);
		else if(e.getSource() == this.addLink)
			addLink();
		else if(e.getSource() == this.changeLink)
			changeLink();
	}

	/**
	 * Implementierte MouseListener-Methode
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		affectedField = room.getField(new Position(e.getX(), e.getY()));
		if(SwingUtilities.isRightMouseButton(e)) {
			dropdown(e);
		}
		else if(SwingUtilities.isLeftMouseButton(e)) {
			// Falls der nächste Klick ein Auswahlklick ist
			if(Editor.editor.chooseClick != Editor.ChooseClickType.NONE){
				switch(Editor.editor.chooseClick) {
				case LINK:
					Editor.editor.linkEditor.chooseClick(affectedField);
					break;
				case CHECKPOINTFIELD:
					break;
				case CHECKPOINTLINK:
					break;
				default:
					break;
				}
			}
			// Ansonsten Standardaktion bei Linksklick
			else	
				changeFieldType(lastFieldType);
		}
	}

	
	
	// Dummies (interface-Methoden wo nichts passiert)
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	

}

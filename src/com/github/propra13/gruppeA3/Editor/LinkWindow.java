package com.github.propra13.gruppeA3.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Editor.Editor.ChooseClickType;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Klasse für den Link-Edit-Dialog. Wird im Editor-Konstruktor
 * konstruiert und bei Bedarf mit Werten befüllt und dargestellt.
 * @author Christian Krüger
 *
 */
public class LinkWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final static int MINHEIGHT = 250;
	private final static int MINWIDTH = 300;
	
	Link workingLink; //Arbeitskopie des Links
	Link linkToEdit; //Link, der bearbeitet wird
	
	//Link-Part, der mittels Auswahlklick geändert werden soll
	int chooseClickLinkPart = -1;
	
	//Buttons, Labels
	JLabel linkTitle, linkID, roomIDA, roomIDB, fieldPosA, fieldPosB;
	JButton bDone, bCancel, bDelete, bChangeA, bChangeB;
	
	Field affectedField; //Zwischenspeicher für Auswahlklick
	
	/**
	 * Konstruktor für den Link-Editor
	 */
	public LinkWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Link-Editor");
		setResizable(false);
		
		/*
		 * Fensterelemente
		 */
		
		linkTitle = new JLabel("Link-ID:");
		linkTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints titleConstraints = new GridBagConstraints();
		titleConstraints.gridx = titleConstraints.gridy = 0;
		titleConstraints.gridheight = titleConstraints.gridwidth = 1;
		titleConstraints.fill = GridBagConstraints.HORIZONTAL;
		titleConstraints.weightx = titleConstraints.weighty = 1;
		titleConstraints.insets = new Insets(4,4,4,4);
		add(linkTitle, titleConstraints);
		
		linkID = new JLabel();
		linkID.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints idConstraints = new GridBagConstraints();
		idConstraints.gridx = 1;
		idConstraints.gridy = 0;
		idConstraints.gridheight = idConstraints.gridwidth = 1;
		idConstraints.fill = GridBagConstraints.HORIZONTAL;
		idConstraints.weightx = idConstraints.weighty = 1;
		idConstraints.insets = new Insets(4,4,4,4);
		add(linkID, idConstraints);
		
		//Untere Buttons
		bDone = new JButton("Fertig");
		bDone.addActionListener(this);
		GridBagConstraints doneConstraints = new GridBagConstraints();
		doneConstraints.gridx = 0;
		doneConstraints.gridy = 7;
		doneConstraints.gridheight = doneConstraints.gridwidth = 1;
		doneConstraints.fill = GridBagConstraints.HORIZONTAL;
		doneConstraints.weightx = doneConstraints.weighty = 1;
		doneConstraints.insets = new Insets(1,4,2,4);
		add(bDone, doneConstraints);
		
		bCancel = new JButton("Abbrechen");
		bCancel.addActionListener(this);
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx = 1;
		cancelConstraints.gridy = 7;
		cancelConstraints.gridheight = cancelConstraints.gridwidth = 1;
		cancelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelConstraints.weightx = cancelConstraints.weighty = 1;
		cancelConstraints.insets = new Insets(1,4,2,4);
		add(bCancel, cancelConstraints);
		
		bDelete = new JButton("Link löschen");
		bDelete.addActionListener(this);
		GridBagConstraints deleteConstraints = new GridBagConstraints();
		deleteConstraints.gridx = 0;
		deleteConstraints.gridy = 6;
		deleteConstraints.gridheight = 1;
		deleteConstraints.gridwidth = 2;
		deleteConstraints.fill = GridBagConstraints.HORIZONTAL;
		deleteConstraints.weightx = deleteConstraints.weighty = 1;
		deleteConstraints.insets = new Insets(2,4,1,4);
		add(bDelete, deleteConstraints);
		
		
		//Linker Teil
		// Rahmen
		JPanel panelA = new JPanel();
		GridBagLayout layoutA = new GridBagLayout();
		panelA.setLayout(layoutA);
		TitledBorder borderA;
		borderA = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Link-Teil A");
		borderA.setTitleJustification(TitledBorder.CENTER);
		panelA.setBorder(borderA);
		
		GridBagConstraints constraintsA = new GridBagConstraints();
		constraintsA.gridx = 0;
		constraintsA.gridy = 1;
		constraintsA.gridwidth = 1;
		constraintsA.gridheight = 1;
		constraintsA.weighty = 5;
		constraintsA.weightx = 1;
		constraintsA.fill = GridBagConstraints.BOTH;
		constraintsA.insets = new Insets(10,0,0,0);
		add(panelA, constraintsA);
		
		
		// Inhalt
		
		JLabel roomA = new JLabel("Raum");
		roomA.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints roomAConstraints = new GridBagConstraints();
		roomAConstraints.gridx = 0;
		roomAConstraints.gridy = 0;
		roomAConstraints.gridheight = roomAConstraints.gridwidth = 1;
		roomAConstraints.fill = GridBagConstraints.HORIZONTAL;
		roomAConstraints.weightx = roomAConstraints.weighty = 1;
		roomAConstraints.insets = new Insets(10,10,2,4);
		panelA.add(roomA, roomAConstraints);
		
		roomIDA = new JLabel();
		roomIDA.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints roomIDAConstraints = new GridBagConstraints();
		roomIDAConstraints.gridx = 1;
		roomIDAConstraints.gridy = 0;
		roomIDAConstraints.gridheight = roomIDAConstraints.gridwidth = 1;
		roomIDAConstraints.fill = GridBagConstraints.HORIZONTAL;
		roomIDAConstraints.weightx = roomIDAConstraints.weighty = 1;
		roomIDAConstraints.insets = new Insets(10,8,2,4);
		panelA.add(roomIDA, roomIDAConstraints);
		
		JLabel fieldA = new JLabel("Feld");
		fieldA.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints fieldAConstraints = new GridBagConstraints();
		fieldAConstraints.gridx = 0;
		fieldAConstraints.gridy = 1;
		fieldAConstraints.gridheight = fieldAConstraints.gridwidth = 1;
		fieldAConstraints.fill = GridBagConstraints.HORIZONTAL;
		fieldAConstraints.weightx = fieldAConstraints.weighty = 1;
		fieldAConstraints.insets = new Insets(2,10,2,4);
		panelA.add(fieldA, fieldAConstraints);
		
		fieldPosA = new JLabel();
		fieldPosA.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints fieldPosAConstraints = new GridBagConstraints();
		fieldPosAConstraints.gridx = 1;
		fieldPosAConstraints.gridy = 1;
		fieldPosAConstraints.gridheight = fieldPosAConstraints.gridwidth = 1;
		fieldPosAConstraints.fill = GridBagConstraints.HORIZONTAL;
		fieldPosAConstraints.weightx = fieldPosAConstraints.weighty = 1;
		fieldPosAConstraints.insets = new Insets(2,8,2,4);
		panelA.add(fieldPosA, fieldPosAConstraints);
		
		bChangeA = new JButton("Ändern");
		bChangeA.addActionListener(this);
		GridBagConstraints changeAConstraints = new GridBagConstraints();
		changeAConstraints.gridx = 0;
		changeAConstraints.gridy = 2;
		changeAConstraints.gridheight = 1;
		changeAConstraints.gridwidth = 2;
		changeAConstraints.fill = GridBagConstraints.HORIZONTAL;
		changeAConstraints.weightx = changeAConstraints.weighty = 1;
		changeAConstraints.insets = new Insets(2,4,2,4);
		panelA.add(bChangeA, changeAConstraints);
		
		
		//Rechter Teil
		// Rahmen
		
		JPanel panelB = new JPanel();
		GridBagLayout layoutB = new GridBagLayout();
		panelB.setLayout(layoutB);
		TitledBorder borderB;
		borderB = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Link-Teil B");
		borderB.setTitleJustification(TitledBorder.CENTER);
		panelB.setBorder(borderB);
		
		GridBagConstraints constraintsB = new GridBagConstraints();
		constraintsB.gridx = 1;
		constraintsB.gridy = 1;
		constraintsB.gridwidth = 1;
		constraintsB.gridheight = 1;
		constraintsB.weighty = 5;
		constraintsB.weightx = 1;
		constraintsB.fill = GridBagConstraints.BOTH;
		constraintsB.insets = new Insets(10,0,0,0);
		add(panelB, constraintsB);
		

		// Inhalt
		JLabel roomB = new JLabel("Raum");
		roomB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints roomBConstraints = new GridBagConstraints();
		roomBConstraints.gridx = 0;
		roomBConstraints.gridy = 0;
		roomBConstraints.gridheight = roomBConstraints.gridwidth = 1;
		roomBConstraints.fill = GridBagConstraints.HORIZONTAL;
		roomBConstraints.weightx = roomBConstraints.weighty = 1;
		roomBConstraints.insets = new Insets(10,10,2,4);
		panelB.add(roomB, roomBConstraints);
		
		roomIDB = new JLabel();
		roomIDB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints roomIDBConstraints = new GridBagConstraints();
		roomIDBConstraints.gridx = 1;
		roomIDBConstraints.gridy = 0;
		roomIDBConstraints.gridheight = roomIDBConstraints.gridwidth = 1;
		roomIDBConstraints.fill = GridBagConstraints.HORIZONTAL;
		roomIDBConstraints.weightx = roomIDBConstraints.weighty = 1;
		roomIDBConstraints.insets = new Insets(10,8,2,4);
		panelB.add(roomIDB, roomIDBConstraints);
		
		JLabel fieldB = new JLabel("Feld");
		fieldB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints fieldBConstraints = new GridBagConstraints();
		fieldBConstraints.gridx = 0;
		fieldBConstraints.gridy = 1;
		fieldBConstraints.gridheight = fieldBConstraints.gridwidth = 1;
		fieldBConstraints.fill = GridBagConstraints.HORIZONTAL;
		fieldBConstraints.weightx = fieldBConstraints.weighty = 1;
		fieldBConstraints.insets = new Insets(2,10,2,4);
		panelB.add(fieldB, fieldBConstraints);
		
		fieldPosB = new JLabel();
		fieldPosB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints fieldPosBConstraints = new GridBagConstraints();
		fieldPosBConstraints.gridx = 1;
		fieldPosBConstraints.gridy = 1;
		fieldPosBConstraints.gridheight = fieldPosBConstraints.gridwidth = 1;
		fieldPosBConstraints.fill = GridBagConstraints.HORIZONTAL;
		fieldPosBConstraints.weightx = fieldPosBConstraints.weighty = 1;
		fieldPosBConstraints.insets = new Insets(2,8,2,4);
		panelB.add(fieldPosB, fieldPosBConstraints);
		
		bChangeB = new JButton("Ändern");
		bChangeB.addActionListener(this);
		GridBagConstraints changeBConstraints = new GridBagConstraints();
		changeBConstraints.gridx = 0;
		changeBConstraints.gridy = 2;
		changeBConstraints.gridheight = 1;
		changeBConstraints.gridwidth = 2;
		changeBConstraints.fill = GridBagConstraints.HORIZONTAL;
		changeBConstraints.weightx = changeBConstraints.weighty = 1;
		changeBConstraints.insets = new Insets(2,4,2,4);
		panelB.add(bChangeB, changeBConstraints);

		
		panelA.setVisible(true);
		panelB.setVisible(true);
	}
	
	/**
	 * Zeigt das Link-Edit-Fenster mit einem neu erstellten Link
	 * @param field Feld, auf dem der neue Link aufgebaut werden soll
	 */
	public void showWindow(Field field) {
							
		// Erste freie Link-ID suchen
		int freeID = -1;
		
		//Erstellt nach ID sortiertes Array aller Links
		int[] IDs = new int[Editor.editor.mapLinks.size()];
		Iterator<Link> iter = Editor.editor.mapLinks.iterator();
		Link testLink;
		for (int i=0; iter.hasNext(); i++) {
			testLink = iter.next();
			IDs[i] = testLink.ID;
		}
		java.util.Arrays.sort(IDs);
		
		//höchste ID == Anzahl IDs => keine ID zwischen drin frei
		if (IDs[IDs.length - 1] == IDs.length - 1)
			freeID = IDs.length;
		else
			for(int i=0; i < IDs.length; i++) {
				if (IDs[i] > i) {
					freeID = i;
					break;
				}
			}
			
		// Konstruktorargumente vorbereiten
		Room[] targetRooms = new Room[2];
		targetRooms[0] = field.getRoom();
		targetRooms[1] = null;
		Field[] targetFields = new Field[2];
		targetFields[0] = field;
		targetFields[1] = null;
		if(freeID == -1)
			try {
				throw new Exception("Keine freie Link-ID gefunden");
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			this.workingLink = new Link(freeID, targetRooms, targetFields, true, true);
		
		update();
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/**
	 * Zeigt das Link-Editor-Fenster mit dem angegebenen Link
	 * @param link Link, der bearbeitet werden soll
	 */
	public void showWindow(Link link) {
		linkToEdit = link;
		this.workingLink = link.clone();
		update();
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/**
	 * Zeigt Link-Editor erneut an, zB nach einem Magic Click
	 */
	public void showWindow() {
		update();
		setVisible(true);
	}
	
	/**
	 * Liest Fensterinhalt neu aus Link ein
	 */
	private void update() {
		
		linkID.setText(""+workingLink.ID);
		
		roomIDA.setText(""+this.workingLink.targetRooms[0].ID);
		
		//Falls bisher nur ein Link-Part gesetzt wurde
		if(workingLink.targetRooms[1] == null)
			roomIDB.setText("nicht gesetzt");
		else
			roomIDB.setText(""+this.workingLink.targetRooms[1].ID);
		
		
		fieldPosA.setText(""+this.workingLink.targetFields[0].pos);
		
		//Falls bisher nur ein Link-Part gesetzt wurde
		if(workingLink.targetFields[1] == null)
			fieldPosB.setText("nicht gesetzt");
		else
			fieldPosB.setText(""+this.workingLink.targetFields[1].pos);
	}
	
	
	
	/*
	 * Button-Aktionen
	 */
	
	/**
	 * Übergibt Änderungen am Link an den Editor.
	 * Wird aufgerufen, falls "Fertig"-Button gedrückt wird.
	 */
	private void save() {
		linkToEdit.edit(workingLink);
		setVisible(false);
	}
	
	/**
	 * Bricht Link-Edit ab und kehrt zum Editor zurück.
	 * Wird aufgerufen, falls "Abbrechen"-Knopf gedrückt wird.
	 */
	private void cancel() {
		System.out.println("\"Abbrechen\" gedrückt");
		workingLink = null;
		setVisible(false);
	}
	
	/**
	 * Gestattet dem Nutzer, das Feld des angegebenen Link-Parts neu zu wählen.
	 * Wird aufgerufen, falls "Ändern"-Button gedrückt wurde.
	 * @param linkPart Link-Part (bzw. Slot der Link-Target-Arrays), dessen Feld gewählt werden soll (0 oder 1).
	 */
	private void chooseField(int linkPart) {
		chooseClickLinkPart = linkPart;
		Editor.editor.chooseClick = ChooseClickType.LINK;
		setVisible(false);
	}
	
	/**
	 * Löscht den Link, der derzeit bearbeitet wird.
	 * Setzt die Link-Referenzen auf dem Feld sowie die Arbeitskopie-Referenz des Editors auf null.
	 */
	private void deleteLink() {
		linkToEdit.targetFields[0].link = null;
		linkToEdit.targetFields[1].link = null;
		linkToEdit = null;
		setVisible(false);
	}
	
	
	/*
	 * Vererbter Kram
	 */

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bDone)
			save();
		else if(e.getSource() == bCancel)
			cancel();
		else if(e.getSource() == bChangeA)
			chooseField(0);
		else if(e.getSource() == bChangeB)
			chooseField(1);
		else if(e.getSource() == bDelete)
			deleteLink();
		
		
	}
	
	/**
	 * Wird aufgerufen, wenn ein Auswahlklick für einen Link getätigt wurde.
	 * @param affectedField Feld, auf das geklickt wurde.
	 */
	public void chooseClick(Field affectedField) {
		
		//Feld-Check
		//Raumrand
		if( ! (affectedField.pos.x == Map.ROOMWIDTH -1 || affectedField.pos.x == 0) ||
				(affectedField.pos.y == Map.ROOMHEIGHT -1 || affectedField.pos.y == 0) ) {
			System.out.println("LinkWindow :: LINKPOS");
			Editor.editor.warning.showWindow(WarningWindow.Type.LINKPOS);
			return;
		}
		//Feld hat bereits einen Link
		else if(affectedField.link != null) {
			System.out.println("LinkWindow :: LINKEXISTS");
			Editor.editor.warning.showWindow(WarningWindow.Type.LINKEXISTS);
			return;
		}
		
		this.affectedField = affectedField;
		Editor.editor.warning.showWindow(WarningWindow.Type.LINKCHOSEN);
	}
	
	/**
	 * Wird aufgerufen, wenn die Feld-Auswahl bestätigt wurde.
	 */
	public void chooseField() {
		workingLink.targetFields[chooseClickLinkPart] = affectedField;
		workingLink.targetRooms[chooseClickLinkPart] = affectedField.getRoom();
		
		chooseClickLinkPart = -1;
		Editor.editor.chooseClick = Editor.ChooseClickType.NONE;
		affectedField = null;
		
		showWindow();
	}

}

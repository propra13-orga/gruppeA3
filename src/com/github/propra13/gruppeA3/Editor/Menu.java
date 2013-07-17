package com.github.propra13.gruppeA3.Editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Menü-Tab für den Map-Editor
 * @author Christian
 *
 */
public class Menu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JButton bNewMap, bOpen, bSave, bSaveAs, bExit, bAdd, bMove, bDel;
	
	OpenMapWindow openWindow;
	
	JComboBox<String> addRoomBox; //JComboBox vom "Raum hinzufügen"-Dialog
	JComboBox<String> moveRoomFromBox; //linke JComboBox vom "Raum verschieben"-Dialog
	JComboBox<String> moveRoomToBox; //rechte JCombobox vom "Raum verschieben"-Dialog
	
	MapHeader tempHeader; //für "Neue Karte"-Button
	
	
	
	JDialog dialog = null;
	JTextField textField = null;
	
	public Menu() {
		
		openWindow = new OpenMapWindow();
		
		setLayout(new GridBagLayout());
		
		Dimension stdButtonSize = new Dimension(200,30);
		
		//Buttons
		//Insets: Abstand zwischen Elementen, new Insets(oben, links, unten, rechts)
		bNewMap = new JButton("Neue Karte");
		bNewMap.setPreferredSize(stdButtonSize);
		GridBagConstraints newMapConstraints = new GridBagConstraints();
		newMapConstraints.gridx = newMapConstraints.gridy = 0;
		newMapConstraints.gridheight = newMapConstraints.gridwidth = 1;
		newMapConstraints.fill = GridBagConstraints.HORIZONTAL;
		newMapConstraints.weightx = newMapConstraints.weighty = 1;
		newMapConstraints.insets = new Insets(10,4,2,4);
		add(bNewMap, newMapConstraints);
		
		bOpen = new JButton("Öffnen");
		bOpen.setPreferredSize(stdButtonSize);
		GridBagConstraints openConstraints = new GridBagConstraints();
		openConstraints.gridx = 0;
		openConstraints.gridy = 1;
		openConstraints.gridheight = openConstraints.gridwidth = 1;
		openConstraints.fill = GridBagConstraints.HORIZONTAL;
		openConstraints.weightx = openConstraints.weighty = 1;
		openConstraints.insets = new Insets(2,4,2,4);
		add(bOpen, openConstraints);
		
		bSave = new JButton("Speichern");
		bSave.setPreferredSize(stdButtonSize);
		GridBagConstraints saveConstraints = new GridBagConstraints();
		saveConstraints.gridx = 0;
		saveConstraints.gridy = 2;
		saveConstraints.gridheight = saveConstraints.gridwidth = 1;
		saveConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveConstraints.weightx = saveConstraints.weighty = 1;
		saveConstraints.insets = new Insets(2,4,2,4);
		add(bSave, saveConstraints);
		
		bSaveAs = new JButton("Speichern als ...");
		bSaveAs.setPreferredSize(stdButtonSize);
		GridBagConstraints saveAsConstraints = new GridBagConstraints();
		saveAsConstraints.gridx = 0;
		saveAsConstraints.gridy = 3;
		saveAsConstraints.gridheight = saveAsConstraints.gridwidth = 1;
		saveAsConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveAsConstraints.weightx = saveAsConstraints.weighty = 1;
		saveAsConstraints.insets = new Insets(2,4,2,4);
		add(bSaveAs, saveAsConstraints);
		
		bExit = new JButton("Beenden");
		bExit.setPreferredSize(stdButtonSize);
		GridBagConstraints exitConstraints = new GridBagConstraints();
		exitConstraints.gridx = 0;
		exitConstraints.gridy = 4;
		exitConstraints.gridheight = exitConstraints.gridwidth = 1;
		exitConstraints.fill = GridBagConstraints.HORIZONTAL;
		exitConstraints.weightx = exitConstraints.weighty = 1;
		exitConstraints.insets = new Insets(2,4,16,4);
		add(bExit, exitConstraints);
		
		bAdd = new JButton("Raum hinzufügen");
		bAdd.setPreferredSize(stdButtonSize);
		GridBagConstraints addConstraints = new GridBagConstraints();
		addConstraints.gridx = 0;
		addConstraints.gridy = 5;
		addConstraints.gridheight = addConstraints.gridwidth = 1;
		addConstraints.fill = GridBagConstraints.HORIZONTAL;
		addConstraints.weightx = addConstraints.weighty = 1;
		addConstraints.insets = new Insets(2,4,2,4);
		add(bAdd, addConstraints);
		
		bMove = new JButton("Raum verschieben");
		bMove.setPreferredSize(stdButtonSize);
		GridBagConstraints moveConstraints = new GridBagConstraints();
		moveConstraints.gridx = 0;
		moveConstraints.gridy = 6;
		moveConstraints.gridheight = moveConstraints.gridwidth = 1;
		moveConstraints.fill = GridBagConstraints.HORIZONTAL;
		moveConstraints.weightx = moveConstraints.weighty = 1;
		moveConstraints.insets = new Insets(2,4,2,4);
		add(bMove, moveConstraints);
		
		bDel = new JButton("Raum entfernen");
		bDel.setPreferredSize(stdButtonSize);
		GridBagConstraints delConstraints = new GridBagConstraints();
		delConstraints.gridx = 0;
		delConstraints.gridy = 7;
		delConstraints.gridheight = moveConstraints.gridwidth = 1;
		delConstraints.fill = GridBagConstraints.HORIZONTAL;
		delConstraints.weightx = moveConstraints.weighty = 1;
		delConstraints.insets = new Insets(2,4,16,4);
		add(bDel, delConstraints);

		// Button-Actions
		bNewMap.addActionListener(this);
		bOpen.addActionListener(this);
		bSave.addActionListener(this);
		bSaveAs.addActionListener(this);
		bExit.addActionListener(this);
		bAdd.addActionListener(this);
		bMove.addActionListener(this);
		bDel.addActionListener(this);
		
		
		//Unsichtbares Panel rechts
		JPanel dummyRPanel = new JPanel();
		GridBagConstraints dummyRConstraints = new GridBagConstraints();
		dummyRConstraints.gridx = 1;
		dummyRConstraints.gridy = 0;
		dummyRConstraints.gridheight = 8;
		dummyRConstraints.weightx = 7;
		add(dummyRPanel, dummyRConstraints);
		
		//Unsichtbares Panel unter Menüknöpfen
		JPanel dummyUPanel = new JPanel();
		GridBagConstraints dummyUConstraints = new GridBagConstraints();
		dummyUConstraints.gridx = 0;
		dummyRConstraints.gridy = 8;
		dummyRConstraints.gridheight = 1;
		dummyRConstraints.weighty = 10;
		dummyRConstraints.fill = GridBagConstraints.VERTICAL;
		Dimension size = new Dimension(0, 200);
		dummyUPanel.setMinimumSize(size);
		dummyUPanel.setPreferredSize(size);
		add(dummyUPanel, dummyUConstraints);
		
		
		setVisible(true);
	}
	
	// Button-Aktionen
	
	/**Wird aufgerufen, wenn "Neu" gedrückt wurde.*/
	private void bNewMap() {
		tempHeader = new MapHeader("", MapHeader.CUSTOM_MAP, 0, -1);
		
		dialog = new JDialog();
		JLabel text = new JLabel("Name der neuen Karte");
		JButton bOk = new JButton("Ok");
		JButton bCancel = new JButton("Abbrechen");
		textField = new JTextField(30);
		bOk.setActionCommand("new map");
		bCancel.setActionCommand("close dialog");
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
		
		JLabel typeDesc = new JLabel("Typ der neuen Karte");
		JLabel emptyLabel1 = new JLabel("");
		JLabel emptyLabel2 = new JLabel("");
		JLabel emptyLabel3 = new JLabel("");
		
		JRadioButton storyRB = new JRadioButton("Kampagne");
		JRadioButton customRB = new JRadioButton("Einzelspieler");
		JRadioButton deathmatchRB = new JRadioButton("Deathmatch");
		JRadioButton coopRB = new JRadioButton("Co-Op");
		
		storyRB.setActionCommand("story map");
		customRB.setActionCommand("custom map");
		deathmatchRB.setActionCommand("deathmatch map");
		coopRB.setActionCommand("coop map");
		
		storyRB.addActionListener(this);
		customRB.addActionListener(this);
		deathmatchRB.addActionListener(this);
		coopRB.addActionListener(this);
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(storyRB);
		group.add(customRB);
		group.add(deathmatchRB);
		group.add(coopRB);
		customRB.setSelected(true);
		
		dialog.setTitle("Neue Karte");
		dialog.setSize(400, 200);
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		MenuStart.centerWindow(dialog);
		
		GridLayout layout = new GridLayout(6, 2);
		layout.setHgap(6);
		layout.setVgap(6);
		dialog.setLayout(layout);
		
		dialog.add(text);
		dialog.add(textField);
		
		dialog.add(typeDesc);
		dialog.add(storyRB);
		dialog.add(emptyLabel1);
		dialog.add(customRB);
		dialog.add(emptyLabel2);
		dialog.add(deathmatchRB);
		dialog.add(emptyLabel3);
		dialog.add(coopRB);
		
		dialog.add(bOk);
		dialog.add(bCancel);

		dialog.setVisible(true);
		
	}

	/**Wird aufgerufen, wenn "Öffnen" gedrückt wurde.*/
	private void bOpen() {
		openWindow.showWindow();
	}
	
	/**Wird aufgerufen, wenn "Speichern" gedrückt wurde oder "speichern unter" bestätigt wurde.*/
	private void bSave() {
		File mapFile = new File(Map.mapDir+File.separator+Map.header.mapName);
		if(mapFile.exists())
			Editor.editor.warning.showWindow(WarningWindow.Type.OVERWRITE);
		else
			Editor.editor.write();
	}
	
	/**Wird aufgerufen, wenn "Speichern unter" gedrückt wurde.*/
	private void bSaveAs() {
		dialog = new JDialog();
		JLabel text = new JLabel("Speichern unter:");
		JButton bOk = new JButton("Speichern");
		JButton bCancel = new JButton("Abbrechen");
		textField = new JTextField(30);
		bOk.setActionCommand("saveAs");
		bCancel.setActionCommand("close dialog");
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
		
		dialog.setTitle("Speichern unter ...");
		dialog.setSize(400, 80);
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		MenuStart.centerWindow(dialog);
		
		GridLayout layout = new GridLayout(2,2);
		layout.setHgap(6);
		layout.setVgap(6);
		dialog.setLayout(layout);
		dialog.add(text);
		dialog.add(textField);
		dialog.add(bOk);
		dialog.add(bCancel);

		dialog.setVisible(true);
		
	}
	
	/**Wird aufgerufen, wenn "Beenden" gedrückt wurde.*/
	private void bExit() {
		Editor.editor.exit();
	}
	
	/**Wird aufgerufen, wenn "Raum hinzufügen" gedrückt wurde.*/
	private void bAdd() {
		if(! Editor.editor.mapIsOpened)
			Editor.editor.warning.showWindow("Es ist keine Karte geöffnet.");
		else {
			dialog = new JDialog();
			JLabel text = new JLabel("Index des neuen Raums:");
			JButton bOk = new JButton("Hinzufügen");
			JButton bCancel = new JButton("Abbrechen");
			textField = new JTextField(30);
			bOk.setActionCommand("add room");
			bCancel.setActionCommand("close dialog");
			bOk.addActionListener(this);
			bCancel.addActionListener(this);
			
			dialog.setTitle("Raum hinzufügen");
			dialog.setSize(400, 80);
			dialog.setModal(true);
			dialog.setResizable(false);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			
			String[] numbers = new String[Map.mapRooms.length + 1];
			for(int i=0; i < numbers.length; i++)
				numbers[i] = Integer.toString(i);
			
			addRoomBox = new JComboBox<String>(numbers);
			
			MenuStart.centerWindow(dialog);
			
			GridLayout layout = new GridLayout(2,2);
			layout.setHgap(6);
			layout.setVgap(6);
			dialog.setLayout(layout);
			dialog.add(text);
			dialog.add(addRoomBox);
			dialog.add(bOk);
			dialog.add(bCancel);
	
			dialog.setVisible(true);
		}
	}
	
	/**Wird aufgerufen, wenn "Raum verschieben" gedrückt wurde.*/
	private void bMove() {
		if(! Editor.editor.mapIsOpened) {
			Editor.editor.warning.showWindow("Es ist keine Karte geöffnet.");
			return;
		}
		dialog = new JDialog();
		JLabel textFrom = new JLabel("Verschiebe Raum ...");
		JLabel textTo = new JLabel("nach ...");
		JButton bOk = new JButton("Verschieben");
		JButton bCancel = new JButton("Abbrechen");
		bOk.setActionCommand("move room");
		bCancel.setActionCommand("close dialog");
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
		
		dialog.setTitle("Raum verschieben");
		dialog.setSize(350, 120);
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		String[] numbers1 = new String[Map.mapRooms.length];
		for(int i=0; i < numbers1.length; i++)
			numbers1[i] = Integer.toString(i);
		
		String[] numbers2 = new String[Map.mapRooms.length];
		for(int i=0; i < numbers2.length; i++)
			numbers2[i] = Integer.toString(i);
		
		moveRoomFromBox = new JComboBox<String>(numbers1);
		moveRoomToBox = new JComboBox<String>(numbers2);
		
		MenuStart.centerWindow(dialog);
		
		GridLayout layout = new GridLayout(3,2);
		layout.setHgap(6);
		layout.setVgap(6);
		dialog.setLayout(layout);
		dialog.add(textFrom);
		dialog.add(moveRoomFromBox);
		
		dialog.add(textTo);
		dialog.add(moveRoomToBox);
		
		dialog.add(bOk);
		dialog.add(bCancel);

		dialog.setVisible(true);
	}
	
	/**Wird aufgerufen, wenn "Raum löschen" gedrückt wurde.*/
	private void bDel() {
		if(! Editor.editor.mapIsOpened) {
			Editor.editor.warning.showWindow("Es ist keine Karte geöffnet.");
			return;
		}
		dialog = new JDialog();
		JLabel text = new JLabel("Lösche Raum ...");
		JButton bOk = new JButton("Löschen");
		JButton bCancel = new JButton("Abbrechen");
		bOk.setActionCommand("delete room");
		bCancel.setActionCommand("close dialog");
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
		
		dialog.setTitle("Raum löschen");
		dialog.setSize(350, 80);
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		String[] numbers = new String[Map.mapRooms.length];
		for(int i=0; i < numbers.length; i++)
			numbers[i] = Integer.toString(i);
		
		moveRoomFromBox = new JComboBox<String>(numbers);
		
		MenuStart.centerWindow(dialog);
		
		GridLayout layout = new GridLayout(2,2);
		layout.setHgap(6);
		layout.setVgap(6);
		dialog.setLayout(layout);
		dialog.add(text);
		dialog.add(moveRoomFromBox);
		
		dialog.add(bOk);
		dialog.add(bCancel);

		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bNewMap)
			bNewMap();
		else if(e.getSource() == bOpen)
			bOpen();
		else if(e.getSource() == bSave)
			bSave();
		else if(e.getSource() == bSaveAs)
			bSaveAs();
		else if(e.getSource() == bExit)
			bExit();
		else if(e.getSource() == bAdd)
			bAdd();
		else if(e.getSource() == bMove)
			bMove();
		else if(e.getSource() == bDel)
			bDel();
		
		//Dialog-Knöpfe
		else if(e.getActionCommand().equals("new map")) {
			Map.newMap(tempHeader);
			Editor.editor.updateTabs();
			dialog.setVisible(false);
		}
		else if(e.getActionCommand().equals("close dialog"))
			dialog.setVisible(false);
		
		else if(e.getActionCommand().equals("save as")) {
			String text = textField.getText();
			if(text.equals("") || text == null)
				Editor.editor.warning.showWindow("Gib bitte einen Namen an.");
			else {
				Map.header.mapName = textField.getText();
				bSave();
			}
		}
		
		//neue Map: Typ-Radiobuttons
		else if(e.getActionCommand().equals("story map"))
			tempHeader.type = MapHeader.STORY_MAP;
		else if(e.getActionCommand().equals("custom map"))
			tempHeader.type = MapHeader.CUSTOM_MAP;
		else if(e.getActionCommand().equals("deathmatch map"))
			tempHeader.type = MapHeader.DEATHMATCH_MAP;
		else if(e.getActionCommand().equals("coop map"))
			tempHeader.type = MapHeader.COOP_MAP;
		
		
		//Raum hinzufügen
		else if(e.getActionCommand().equals("add room")) {
			for(int i=0; i <= Map.mapRooms.length; i++) {
				String choice = (String)addRoomBox.getSelectedItem();
				if (i == Integer.parseInt(choice)) {
					Editor.editor.addRoom(i);
					break;
				}
			}
			dialog.setVisible(false);
		}
		
		//Raum verschieben
		else if(e.getActionCommand().equals("move room")) {
			String roomToMove = (String)moveRoomFromBox.getSelectedItem();
			String newIndex = (String)moveRoomToBox.getSelectedItem();
			dialog.setVisible(false);
			Editor.editor.moveRoom(Integer.parseInt(roomToMove), Integer.parseInt(newIndex));
		}
		
		//Raum löschen
		else if(e.getActionCommand().equals("delete room")) {
			String roomToDelete = (String)moveRoomFromBox.getSelectedItem();
			dialog.setVisible(false);
			Editor.editor.deleteRoom(Integer.parseInt(roomToDelete));
		}
	}

}

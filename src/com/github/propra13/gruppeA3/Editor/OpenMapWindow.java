package com.github.propra13.gruppeA3.Editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class OpenMapWindow extends JDialog implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	String type, size, strength, health, armor, speed, coinVal;
	JList<JPanel> mapList;
	JTable attributeTable;
	String[][] tableContent;
	DefaultTableModel tableModel;
	
	public OpenMapWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Öffne Karte ...");
		setResizable(false);
		
		/* Monster-Liste //TODO: Dynamisch einlesen
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		mapList = new JList<JPanel>();
		mapList.setCellRenderer(new ListRenderer());
		mapList.addListSelectionListener(this);
		mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mapList.setLayoutOrientation(JList.VERTICAL);
		mapList.setFixedCellHeight(38);
		
		//Liste einlesen
		updateList();
		
		JScrollPane monsterListPane = new JScrollPane(mapList);
		monsterListPane.setPreferredSize(new Dimension(100, 300)); //Größe der Liste
		
		//Liste auf Dialog legen
		GridBagConstraints listConstraints = new GridBagConstraints();
		listConstraints.gridx = 0;
		listConstraints.gridy = 0;
		listConstraints.gridheight = listConstraints.gridwidth = 1;
		listConstraints.fill = GridBagConstraints.BOTH;
		listConstraints.weightx = 1;
		listConstraints.weighty = 6;
		listConstraints.insets = new Insets(1,4,2,4);
		add(monsterListPane, listConstraints);
		
		/*
		 * Monster-Eigenschaften-Tabelle
		 */
		
		type = "unset";
		String[][] tableData =
			{
				{"Name", ""},
				{"Typ", ""},
				{"max. Spieler", ""},
				{"Kamp.-ID", ""}
			};
		
		tableContent = tableData;
		tableModel = new DefaultTableModel(tableContent, tableContent[0]);
		attributeTable = new JTable(tableModel);
		attributeTable.setTableHeader(null);
		GridBagConstraints tableConstraints = new GridBagConstraints();
		tableConstraints.gridx = 1;
		tableConstraints.gridy = 0;
		tableConstraints.gridheight = tableConstraints.gridwidth = 1;
		tableConstraints.fill = GridBagConstraints.BOTH;
		tableConstraints.weightx = 1;
		tableConstraints.weighty = 6;
		tableConstraints.insets = new Insets(1,4,2,4);
		add(attributeTable, tableConstraints);
		
		/*
		 * Buttons
		 */
		bDone = new JButton("Öffnen");
		bDone.addActionListener(this);
		GridBagConstraints doneConstraints = new GridBagConstraints();
		doneConstraints.gridx = 0;
		doneConstraints.gridy = 2;
		doneConstraints.gridheight = doneConstraints.gridwidth = 1;
		doneConstraints.fill = GridBagConstraints.HORIZONTAL;
		doneConstraints.weightx = doneConstraints.weighty = 1;
		doneConstraints.insets = new Insets(1,4,2,4);
		add(bDone, doneConstraints);
		
		bCancel = new JButton("Abbrechen");
		bCancel.addActionListener(this);
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx = 1;
		cancelConstraints.gridy = 2;
		cancelConstraints.gridheight = cancelConstraints.gridwidth = 1;
		cancelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelConstraints.weightx = cancelConstraints.weighty = 1;
		cancelConstraints.insets = new Insets(1,4,2,4);
		add(bCancel, cancelConstraints);
		
	}
	
	private void updateList() {
		//JLabels
		JPanel[] panels = new JPanel[Game.mapHeaders.size()];
		MapHeader testHeader;
		for(int i=0; i < Game.mapHeaders.size(); i++) {
			testHeader = Game.mapHeaders.get(i);
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel(testHeader.mapName);
			panel.add(label);
			panels[i] = panel;
		}
		mapList.setListData(panels);
	}
	
	/**
	 * Zeigt das Karte-Öffnen-Fenster.
	 */
	public void showWindow() {
		
		//Fenster-Setup
		MenuStart.centerWindow(this);
		updateList();
		
		mapList.setSelectedIndex(0);
		updateTable();
		
		setVisible(true);
	}
	
	/**
	 * Updatet die Map-Eigenschaften-Tabelle.
	 */
	private void updateTable() {
		if(mapList.getSelectedIndex() == -1)
			mapList.setSelectedIndex(0);
		MapHeader header = Game.mapHeaders.get(mapList.getSelectedIndex());
		
		//Typ, Kampagnen-ID
		switch(header.type) {
		case MapHeader.STORY_MAP:
			tableContent[1][1] = "Kampagne";						//Typ
			tableContent[3][1] = Integer.toString(header.storyID);	//Kamp.-ID
			break;
		case MapHeader.CUSTOM_MAP:
			tableContent[1][1] = "Einzelspieler";
			tableContent[3][1] = "";
			break;
		case MapHeader.DEATHMATCH_MAP:
			tableContent[1][1] = "Deathmatch";
			tableContent[3][1] = "";
			break;
		case MapHeader.COOP_MAP:
			tableContent[1][1] = "Co-Op";
			tableContent[3][1] = "";
			break;
		default:
			tableContent[1][1] = "Fehler";
			tableContent[3][1] = "";
			break;
		}
		
		tableContent[0][1] = header.mapName;						//Name
		tableContent[2][1] = Integer.toString(header.maxPlayers);	//max. Spieler
		
		// Tabelle updaten
		tableModel.setDataVector(tableContent, tableContent[0]);
		tableModel.fireTableDataChanged();
	}
	
	/*
	 * Button-Aktionen
	 */

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bCancel)
			setVisible(false);
		else if(e.getSource() == bDone) {
			Editor.editor.openMap(Game.mapHeaders.get(mapList.getSelectedIndex()));
			setVisible(false);
		}
		
	}
	
	/**
	 * EventListener für die Auswahl in der Kartenliste
	 * Updatet die Tabelle mit den Karteneigenschaften.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(! e.getValueIsAdjusting()) { //Damit nicht zweimal pro Selection ausgeführt wird
			updateTable();
		}
	}

}

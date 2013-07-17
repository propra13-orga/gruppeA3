package com.github.propra13.gruppeA3.Menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Editor.ListRenderer;
import com.github.propra13.gruppeA3.Map.MapHeader;

public class SingleplayerWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	JList<JPanel> mapList;
	MapHeader[] headers;
	
	public SingleplayerWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Öffne Karte ...");
		setResizable(false);
		MenuStart.centerWindow(this);
		
		/* Monster-Liste //TODO: Dynamisch einlesen
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		mapList = new JList<JPanel>();
		mapList.setCellRenderer(new ListRenderer());
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
		listConstraints.gridheight = 1;
		listConstraints.gridwidth = 2;
		listConstraints.fill = GridBagConstraints.BOTH;
		listConstraints.weightx = 1;
		listConstraints.weighty = 6;
		listConstraints.insets = new Insets(2,4,2,4);
		add(monsterListPane, listConstraints);
		
		/*
		 * Buttons
		 */
		bDone = new JButton("Spielen");
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
		

		mapList.setSelectedIndex(0);
		setVisible(true);
		
	}
	
	private void updateList() {
		//JLabels
		
		//Einzelspielermaps zählen
		int mapCtr = 0;
		for(Iterator<MapHeader> iter = Game.mapHeaders.iterator(); iter.hasNext();)
			if(iter.next().type == MapHeader.CUSTOM_MAP)
				mapCtr++;
		
		JPanel[] panels = new JPanel[mapCtr];
		headers = new MapHeader[mapCtr];
		MapHeader testHeader;
		//Einzelspielermaps zur Liste hinzufügen
		for(Iterator<MapHeader> iter = Game.mapHeaders.iterator(); iter.hasNext();) {
			testHeader = iter.next();
			if(testHeader.type == MapHeader.CUSTOM_MAP) {
				JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel label = new JLabel(testHeader.mapName);
				panel.add(label);
				
				panels[panels.length - mapCtr] = panel;
				headers[headers.length - mapCtr] = testHeader;
				mapCtr--;
			}
		}
		mapList.setListData(panels);
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
			Game.Menu.initMap(headers[mapList.getSelectedIndex()], 0);
			setVisible(false);
		}
		
	}

}

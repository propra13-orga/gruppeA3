package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class ItemWindow extends JDialog implements ActionListener, ListCellRenderer<JPanel>, ListSelectionListener {
private static final long serialVersionUID = 1L;
	
	Monster workingItem; //Arbeitskopie des Items
	Monster itemToEdit; //Item, das geändert werden soll
	Field affectedField; //Feld, auf dem sich das Item befindet
	RoomTab roomTab; //Raum-Tab, auf dessen Raum sich das Item befindet
	
	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	String type, size, strength, health, armor, speed, coinVal;
	boolean isBoss;
	JList<JPanel> itemList;
	JTable attributeTable;
	String[][] tableContent;
	DefaultTableModel tableModel;
	
	//Listeneinträge
	JPanel emptyPanel, scorpPanel, snakePanel, zombiePanel, spiderPanel, bossPanel;
	
	public ItemWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Monster-Editor");
		setResizable(false);
		
		/* Monster-Liste //TODO: Dynamisch einlesen
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		itemList = new JList<JPanel>();
		itemList.setCellRenderer(this);
		itemList.addListSelectionListener(this);
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setLayoutOrientation(JList.VERTICAL);
		itemList.setFixedCellHeight(38);
		
		//JLabels
		JLabel emptyEntry = new JLabel("Kein Monster");
		JLabel scorpEntry = new JLabel("Skorpion", new ImageIcon(GameWindow.monsterimg_1up), JLabel.LEFT);
		JLabel snakeEntry = new JLabel("Schlange", new ImageIcon(GameWindow.monsterimg_2up), JLabel.LEFT);
		JLabel zombieEntry = new JLabel("Zombie", new ImageIcon(GameWindow.monsterimg_3up), JLabel.LEFT);
		JLabel spiderEntry = new JLabel("Spinne", new ImageIcon(GameWindow.monsterimg_4up), JLabel.LEFT);
		//Boss-Icon auf 32x32 verkleinern
		ImageIcon bossIcon = new ImageIcon(GameWindow.bossimg_1up.getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH));
		JLabel bossEntry = new JLabel("Boss-Monster", bossIcon, JLabel.LEFT);
		
		//Zugehörige JPanels
		emptyPanel = new JPanel(new GridBagLayout());
		scorpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		snakePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zombiePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spiderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//Labels auf Panels klatschen
		GridBagConstraints emptyConstr = new GridBagConstraints();
		emptyConstr.fill = GridBagConstraints.HORIZONTAL;
		emptyPanel.add(emptyEntry, emptyConstr);
		scorpPanel.add(scorpEntry);
		snakePanel.add(snakeEntry);
		zombiePanel.add(zombieEntry);
		spiderPanel.add(spiderEntry);
		bossPanel.add(bossEntry);
		
		//Panels auf JList legen; Liste auf Scrollpane legen
		JPanel[] panels = {emptyPanel, scorpPanel, snakePanel, zombiePanel, spiderPanel, bossPanel};
		itemList.setListData(panels);
		JScrollPane monsterListPane = new JScrollPane(itemList);
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
				{"Typ", type},
				{"Größe", size},
				{"Angriffsstärke",strength},
				{"Leben",health},
				{"Rüstung",armor},
				{"Geschwindigkeit",speed},
				{"Münzen",coinVal}
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
		bDone = new JButton("Fertig");
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
	
	/**
	 * Zeigt das Monster-Editor-Fenster mit dem angegebenen Monster
	 * @param room Raum, in dem sich das zu bearbeitende Monster befindet.
	 * @param field Feld, auf dem sich das Monster befindet.
	 * @param monster Monster, das bearbeitet werden soll.
	 */
	public void showWindow(RoomTab roomTab, Field field, Monster monster) {
		this.roomTab = roomTab;
		
		//workingMonster und monsterToEdit setzen
		itemToEdit = monster;
		
		if(itemToEdit == null)
			workingItem = new Monster(Editor.editor.getSelectedIndex(), 1.0, 1, 0, 10, 
					5, 5, "unset", 10, 10, 10, false);
		else
			workingItem = monster.clone();
		
		//Auswahl in Liste setzen
		itemList.setSelectedIndex(workingItem.getType());
		
		//Fenster-Setup
		MenuStart.centerWindow(this);
		update();
		setVisible(true);
	}
	
	/**
	 * Updatet die Monster-Eigenschaften-Tabelle.
	 */
	private void update() {
		if(workingItem.getType() != 0)
			attributeTable.setVisible(true);
		switch(workingItem.getType()) {
		case 1:
			tableContent[0][1] = "Skorpion";	//Typ
			tableContent[1][1] = "todo";		//Größe
			tableContent[2][1] = "todo";		//Stärke
			tableContent[3][1] = "todo";		//Leben
			tableContent[4][1] = "todo";		//Rüstung
			tableContent[5][1] = "todo";		//Geschwindigkeit
			tableContent[6][1] = "todo";		//Münzwert
			isBoss = false;
		case 2:
			tableContent[0][1] = "Schlange";	//Typ
			tableContent[1][1] = "todo";		//Größe
			tableContent[2][1] = "todo";		//Stärke
			tableContent[3][1] = "todo";		//Leben
			tableContent[4][1] = "todo";		//Rüstung
			tableContent[5][1] = "todo";		//Geschwindigkeit
			tableContent[6][1] = "todo";		//Münzwert
			isBoss = false;
			break;
		case 3:
			tableContent[0][1] = "Zombie";		//Typ
			tableContent[1][1] = "todo";		//Größe
			tableContent[2][1] = "todo";		//Stärke
			tableContent[3][1] = "todo";		//Leben
			tableContent[4][1] = "todo";		//Rüstung
			tableContent[5][1] = "todo";		//Geschwindigkeit
			tableContent[6][1] = "todo";		//Münzwert
			break;
		case 4:
			tableContent[0][1] = "Spinne";		//Typ
			tableContent[1][1] = "todo";		//Größe
			tableContent[2][1] = "todo";		//Stärke
			tableContent[3][1] = "todo";		//Leben
			tableContent[4][1] = "todo";		//Rüstung
			tableContent[5][1] = "todo";		//Geschwindigkeit
			tableContent[6][1] = "todo";		//Münzwert
			break;
		case 5:
			tableContent[0][1] = "Boss-Monster";//Typ
			tableContent[1][1] = "todo";		//Größe
			tableContent[2][1] = "todo";		//Stärke
			tableContent[3][1] = "todo";		//Leben
			tableContent[4][1] = "todo";		//Rüstung
			tableContent[5][1] = "todo";		//Geschwindigkeit
			tableContent[6][1] = "todo";		//Münzwert
			isBoss = true;
			break;
		default:
			type = "unbekannt";
			break;
		}
		
		// Tabelle updaten
		tableModel.setDataVector(tableContent, tableContent[0]);
		tableModel.fireTableDataChanged();
	}
	
	/*
	 * Button-Aktionen
	 */
	
	/**
	 * Übernimmt die Änderungen und schließt den Monster-Editor.
	 * Wird aufgerufen, falls "Fertig" gedrückt wurde.
	 */
	private void save() {
		
		//Ein bestehendes Monster wird gelöscht
		if(workingItem.getType() == 0 && itemToEdit != null)
			workingItem.getRoom().entities.remove(itemToEdit);
		
		//Ein bestehendes Monster wird geändert
		else if(workingItem.getType() != 0 && itemToEdit != null)
			itemToEdit.edit(workingItem);
		
		//Ein neues Monster wird hinzugefügt
		else if(workingItem.getType() != 0 && itemToEdit == null)
			workingItem.getRoom().entities.add(workingItem);
		
		itemToEdit = workingItem = null;
		roomTab.clearHighlights();
		roomTab.repaint();
		setVisible(false);
	}
	
	/**
	 * Schließt den Monster-Editor, verwirft alle Änderungen.
	 * Wird aufgerufen, falls "Abbrechen" gedrückt wurde.
	 */
	private void cancel() {
		itemToEdit = workingItem = null;
		roomTab.clearHighlights();
		setVisible(false);
	}

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bCancel)
			cancel();
		else if(e.getSource() == bDone)
			save();
		
	}
	
	/*
	 * Provisorische Display-Methoden für die einzelnen Monster
	 */
	private void showScorp() {
		workingItem.setType(1);
	}
	
	private void showSnake() {
		workingItem.setType(2);
	}
	
	private void showZombie() {
		workingItem.setType(3);
	}
	
	private void showSpider() {
		workingItem.setType(4);
	}
	
	private void showBoss() {
		workingItem.setType(5);
	}

	/**
	 * Listenrenderer für die JLabels in monsterList
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends JPanel> arg0,
			JPanel panel, int cellIndex, boolean isSelected, boolean cellHasFocus) {
		
		Component component = (Component) panel;
	    component.setForeground (Color.white);
	    component.setBackground (isSelected ? UIManager.getColor("Table.focusCellForeground") : Color.white);
	    return component;
	}

	/**
	 * EventListener für die Auswahl in der Monsterliste
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(! e.getValueIsAdjusting()) { //Damit nicht zweimal pro Selection ausgeführt wird
				
			if(itemList.getSelectedIndex() == 0) {
				workingItem.setType(0);
				attributeTable.setVisible(false);
			}
			else {
				attributeTable.setVisible(true);
				if(itemList.getSelectedIndex() == 1)
					showScorp();
				else if(itemList.getSelectedIndex() == 2)
					showSnake();
				else if(itemList.getSelectedIndex() == 3)
					showZombie();
				else if(itemList.getSelectedIndex() == 4)
					showSpider();
				else if(itemList.getSelectedIndex() == 5)
					showBoss();
	
				update();
				attributeTable.setVisible(true);
			}
		}
		
	}

}

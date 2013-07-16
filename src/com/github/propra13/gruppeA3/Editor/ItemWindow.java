package com.github.propra13.gruppeA3.Editor;

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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Entities.Hitbox;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Moveable;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class ItemWindow extends JDialog implements ActionListener, ListSelectionListener {
private static final long serialVersionUID = 1L;
	
	Item workingItem; //Arbeitskopie des Items
	Item itemToEdit; //Item, das geändert werden soll
	Field affectedField; //Feld, auf dem sich das Item befindet
	RoomTab roomTab; //Raum-Tab, auf dessen Raum sich das Item befindet
	
	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	String type, power,  coinVal, element;
	JList<JPanel> itemList;
	JTable attributeTable;
	String[][] tableContent;
	DefaultTableModel tableModel;
	
	//Listeneinträge
	JPanel emptyPanel, healthPanel, poisonPanel, manaPanel,
			iceShieldPanel, waterShieldPanel, fireShieldPanel, physicalShieldPanel,
			iceSwordPanel, waterSwordPanel, fireSwordPanel, physicalSwordPanel;
	
	public ItemWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Item-Editor");
		setResizable(false);
		
		/* Item-Liste //TODO: Dynamisch einlesen
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		itemList = new JList<JPanel>();
		itemList.setCellRenderer(new ListRenderer());
		itemList.addListSelectionListener(this);
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setLayoutOrientation(JList.VERTICAL);
		itemList.setFixedCellHeight(38);
		
		//JLabels
		JLabel emptyEntry = new JLabel("Kein Item");
		JLabel healthEntry = new JLabel("Lebenstrank", new ImageIcon(GameWindow.lifePosion), JLabel.LEFT);
		JLabel poisonEntry = new JLabel("Gifttrank", new ImageIcon(GameWindow.deadlyPosion), JLabel.LEFT);
		JLabel manaEntry = new JLabel("Manatrank", new ImageIcon(GameWindow.manaPosion), JLabel.LEFT);
		JLabel physicalSwordEntry = new JLabel("Schwert", new ImageIcon(GameWindow.sword), JLabel.LEFT);
		JLabel iceSwordEntry = new JLabel("Eisschwert", new ImageIcon(GameWindow.swordeis), JLabel.LEFT);
		JLabel fireSwordEntry = new JLabel("Feuerschwert", new ImageIcon(GameWindow.swordfeuer), JLabel.LEFT);
		JLabel waterSwordEntry = new JLabel("Wasserschwert", new ImageIcon(GameWindow.swordwasser), JLabel.LEFT);
		JLabel physicalShieldEntry = new JLabel("Schild", new ImageIcon(GameWindow.shield), JLabel.LEFT);
		JLabel iceShieldEntry = new JLabel("Eisschild", new ImageIcon(GameWindow.shieldeis), JLabel.LEFT);
		JLabel fireShieldEntry = new JLabel("Feuerschild", new ImageIcon(GameWindow.shieldfeuer), JLabel.LEFT);
		JLabel waterShieldEntry = new JLabel("Wasserschild", new ImageIcon(GameWindow.shieldwasser), JLabel.LEFT);
		
		//Zugehörige JPanels
		emptyPanel = new JPanel(new GridBagLayout());
		healthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		poisonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		manaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		physicalSwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iceSwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fireSwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		waterSwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		physicalShieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iceShieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fireShieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		waterShieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//Labels auf Panels klatschen
		GridBagConstraints emptyConstr = new GridBagConstraints();
		emptyConstr.fill = GridBagConstraints.HORIZONTAL;
		emptyPanel.add(emptyEntry, emptyConstr);
		healthPanel.add(healthEntry);
		poisonPanel.add(poisonEntry);
		manaPanel.add(manaEntry);
		physicalSwordPanel.add(physicalSwordEntry);
		iceSwordPanel.add(iceSwordEntry);
		fireSwordPanel.add(fireSwordEntry);
		waterSwordPanel.add(waterSwordEntry);
		physicalShieldPanel.add(physicalShieldEntry);
		iceShieldPanel.add(iceShieldEntry);
		fireShieldPanel.add(fireShieldEntry);
		waterShieldPanel.add(waterShieldEntry);
		
		//Panels auf JList legen; Liste auf Scrollpane legen
		JPanel[] panels = {emptyPanel, healthPanel, poisonPanel, manaPanel,
				physicalSwordPanel, iceSwordPanel, fireSwordPanel, waterSwordPanel,
				physicalShieldPanel, iceShieldPanel, fireShieldPanel, waterShieldPanel};
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
		 * Item-Eigenschaften-Tabelle
		 */
		
		type = "unset";
		String[][] tableData =
			{
				{"Typ", type},
				{"Stärke",power},
				{"Münzen",coinVal},
				{"Element",element}
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
	 * Zeigt das Item-Editor-Fenster mit dem angegebenen Item
	 * @param room Raum, in dem sich das zu bearbeitende Item befindet.
	 * @param field Feld, auf dem sich das Item befindet.
	 * @param monster Item, das bearbeitet werden soll.
	 */
	public void showWindow(RoomTab roomTab, Field field, Item item) {
		this.roomTab = roomTab;
		this.affectedField = field;
		
		//workingItem und itemToEdit setzen
		itemToEdit = item;
		
		if(itemToEdit == null)
			workingItem = new Item(
					Item.standardHealthPower, 1, 
					new Position((affectedField.pos.x*32+Hitbox.standard.width/2),
					(affectedField.pos.y*32+Hitbox.standard.height/2)),
					"Erfrischt", "Lebenstrank", Item.standardHealthValue,
					Moveable.Element.PHYSICAL
					);
		else
			workingItem = item.clone();
		
		//Auswahl in Liste setzen
		itemList.setSelectedIndex(workingItem.getType());
		
		//Fenster-Setup
		MenuStart.centerWindow(this);
		update();
		setVisible(true);
	}
	
	/**
	 * Updatet die Item-Eigenschaften-Tabelle.
	 */
	private void update() {
		if(workingItem.getType() != 0)
			attributeTable.setVisible(true);
		switch(workingItem.getType()) {
		case 1:
			tableContent[0][1] = "Lebenstrank";					//Typ
			tableContent[1][1] = Item.standardHealthPower+"";	//Stärke
			tableContent[2][1] = Item.standardHealthValue+"";	//Münzwert
			tableContent[3][1] = "";							//Element
			break;
		case 2:
			tableContent[0][1] = "Gifttrank";					//Typ
			tableContent[1][1] = Item.standardPoisonPower+"";	//Stärke
			tableContent[2][1] = Item.standardPoisonValue+"";	//Münzwert
			tableContent[3][1] = "";							//Element
			break;
		case 3:
			tableContent[0][1] = "Manatrank";					//Typ
			tableContent[1][1] = Item.standardManaPower+"";		//Stärke
			tableContent[2][1] = Item.standardManaValue+"";		//Münzwert
			tableContent[3][1] = "";							//Element
			break;
		case 4:
			tableContent[0][1] = "Schwert";						//Typ
			tableContent[1][1] = Item.standardSwordPower+"";	//Stärke
			tableContent[2][1] = Item.standardSwordValue+"";	//Münzwert
			tableContent[3][1] = Moveable.getElementName(workingItem.getElement()); //Element
			break;
		case 5:
			tableContent[0][1] = "Schild";						//Typ
			tableContent[1][1] = Item.standardShieldPower+"";	//Stärke
			tableContent[2][1] = Item.standardShieldValue+"";	//Münzwert
			tableContent[3][1] = Moveable.getElementName(workingItem.getElement()); //Element
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
	 * Übernimmt die Änderungen und schließt den Item-Editor.
	 * Wird aufgerufen, falls "Fertig" gedrückt wurde.
	 */
	private void save() {
		
		//Ein bestehendes Item wird gelöscht
		if(workingItem.getType() == 0 && itemToEdit != null)
			roomTab.room.entities.remove(itemToEdit);
		
		//Ein bestehendes Item wird geändert
		else if(workingItem.getType() != 0 && itemToEdit != null)
			itemToEdit.edit(workingItem);
		
		//Ein neues Item wird hinzugefügt
		else if(workingItem.getType() != 0 && itemToEdit == null)
			roomTab.room.entities.add(workingItem);
		
		itemToEdit = workingItem = null;
		roomTab.clearHighlights();
		roomTab.repaint();
		setVisible(false);
	}
	
	/**
	 * Schließt den Item-Editor, verwirft alle Änderungen.
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
	 * Provisorische Display-Methoden für die einzelnen Items
	 */
	private void showHealth() {
		workingItem.setType(1);
		workingItem.setDamage(Item.standardHealthPower);
		workingItem.setValue(Item.standardHealthValue);
	}
	
	private void showPoison() {
		workingItem.setType(2);
		workingItem.setDamage(Item.standardPoisonPower);
		workingItem.setValue(Item.standardPoisonValue);
	}
	
	private void showMana() {
		workingItem.setType(3);
		workingItem.setDamage(Item.standardManaPower);
		workingItem.setValue(Item.standardManaValue);
	}
	
	private void showSword(Moveable.Element element) {
		workingItem.setType(4);
		workingItem.setDamage(Item.standardSwordPower);
		workingItem.setValue(Item.standardSwordValue);
		workingItem.setElement(element);
	}
	
	private void showShield(Moveable.Element element) {
		workingItem.setType(5);
		workingItem.setDamage(Item.standardShieldPower);
		workingItem.setValue(Item.standardShieldValue);
		workingItem.setElement(element);
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
					showHealth();
				else if(itemList.getSelectedIndex() == 2)
					showPoison();
				else if(itemList.getSelectedIndex() == 3)
					showMana();
				
				//Schwerter
				else if(itemList.getSelectedIndex() == 4)
					showSword(Moveable.Element.PHYSICAL);
				else if(itemList.getSelectedIndex() == 5)
					showSword(Moveable.Element.ICE);
				else if(itemList.getSelectedIndex() == 6)
					showSword(Moveable.Element.FIRE);
				else if(itemList.getSelectedIndex() == 7)
					showSword(Moveable.Element.WATER);
				
				//Schilde
				else if(itemList.getSelectedIndex() == 8)
					showShield(Moveable.Element.PHYSICAL);
				else if(itemList.getSelectedIndex() == 9)
					showShield(Moveable.Element.ICE);
				else if(itemList.getSelectedIndex() == 10)
					showShield(Moveable.Element.FIRE);
				else if(itemList.getSelectedIndex() == 11)
					showShield(Moveable.Element.WATER);
	
				update();
				attributeTable.setVisible(true);
			}
		}
		
	}

}

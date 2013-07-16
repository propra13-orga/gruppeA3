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
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Moveable;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Klasse für den Monster-Dialog. Wird im Editor-Konstruktor
 * konstruiert und bei Bedarf mit Werten gefüllt.
 * @author christian
 *
 */
public class MonsterWindow extends JDialog implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	Monster workingMonster; //Arbeitskopie des Monsters
	Monster monsterToEdit; //Monster, das geändert werden soll
	Field affectedField; //Feld, auf dem sich das Monster befindet
	RoomTab roomTab; //Raum-Tab, auf dessen Raum sich das Monster befindet
	
	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	String type, size, strength, health, armor, speed, coinVal;
	boolean isBoss;
	JList<JPanel> monsterList;
	JTable attributeTable;
	String[][] tableContent;
	DefaultTableModel tableModel;
	
	//Listeneinträge
	JPanel emptyPanel, scorpPanel, snakePanel, zombiePanel, spiderPanel, bossPanel;
	
	public MonsterWindow() {
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
		monsterList = new JList<JPanel>();
		monsterList.setCellRenderer(new ListRenderer());
		monsterList.addListSelectionListener(this);
		monsterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		monsterList.setLayoutOrientation(JList.VERTICAL);
		monsterList.setFixedCellHeight(38);
		
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
		monsterList.setListData(panels);
		JScrollPane monsterListPane = new JScrollPane(monsterList);
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
		affectedField = field;
		
		//workingMonster und monsterToEdit setzen
		monsterToEdit = monster;//TODO Monster werden nicht abgebildet
		if(monsterToEdit == null)
			workingMonster = new Monster(roomTab.room.ID, 1.0, 1, 0, 10, 
					new Position(field.pos.x*32+Hitbox.standard.width/2,
					field.pos.y*32+Hitbox.standard.height/2),
					"unset", 10, 10, 10, false, Moveable.Element.PHYSICAL);
		else
			workingMonster = monster;
		
		//Auswahl in Liste setzen
		monsterList.setSelectedIndex(workingMonster.getType());
		
		//Fenster-Setup
		MenuStart.centerWindow(this);
		update();
		setVisible(true);
	}
	
	/**
	 * Updatet die Monster-Eigenschaften-Tabelle.
	 */
	private void update() {
		if(workingMonster.getType() != 0)
			attributeTable.setVisible(true);
		switch(workingMonster.getType()) {
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
		if(workingMonster.getType() == 0 && monsterToEdit != null)
			workingMonster.getRoom().entities.remove(monsterToEdit);
		
		//Ein bestehendes Monster wird geändert
		else if(workingMonster.getType() != 0 && monsterToEdit != null)
			monsterToEdit.edit(workingMonster);
		
		//Ein neues Monster wird hinzugefügt
		else if(workingMonster.getType() != 0 && monsterToEdit == null)
			workingMonster.getRoom().entities.add(workingMonster);
		
		monsterToEdit = workingMonster = null;
		roomTab.clearHighlights();
		roomTab.repaint();
		setVisible(false);
	}
	
	/**
	 * Schließt den Monster-Editor, verwirft alle Änderungen.
	 * Wird aufgerufen, falls "Abbrechen" gedrückt wurde.
	 */
	private void cancel() {
		monsterToEdit = workingMonster = null;
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
		workingMonster.setType(1);
	}
	
	private void showSnake() {
		workingMonster.setType(2);
	}
	
	private void showZombie() {
		workingMonster.setType(3);
	}
	
	private void showSpider() {
		workingMonster.setType(4);
	}
	
	private void showBoss() {
		workingMonster.setType(5);
	}

	/**
	 * EventListener für die Auswahl in der Monsterliste
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(! e.getValueIsAdjusting()) { //Damit nicht zweimal pro Selection ausgeführt wird
				
			if(monsterList.getSelectedIndex() == 0) {
				workingMonster.setType(0);
				attributeTable.setVisible(false);
			}
			else {
				attributeTable.setVisible(true);
				if(monsterList.getSelectedIndex() == 1)
					showScorp();
				else if(monsterList.getSelectedIndex() == 2)
					showSnake();
				else if(monsterList.getSelectedIndex() == 3)
					showZombie();
				else if(monsterList.getSelectedIndex() == 4)
					showSpider();
				else if(monsterList.getSelectedIndex() == 5)
					showBoss();
	
				update();
				attributeTable.setVisible(true);
			}
		}
		
	}

}

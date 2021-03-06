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
	String type, size, strength, health, armor, speed, coinVal, element;
	boolean isBoss;
	JList<JPanel> monsterList;
	JTable attributeTable;
	String[][] tableContent;
	DefaultTableModel tableModel;
	
	//Listeneinträge
	JPanel emptyPanel, scorpPanel, snakePanel, zombiePanel, spiderPanel, 
			phyBossPanel, iceBossPanel, fireBossPanel, waterBossPanel;
	
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
		//Boss-Icons auf 32x32 verkleinern
		ImageIcon phyBossIcon = new ImageIcon(GameWindow.bossImgs_up[0].getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH));
		JLabel phyBossEntry = new JLabel("Boss-Monster", phyBossIcon, JLabel.LEFT);
		ImageIcon iceBossIcon = new ImageIcon(GameWindow.bossImgs_up[3].getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH));
		JLabel iceBossEntry = new JLabel("Eis-Boss", iceBossIcon, JLabel.LEFT);
		ImageIcon fireBossIcon = new ImageIcon(GameWindow.bossImgs_up[1].getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH));
		JLabel fireBossEntry = new JLabel("Feuer-Boss", fireBossIcon, JLabel.LEFT);
		ImageIcon waterBossIcon = new ImageIcon(GameWindow.bossImgs_up[2].getScaledInstance(32,32, java.awt.Image.SCALE_SMOOTH));
		JLabel waterBossEntry = new JLabel("Wasser-Boss", waterBossIcon, JLabel.LEFT);
		
		//Zugehörige JPanels
		emptyPanel = new JPanel(new GridBagLayout());
		scorpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		snakePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zombiePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spiderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		phyBossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iceBossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fireBossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		waterBossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//Labels auf Panels klatschen
		GridBagConstraints emptyConstr = new GridBagConstraints();
		emptyConstr.fill = GridBagConstraints.HORIZONTAL;
		emptyPanel.add(emptyEntry, emptyConstr);
		scorpPanel.add(scorpEntry);
		snakePanel.add(snakeEntry);
		zombiePanel.add(zombieEntry);
		spiderPanel.add(spiderEntry);
		phyBossPanel.add(phyBossEntry);
		iceBossPanel.add(iceBossEntry);
		fireBossPanel.add(fireBossEntry);
		waterBossPanel.add(waterBossEntry);
		
		//Panels auf JList legen; Liste auf Scrollpane legen
		JPanel[] panels = {emptyPanel, scorpPanel, snakePanel, zombiePanel, spiderPanel,
				phyBossPanel, iceBossPanel, fireBossPanel, waterBossPanel};
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
				{"Münzen",coinVal},
				{"Element", element}
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
			tableContent[1][1] = "1";			//Größe
			tableContent[2][1] = "8";			//Stärke
			tableContent[3][1] = "8";			//Leben
			tableContent[4][1] = "1";			//Rüstung
			tableContent[5][1] = "1";			//Geschwindigkeit
			tableContent[6][1] = "10";			//Münzwert
			tableContent[7][1] = "Physisch";	//Element
			isBoss = false;
		case 2:
			tableContent[0][1] = "Schlange";	//Typ
			tableContent[1][1] = "1";			//Größe
			tableContent[2][1] = "6";			//Stärke
			tableContent[3][1] = "7";			//Leben
			tableContent[4][1] = "1";			//Rüstung
			tableContent[5][1] = "1";			//Geschwindigkeit
			tableContent[6][1] = "11";			//Münzwert
			tableContent[7][1] = "Physisch";	//Element
			isBoss = false;
			break;
		case 3:
			tableContent[0][1] = "Zombie";		//Typ
			tableContent[1][1] = "1";			//Größe
			tableContent[2][1] = "4";			//Stärke
			tableContent[3][1] = "11";			//Leben
			tableContent[4][1] = "1";			//Rüstung
			tableContent[5][1] = "1";			//Geschwindigkeit
			tableContent[6][1] = "9";			//Münzwert
			tableContent[7][1] = "Physisch";	//Element
			isBoss = false;
			break;
		case 4:
			tableContent[0][1] = "Spinne";		//Typ
			tableContent[1][1] = "1";			//Größe
			tableContent[2][1] = "6";			//Stärke
			tableContent[3][1] = "9";			//Leben
			tableContent[4][1] = "1";			//Rüstung
			tableContent[5][1] = "1";			//Geschwindigkeit
			tableContent[6][1] = "8";			//Münzwert
			tableContent[7][1] = "Physisch";	//Element
			isBoss = false;
			break;
		case 5:
			switch(workingMonster.getElement()) {
			case FIRE:
				tableContent[0][1] = "Feuer-Boss";	//Typ
				tableContent[1][1] = "1,5";			//Größe
				tableContent[2][1] = "17";			//Stärke
				tableContent[3][1] = "22";			//Leben
				tableContent[4][1] = "3";			//Rüstung
				tableContent[5][1] = "1";			//Geschwindigkeit
				tableContent[6][1] = "27";			//Münzwert
				tableContent[7][1] = Moveable.getElementName(workingMonster.getElement());	//Element
				break;
			case ICE:
				tableContent[0][1] = "Eis-Boss";	//Typ
				tableContent[1][1] = "1,5";			//Größe
				tableContent[2][1] = "15";			//Stärke
				tableContent[3][1] = "18";			//Leben
				tableContent[4][1] = "2";			//Rüstung
				tableContent[5][1] = "0,8";			//Geschwindigkeit
				tableContent[6][1] = "23";			//Münzwert
				tableContent[7][1] = Moveable.getElementName(workingMonster.getElement());	//Element
				isBoss = true;
				break;
			case PHYSICAL:
				tableContent[0][1] = "Normaler Boss";//Typ
				tableContent[1][1] = "1,5";			//Größe
				tableContent[2][1] = "9";			//Stärke
				tableContent[3][1] = "13";			//Leben
				tableContent[4][1] = "2";			//Rüstung
				tableContent[5][1] = "1";			//Geschwindigkeit
				tableContent[6][1] = "15";			//Münzwert
				tableContent[7][1] = Moveable.getElementName(workingMonster.getElement());	//Element
				isBoss = true;
				break;
			case WATER:
				tableContent[0][1] = "Wasser-Boss";	//Typ
				tableContent[1][1] = "1,5";			//Größe
				tableContent[2][1] = "12";			//Stärke
				tableContent[3][1] = "15";			//Leben
				tableContent[4][1] = "2";			//Rüstung
				tableContent[5][1] = "1,5";			//Geschwindigkeit
				tableContent[6][1] = "20";			//Münzwert
				tableContent[7][1] = Moveable.getElementName(workingMonster.getElement());	//Element
				break;
			default:
				break;
			
			}
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
		workingMonster.setElement(Moveable.Element.PHYSICAL);
		workingMonster.setArmour(1);
		workingMonster.setAttack(8);
		workingMonster.setHealth(8);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(8);
		workingMonster.isBoss = false;
	}
	
	private void showSnake() {
		workingMonster.setType(2);
		workingMonster.setElement(Moveable.Element.PHYSICAL);
		workingMonster.setArmour(1);
		workingMonster.setAttack(6);
		workingMonster.setHealth(7);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(7);
		workingMonster.isBoss = false;
	}
	
	private void showZombie() {
		workingMonster.setType(3);
		workingMonster.setElement(Moveable.Element.PHYSICAL);
		workingMonster.setArmour(1);
		workingMonster.setAttack(4);
		workingMonster.setHealth(11);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(9);
		workingMonster.isBoss = false;
	}
	
	private void showSpider() {
		workingMonster.setType(4);
		workingMonster.setElement(Moveable.Element.PHYSICAL);
		workingMonster.setArmour(1);
		workingMonster.setAttack(6);
		workingMonster.setHealth(9);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(8);
		workingMonster.isBoss = false;
	}
	
	private void showPhyBoss() {
		workingMonster.setType(5);
		workingMonster.setElement(Moveable.Element.PHYSICAL);
		workingMonster.setArmour(2);
		workingMonster.setAttack(9);
		workingMonster.setHealth(813);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(15);
		workingMonster.isBoss = true;
	}
	
	private void showIceBoss() {
		workingMonster.setType(5);
		workingMonster.setElement(Moveable.Element.ICE);
		workingMonster.setArmour(2);
		workingMonster.setAttack(15);
		workingMonster.setHealth(18);
		workingMonster.setSpeed(0.8);
		workingMonster.getCoin().setValue(23);
		workingMonster.isBoss = true;
	}

	
	private void showFireBoss() {
		workingMonster.setType(5);
		workingMonster.setElement(Moveable.Element.FIRE);
		workingMonster.setArmour(3);
		workingMonster.setAttack(17);
		workingMonster.setHealth(22);
		workingMonster.setSpeed(1.0);
		workingMonster.getCoin().setValue(27);
		workingMonster.isBoss = true;
	}

	
	private void showWaterBoss() {
		workingMonster.setType(5);
		workingMonster.setElement(Moveable.Element.WATER);
		workingMonster.setArmour(2);
		workingMonster.setAttack(12);
		workingMonster.setHealth(15);
		workingMonster.setSpeed(1.6);
		workingMonster.getCoin().setValue(20);
		workingMonster.isBoss = true;
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
					showPhyBoss();
				else if(monsterList.getSelectedIndex() == 6)
					showIceBoss();
				else if(monsterList.getSelectedIndex() == 7)
					showFireBoss();
				else if(monsterList.getSelectedIndex() == 8)
					showWaterBoss();
	
				update();
				attributeTable.setVisible(true);
			}
		}
		
	}

}

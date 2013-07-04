package com.github.propra13.gruppeA3.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Map.Checkpoint;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Trigger;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Klasse für den Trigger-Edit-Dialog. Wird im Editor-Konstruktor
 * konstruiert und bei Bedarf mit Werten befüllt und dargestellt.
 * @author christian
 *
 */
public class TriggerWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	protected Trigger workingTrigger; //Arbeitskopie des Triggers
	private Trigger triggerToEdit; //Trigger, der bearbeitet wird
	private Field affectedField; //Zwischenspeicher für Auswahlklick-Bestätigung
	
	/**Konstante für Checkpointtrigger*/
	public final static int CHECKPOINT = 1;
	
	//Fensterkram
	private final static int MINWIDTH = 300;
	private final static int MINHEIGHT = 250;
	
	private JLabel checkpoint, triggerPos, target, targetPos;
	private JButton bChangeTrigger, bChangeTarget, bCancel, bDone, bDelete;
	
	
	public TriggerWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("unset");
		setResizable(false);
		
		//Buttons und Labels positionieren
		JLabel typeDesc = new JLabel("Triggertyp: ");
		typeDesc.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints typeDescConstraints = new GridBagConstraints();
		typeDescConstraints.gridx = 0;
		typeDescConstraints.gridy = 0;
		typeDescConstraints.gridheight = typeDescConstraints.gridwidth = 1;
		typeDescConstraints.fill = GridBagConstraints.HORIZONTAL;
		typeDescConstraints.weightx = typeDescConstraints.weighty = 1;
		typeDescConstraints.insets = new Insets(2,10,2,4);
		add(typeDesc, typeDescConstraints);
		
		checkpoint = new JLabel("Checkpoint");
		GridBagConstraints checkpointConstraints = new GridBagConstraints();
		checkpointConstraints.gridx = 1;
		checkpointConstraints.gridy = 0;
		checkpointConstraints.gridheight = checkpointConstraints.gridwidth = 1;
		checkpointConstraints.fill = GridBagConstraints.HORIZONTAL;
		checkpointConstraints.weightx = checkpointConstraints.weighty = 1;
		checkpointConstraints.insets = new Insets(2,8,2,4);
		add(checkpoint, checkpointConstraints);
		
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
		
		bDelete = new JButton("Trigger löschen");
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

		//Linker Teil (Trigger-Position)
		// Rahmen
		JPanel panelTrigger = new JPanel();
		GridBagLayout layoutA = new GridBagLayout();
		panelTrigger.setLayout(layoutA);
		TitledBorder borderA; //Rahmen
		borderA = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Schalterfeld");
		borderA.setTitleJustification(TitledBorder.CENTER);
		panelTrigger.setBorder(borderA);
		
		GridBagConstraints constraintsA = new GridBagConstraints();
		constraintsA.gridx = 0;
		constraintsA.gridy = 1;
		constraintsA.gridwidth = 1;
		constraintsA.gridheight = 1;
		constraintsA.weighty = 5;
		constraintsA.weightx = 1;
		constraintsA.fill = GridBagConstraints.BOTH;
		constraintsA.insets = new Insets(10,0,0,0);
		add(panelTrigger, constraintsA);
		
		
		// Inhalt
		JLabel triggerField = new JLabel("Position");
		triggerField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints triggerFieldConstraints = new GridBagConstraints();
		triggerFieldConstraints.gridx = 0;
		triggerFieldConstraints.gridy = 0;
		triggerFieldConstraints.gridheight = triggerFieldConstraints.gridwidth = 1;
		triggerFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		triggerFieldConstraints.weightx = triggerFieldConstraints.weighty = 1;
		triggerFieldConstraints.insets = new Insets(10,10,2,4);
		panelTrigger.add(triggerField, triggerFieldConstraints);
		
		triggerPos = new JLabel();
		triggerPos.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints triggerPosConstraints = new GridBagConstraints();
		triggerPosConstraints.gridx = 0;
		triggerPosConstraints.gridy = 1;
		triggerPosConstraints.gridheight = triggerPosConstraints.gridwidth = 1;
		triggerPosConstraints.fill = GridBagConstraints.HORIZONTAL;
		triggerPosConstraints.weightx = triggerPosConstraints.weighty = 1;
		triggerPosConstraints.insets = new Insets(10,8,2,4);
		panelTrigger.add(triggerPos, triggerPosConstraints);
		
		bChangeTrigger = new JButton("Ändern");
		bChangeTrigger.addActionListener(this);
		GridBagConstraints changeAConstraints = new GridBagConstraints();
		changeAConstraints.gridx = 0;
		changeAConstraints.gridy = 2;
		changeAConstraints.gridheight = 1;
		changeAConstraints.gridwidth = 2;
		changeAConstraints.fill = GridBagConstraints.HORIZONTAL;
		changeAConstraints.weightx = changeAConstraints.weighty = 1;
		changeAConstraints.insets = new Insets(2,4,2,4);
		panelTrigger.add(bChangeTrigger, changeAConstraints);
		
		
		//Rechter Teil
		// Rahmen
		JPanel panelTarget = new JPanel();
		GridBagLayout layoutB = new GridBagLayout();
		panelTarget.setLayout(layoutB);
		TitledBorder borderB; //Rahmen
		borderB = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Zielfeld");
		borderB.setTitleJustification(TitledBorder.CENTER);
		panelTarget.setBorder(borderB);
		
		GridBagConstraints constraintsB = new GridBagConstraints();
		constraintsB.gridx = 1;
		constraintsB.gridy = 1;
		constraintsB.gridwidth = 1;
		constraintsB.gridheight = 1;
		constraintsB.weighty = 5;
		constraintsB.weightx = 1;
		constraintsB.fill = GridBagConstraints.BOTH;
		constraintsB.insets = new Insets(10,0,0,0);
		add(panelTarget, constraintsB);
		

		// Inhalt
		target = new JLabel();
		target.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints targetNameConstraints = new GridBagConstraints();
		targetNameConstraints.gridx = 0;
		targetNameConstraints.gridy = 0;
		targetNameConstraints.gridheight = targetNameConstraints.gridwidth = 1;
		targetNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		targetNameConstraints.weightx = targetNameConstraints.weighty = 1;
		targetNameConstraints.insets = new Insets(10,10,2,4);
		panelTarget.add(target, targetNameConstraints);
		
		targetPos = new JLabel();
		targetPos.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints targetPosConstraints = new GridBagConstraints();
		targetPosConstraints.gridx = 0;
		targetPosConstraints.gridy = 1;
		targetPosConstraints.gridheight = targetPosConstraints.gridwidth = 1;
		targetPosConstraints.fill = GridBagConstraints.HORIZONTAL;
		targetPosConstraints.weightx = targetPosConstraints.weighty = 1;
		targetPosConstraints.insets = new Insets(10,8,2,4);
		panelTarget.add(targetPos, targetPosConstraints);
		
		bChangeTarget = new JButton("Ändern");
		bChangeTarget.addActionListener(this);
		GridBagConstraints changeBConstraints = new GridBagConstraints();
		changeBConstraints.gridx = 0;
		changeBConstraints.gridy = 2;
		changeBConstraints.gridheight = 1;
		changeBConstraints.gridwidth = 2;
		changeBConstraints.fill = GridBagConstraints.HORIZONTAL;
		changeBConstraints.weightx = changeBConstraints.weighty = 1;
		changeBConstraints.insets = new Insets(2,4,2,4);
		panelTarget.add(bChangeTarget, changeBConstraints);

		
		panelTrigger.setVisible(true);
		panelTarget.setVisible(true);
	}
	
	/**
	 * Zeigt das Trigger-Edit-Fenster mit einem neu erstellten Trigger
	 * @param field Feld, auf dem der neue Trigger aufgebaut werden soll
	 * @param triggerType Typ des neuen Triggers (siehe Konstanten)
	 */
	public void showWindow(Field field, int triggerType) {
		
		switch(triggerType) {
		case CHECKPOINT:
			setTitle("Checkpoint-Editor");
			workingTrigger = new Checkpoint(field, null);
			checkpoint.setVisible(true);
			break;
		default:
			try {
				throw new Exception("Unbekannter Triggertyp: "+triggerType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		
		update();
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/**
	 * Zeigt das Trigger-Editor-Fenster mit dem angegebenen Trigger
	 * @param link Trigger, der bearbeitet werden soll
	 */
	public void showWindow(Trigger trigger) {
		if(trigger instanceof Checkpoint)
			setTitle("Checkpoint-Editor");
		triggerToEdit = trigger;
		this.workingTrigger = trigger.clone();
		update();
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/**
	 * Zeigt Trigger-Editor erneut an, zB nach einem Auswahlklick
	 */
	public void showWindow() {
		update();
		setVisible(true);
	}
	
	/**
	 * Liest Fensterinhalt neu aus Trigger ein
	 */
	private void update() {

		triggerPos.setText(workingTrigger.getField().pos.toString());
		if(workingTrigger instanceof Checkpoint) {
			Checkpoint checkp = (Checkpoint)workingTrigger;
			checkpoint.setVisible(true);
			
			//Checkpoint-Trigger-Target noch nicht gesetzt
			if(checkp.getToActivate() == null) {
				target.setText("Nicht gesetzt");
				targetPos.setText("");
			}
			
			else {
				target.setText("Link "+checkp.getToActivate().ID);
				
				//Link-Position, falls in diesem Raum
				Link cpLink = checkp.getToActivate();
				if(cpLink.targetRooms[0] == workingTrigger.getField().getRoom())
					targetPos.setText(cpLink.targetFields[0].pos.toString());
				
				else if(cpLink.targetRooms[1] == workingTrigger.getField().getRoom())
					targetPos.setText(cpLink.targetFields[1].pos.toString());
				
				//Ansonsten liegt der Link nicht in diesem Raum
				else
					targetPos.setText("<html><body>verbindet die Räume<br>" + cpLink.targetRooms[0].ID +
							" und " + cpLink.targetRooms[1].ID);
			}
		}
	}
	
	/**
	 * Wird aufgerufen, wenn ein Auswahlklick für das Trigger-Ziel getätigt wurde.
	 * @param affectedField Feld, das ausgewählt wurde.
	 */
	protected void chooseClickTarget(Field affectedField) {
		this.affectedField = affectedField;
		Editor.editor.warning.showWindow(WarningWindow.Type.TRIGGERTARGETCHOSEN);
	}
	
	/**
	 * Wird aufgerufen, wenn ein Auswahlklick für das Trigger-Feld getätigt wurde.
	 * @param affectedField Feld, das ausgewählt wurde.
	 */
	protected void chooseClickTrigger(Field affectedField) {
		this.affectedField = affectedField;
		Editor.editor.warning.showWindow(WarningWindow.Type.TRIGGERCHOSEN);
	}
	
	/*
	 * Button-Aktionen
	 */
	
	/**
	 * Löst einen Auswahlklick aus, der es dem Nutzer gestattet, das Feld des Triggers neu zu wählen.
	 * Wird aufgerufen, falls "Ändern" im Trigger-Teil gedrückt wurde.
	 */
	private void changeTrigger() {
		if(workingTrigger instanceof Checkpoint)
			Editor.editor.chooseClick = Editor.ChooseClickType.CHECKPOINTFIELD;
		
		setVisible(false);
		
	}
	
	/**
	 * Löst einen Auswahlklick aus, der es dem Nutzer gestattet, das Zielfeld des Triggers neu zu wählen.
	 * Wird aufgerufen, falls "Ändern" im Ziel-Teil gedrückt wurde.
	 */
	private void changeTarget() {
		if(workingTrigger instanceof Checkpoint)
			Editor.editor.chooseClick = Editor.ChooseClickType.CHECKPOINTLINK;
		
		setVisible(false);
		
	}
	
	/**
	 * Übernimmt die Änderungen und schließt den Trigger-Editor.
	 * Wird aufgerufen, falls "Fertig" gedrückt wurde.
	 */
	private void save() {
		triggerToEdit.edit(workingTrigger);
		setVisible(false);
	}
	
	/**
	 * Schließt den Trigger-Editor, verwirft alle Änderungen.
	 * Wird aufgerufen, falls "Abbrechen" gedrückt wurde.
	 */
	private void cancel() {
		triggerToEdit = null;
		workingTrigger = null;
		setVisible(false);
	}
	
	/**
	 * Löscht den Trigger.
	 * Wird aufgerufen, falls "Trigger löschen" gedrückt wurde.
	 */
	private void delete() {
		triggerToEdit.getField().trigger = null;
		workingTrigger = null;
		triggerToEdit = null;
		setVisible(false);
	}
	
	/**
	 * Wird aufgerufen, wenn die Trigger-Feld-Auswahl bestätigt wurde.
	 */
	protected void chooseTrigger() {
		System.out.println("Call: chooseTrigger()");
		workingTrigger.setField(affectedField);
		
		Editor.editor.chooseClick = Editor.ChooseClickType.NONE;
		affectedField = null;
		showWindow();
	}
	
	/**
	 * Wird aufgerufen, wenn die Trigger-Zielfeld-Auswahl bestätigt wurde.
	 */
	protected void chooseTarget() {
		System.out.println("Triggertarget gewählt!");
		if(workingTrigger instanceof Checkpoint) {
			Checkpoint cp = (Checkpoint)workingTrigger;
			cp.setToActivate(affectedField.link);
		}
		
		Editor.editor.chooseClick = Editor.ChooseClickType.NONE;
		affectedField = null;
		showWindow();
	}

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bChangeTrigger)
			changeTrigger();
		else if(e.getSource() == bChangeTarget)
			changeTarget();
		else if(e.getSource() == bCancel)
			cancel();
		else if(e.getSource() == bDone)
			save();
		else if(e.getSource() == bDelete)
			delete();
		
	}

}

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
	
	private Trigger workingTrigger; //Arbeitskopie des Triggers
	private Trigger triggerToEdit; //Trigger, der bearbeitet wird
	
	/**Konstante für Checkpointtrigger*/
	public final static int CHECKPOINT = 1;
	
	//Fensterkram
	private final static int MINWIDTH = 150;
	private final static int MINHEIGHT = 300;
	
	private JLabel type, checkpoint, triggerPos, targetPos;
	private JButton bChangeTrigger, bChangeTarget;
	
	
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
		
		checkpoint = new JLabel("Checkpoint-Trigger");
		GridBagConstraints checkpointConstraints = new GridBagConstraints();
		checkpointConstraints.gridx = 1;
		checkpointConstraints.gridy = 0;
		checkpointConstraints.gridheight = checkpointConstraints.gridwidth = 1;
		checkpointConstraints.fill = GridBagConstraints.HORIZONTAL;
		checkpointConstraints.weightx = checkpointConstraints.weighty = 1;
		checkpointConstraints.insets = new Insets(2,8,2,4);
		add(checkpoint, checkpointConstraints);
		
		//Linker Teil (Trigger-Position)
		// Rahmen
		JPanel panelTrigger = new JPanel();
		GridBagLayout layoutA = new GridBagLayout();
		panelTrigger.setLayout(layoutA);
		TitledBorder borderA;
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
		triggerField.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints triggerFieldConstraints = new GridBagConstraints();
		triggerFieldConstraints.gridx = 0;
		triggerFieldConstraints.gridy = 0;
		triggerFieldConstraints.gridheight = triggerFieldConstraints.gridwidth = 1;
		triggerFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		triggerFieldConstraints.weightx = triggerFieldConstraints.weighty = 1;
		triggerFieldConstraints.insets = new Insets(10,10,2,4);
		panelTrigger.add(triggerField, triggerFieldConstraints);
		
		triggerPos = new JLabel();
		triggerPos.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints triggerPosConstraints = new GridBagConstraints();
		triggerPosConstraints.gridx = 1;
		triggerPosConstraints.gridy = 0;
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
		TitledBorder borderB;
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
		JLabel targetField = new JLabel("Position");
		targetField.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints roomBConstraints = new GridBagConstraints();
		roomBConstraints.gridx = 0;
		roomBConstraints.gridy = 0;
		roomBConstraints.gridheight = roomBConstraints.gridwidth = 1;
		roomBConstraints.fill = GridBagConstraints.HORIZONTAL;
		roomBConstraints.weightx = roomBConstraints.weighty = 1;
		roomBConstraints.insets = new Insets(10,10,2,4);
		panelTarget.add(targetField, roomBConstraints);
		
		targetPos = new JLabel();
		targetPos.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints targetPosConstraints = new GridBagConstraints();
		targetPosConstraints.gridx = 1;
		targetPosConstraints.gridy = 0;
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
			workingTrigger = new Checkpoint(field, null);
			checkpoint.setVisible(true);
		default:
			try {
				throw new Exception("Unbekannter Triggertyp: "+triggerType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		update();
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/**
	 * Zeigt das Link-Editor-Fenster mit dem angegebenen Link
	 * @param link Link, der bearbeitet werden soll
	 */
	public void showWindow(Trigger trigger) {
		triggerToEdit = trigger;
		this.workingTrigger = trigger.clone();
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
	 * Liest Fensterinhalt neu aus Trigger ein
	 */
	private void update() {

		triggerPos.setText(workingTrigger.getField().pos.toString());
		if(workingTrigger instanceof Checkpoint) {
			Checkpoint checkp = (Checkpoint)workingTrigger;
			checkpoint.setVisible(true);
			if(checkp.getToActivate() == null)
				targetPos.setText("Nicht gesetzt");
			else
				targetPos.setText("Link "+checkp.getToActivate().ID);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

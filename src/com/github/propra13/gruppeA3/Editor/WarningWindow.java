package com.github.propra13.gruppeA3.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * JDialog für jegliche Warnungen / Fehler, die auftreten
 * @author christian
 */
public class WarningWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public enum Type{ LINKEXISTS, LINKPOS, LINKCHOSEN, TRIGGERCHOSEN, TRIGGERTARGETCHOSEN,
		HASNOLINK, OVERWRITE, NONE}
	private Type type;
	
	final public static int MINWIDTH = 400;
	final public static int MINHEIGHT = 150;
	
	JLabel infotext = new JLabel();
	JButton bOk = new JButton("Ok");
	JButton bCancel = new JButton("Auswahl abbrechen");

	public WarningWindow() {
        setTitle("Meldung");
        setSize(MINWIDTH,MINHEIGHT);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        
        // Ausrichten: Zentrieren über Hauptfenster
        Point framePos = Game.frame.getLocationOnScreen();
     	Point thisPos = new Point();
     	thisPos.x = framePos.x - (Game.MINWIDTH/2) + MINWIDTH/2;
		thisPos.y = framePos.y - (Game.MINHEIGHT/2) + MINHEIGHT/2;
     	setLocation(thisPos);
        
        //Fensterinhalt-Layout
        bOk.addActionListener(this);
		GridBagConstraints okConstraints = new GridBagConstraints();
		okConstraints.gridx = 0;
		okConstraints.gridy = 1;
		okConstraints.gridheight = okConstraints.gridwidth = 1;
		okConstraints.fill = GridBagConstraints.HORIZONTAL;
		okConstraints.weightx = okConstraints.weighty = 1;
		okConstraints.insets = new Insets(2,4,2,4);
		add(bOk, okConstraints);
		
		bCancel.addActionListener(this);
		GridBagConstraints auswAbbrConstraints = new GridBagConstraints();
		auswAbbrConstraints.gridx = 1;
		auswAbbrConstraints.gridy = 1;
		auswAbbrConstraints.gridheight = auswAbbrConstraints.gridwidth = 1;
		auswAbbrConstraints.fill = GridBagConstraints.HORIZONTAL;
		auswAbbrConstraints.weightx = auswAbbrConstraints.weighty = 1;
		auswAbbrConstraints.insets = new Insets(1,4,2,4);
		add(bCancel, auswAbbrConstraints);
		
		infotext.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints infotextConstraints = new GridBagConstraints();
		infotextConstraints.gridx = 0;
		infotextConstraints.gridy = 0;
		infotextConstraints.gridheight = 1;
		infotextConstraints.gridwidth = 2;
		infotextConstraints.fill = GridBagConstraints.HORIZONTAL;
		infotextConstraints.weightx = infotextConstraints.weighty = 1;
		infotextConstraints.insets = new Insets(15,10,2,4);
		add(infotext, infotextConstraints);
	}
	
	/**
	 * Zeigt das Warnung Fenster mit einer angegebenen Warnung.
	 * @param type Warning-Typ, den das Warnung-Fenster ausgibt.
	 */
	public void showWindow(Type type) {

		MenuStart.centerWindow(this);
		bCancel.setVisible(true);
		this.type = type;
		
		switch(type) {
		case LINKEXISTS:
			infotext.setText("<html><body>Entschuldige, aber auf diesem Feld<br> befindet sich bereits ein Link.</body></html>");
			bOk.setText("Ok");
			bCancel.setText("Auswahl abbrechen");
			break;
		case LINKPOS:
			infotext.setText("<html><body>Entschuldige, aber Links dürfen nur<br> am Rand des Raums platziert werden.</body></html>");
			bOk.setText("Ok");
			bCancel.setText("Auswahl abbrechen");
			break;
		case LINKCHOSEN:
			infotext.setText("Möchtest du den Link auf das markierte Feld setzen?");
			bOk.setText("Ja");
			bCancel.setText("Nein");
			break;
		case TRIGGERCHOSEN:
			infotext.setText("<html><body>Möchtest du den Trigger auf das<br> markierte Feld setzen?</body></html>");
			bOk.setText("Ja");
			bCancel.setText("Nein");
			break;
		case TRIGGERTARGETCHOSEN:
			infotext.setText("<html><body>Möchtest du das Zielfeld für den Trigger<br> auf das markierte Feld setzen?</body></html>");
			bOk.setText("Ja");
			bCancel.setText("Nein");
			break;
		case HASNOLINK:
			infotext.setText("<html><body>Das ausgewählte Feld hat keinen Link,<br> der getriggert werden könnte.</body></html>");
			bOk.setText("Ok");
			bCancel.setText("Auswahl abbrechen");
			break;
		case OVERWRITE:
			infotext.setText("Möchtest du die Karte überschreiben?");
			bOk.setText("Ja");
			bCancel.setText("Nein");
			break;
		case NONE:
			infotext.setText("Dieser Dialog sollte eigentlich nicht auftauchen.");
			bOk.setText("Ok");
			bCancel.setText("Ich widerspreche!");
			break;
		}
		
		setVisible(true);
	}
	
	public void showWindow(String msg) {
		MenuStart.centerWindow(this);
		infotext.setText(msg);
		bOk.setText("Ok");
		bCancel.setVisible(false);
		type = Type.NONE;
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Ok / Ja / ...
		if(e.getSource() == bOk) {
			
			//Den jeweiligen Editor benachrichtigen.
			if(type == Type.LINKCHOSEN)
				Editor.editor.linkEditor.chooseField();
			else if(type == Type.TRIGGERCHOSEN)
				Editor.editor.triggerEditor.chooseTrigger();
			else if(type == Type.TRIGGERTARGETCHOSEN)
				Editor.editor.triggerEditor.chooseTarget();
			else if(type == Type.OVERWRITE)
				Editor.editor.write();
		}
		
		//Abbrechen / Auswahl abbrechen / ...
		else if(e.getSource() == bCancel) {
			
			//Falls der Meldungstyp eine Fehlermeldung ist, "Auswahl abbrechen" durchführen
			if(type == Type.LINKEXISTS || type == Type.LINKPOS || type == Type.HASNOLINK) {
				Editor.editor.chooseClick = Editor.ChooseClickType.NONE;
				Editor.editor.linkEditor.showWindow();
			}
		}

		setVisible(false);
		//type = Type.NONE;
	}
	
	
}

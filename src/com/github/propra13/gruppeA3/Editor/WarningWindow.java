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

/**
 * JDialog für jegliche Warnungen / Fehler, die auftreten
 * @author christian
 */
public class WarningWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public enum Type{ LINKEXISTS, LINKPOS, LINKCHOSEN }
	private Type type;
	
	final public static int MINWIDTH = 400;
	final public static int MINHEIGHT = 200;
	
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
			infotext.setText("<html><body>Möchtest du den Link auf das markierte Feld setzen?");
			bOk.setText("Ja");
			bCancel.setText("Nein");
			break;
		}
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Ok / Ja / ...
		if(e.getSource() == bOk) {
			if(type == Type.LINKEXISTS || type == Type.LINKPOS)
				setVisible(false);
			else if(type == Type.LINKCHOSEN)
				Editor.editor.linkEditor.chooseField();
		}
		
		//Abbrechen / Auswahl abbrechen / ...
		else if(e.getSource() == bCancel) {
			setVisible(false);
			if(type == Type.LINKEXISTS || type == Type.LINKPOS) {
				Editor.editor.chooseClick = Editor.ChooseClickType.NONE;
				Editor.editor.linkEditor.showWindow();
			}
		}
	}
}

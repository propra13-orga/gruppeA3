package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;

/**
 * JPanel für die Optionen
 * @author christian
 *
 */
public class MainOptions extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JButton bToggleSound = new JButton();
	private JButton bBack = new JButton("Zurück");
	
	public MainOptions() {
		super();
		
		setLayout(null);
		
		int buttonPosX = Game.MINWIDTH/2-100;
		int buttonPosY = Game.MINHEIGHT/2-100;
		
		bToggleSound.setBounds(buttonPosX, buttonPosY, 200, 30);
		bBack.setBounds(buttonPosX, buttonPosY+40, 200, 30);
		
		add(bToggleSound);
		add(bBack);
		
		bToggleSound.addActionListener(this);
		bBack.addActionListener(this);
		
		bBack.setVisible(true);
		bToggleSound.setVisible(true);
		
		validate();
		updateSoundButton();
		setVisible(true);
	}
	
	/**
	 * Macht die Musik an und aus.
	 * Wird ausgeführt, wenn der Musik-Knopf gedrückt wurde.
	 */
	private void toggleSound() {
		if(Game.Menu.music.isRunning()) {
			Game.Menu.music.stop();
		}
		else
			Game.Menu.music.play();
		
		updateSoundButton();
	}
	
	/**
	 * Passt den Text des Sound-Buttons an, je nach Zustand der Musik
	 */
	private void updateSoundButton() {
		if(Game.Menu.music.isRunning()) {
			bToggleSound.setText("Musik aus");
		}
		else
			bToggleSound.setText("Musik an");
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bToggleSound)
			toggleSound();
		
		//Zurück-Knopf
		else if(e.getSource() == bBack) {
			setVisible(false);
			Game.Menu.remove(this);
			Game.Menu.setVisible(true);
		}
		
	}
	
	/**
	 * Malt das Hintergrundbild.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(GameWindow.background, 0, 0, this);
	}
	
	/**
	 * Malt den Titel.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//"Optionen"-Titel
		Font small = new Font("Arial", Font.BOLD, 20);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString("Optionen", (Game.MINWIDTH - metr.stringWidth("Optionen"))/2, 80);
	}
}

package com.github.propra13.gruppeA3.Menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.XMLParser.CrawlerSAX;

@SuppressWarnings("serial")
public class MenuStart extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	
	public static void main(String[] args) {
		try {
			MenuOption dialog = new MenuOption();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 */
	/**
	 * Create the dialog.
	 */
	public MenuStart() {

		setBounds(100, 100,CrawlerSAX.map.length*32,CrawlerSAX.map[0].length*32);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}

}

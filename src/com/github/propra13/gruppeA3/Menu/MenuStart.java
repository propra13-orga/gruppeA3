package com.github.propra13.gruppeA3.Menu;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.github.propra13.gruppeA3.Entities.Walls;
import com.github.propra13.gruppeA3.XMLParser.CrawlerSAX;
import java.awt.*;

@SuppressWarnings("serial")
public class MenuStart extends JDialog {

	//private final JPanel contentPanel = new JPanel();

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
		//setBounds(100, 100,CrawlerSAX.map.length*32,CrawlerSAX.map[0].length*32);
		/*getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}*/
	}
	
	public void paint(Graphics g) {
		//super.paint(g);
		ImageIcon tempImage;
		// Array von Bildern aller Wandtypen
		Image[] walls = new Image[5];
		for (int x = 0; x < 5 ; x++) {
			tempImage = new ImageIcon("data/images/wall_"+x+"_32.png");
			walls[x] = tempImage.getImage();
		}
		
		for (int i = 0; i < CrawlerSAX.map.length; i++){
			for(int j = 0; j < CrawlerSAX.map[0].length; j++) {
				if (CrawlerSAX.map[i][j] instanceof Walls) {
					Walls wall = (Walls)CrawlerSAX.map[i][j];
					// wall.getType-1 liefert die Position des geladenen Bildes im Array
					// somit ist walls[wall.getType()-1] das zu landende Bild.
					g.drawImage(walls[wall.getType()-1],i*32,j*32,this);
					
					System.out.print("W ");
				}
				else System.out.print("  ");
			}
			System.out.println();
		}
	}

}

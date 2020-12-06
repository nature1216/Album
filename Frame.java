package project;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Frame extends JFrame {
	JTextField path_field;
	public Frame(Image im) { //경로로 불러올 때
		setTitle("file path");
		
		
		JPanel newWindowContainer = new JPanel();
		setContentPane(newWindowContainer);
		JLabel ShowImage = new JLabel();
		ShowImage.setIcon(new ImageIcon(im));
		newWindowContainer.add(ShowImage);
		pack();
		
		setSize(900,800);
		setVisible(true);
	}
	public Frame(String p) {	//파일탐색기로 불러올 때
		setTitle("file path");
		
		
		JPanel newWindowContainer = new JPanel();
		setContentPane(newWindowContainer);
		JLabel ShowImage = new JLabel();
		ShowImage.setIcon(new ImageIcon(p));
		newWindowContainer.add(ShowImage);
		pack();
		
		setSize(900,900);
		setVisible(true);
		
	}
}
package project;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Frame extends JFrame {
	JTextField path_field;
	public Frame(Image im) { //��η� �ҷ��� ��
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
	public Frame(String p) {	//����Ž����� �ҷ��� ��
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
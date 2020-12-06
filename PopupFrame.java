package project;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

class PopupFrame extends JFrame {
	JTextArea memoA;
	public PopupFrame(String name) throws IOException {
		JPanel PopupContent = new JPanel();
		setContentPane(PopupContent);
		JLabel ImageLabel = new JLabel();
		memoA = new JTextArea(5,40);
		JScrollPane memoS = new JScrollPane(memoA); //¸Þ¸ð panel

		memoS.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		memoS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		memoA.setEditable(false);
		String p ="C:\\JavaAlbum\\images\\" + name + ".jpg";
		String memop = "C:\\JavaAlbum\\memo\\" + name + ".txt";
		ImageLabel.setIcon(new ImageIcon(p));
		get_memo(name);
		
		
		
		PopupContent.add(ImageLabel, BorderLayout.CENTER);
		PopupContent.add(memoS,BorderLayout.WEST);
		pack();
		setSize(1200,800);
		setVisible(true);
		
	}

	void get_memo(String name) throws IOException {
		String path = "C:\\JavaAlbum\\memo\\" + name + ".txt";
		
		BufferedReader br = null;
		
		try {
			String text;
			File file = new File(path);
			br = new BufferedReader(new FileReader(file));
			
			while((text = br.readLine()) != null) {
				memoA.setText(text);
			}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(br != null) br.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
}


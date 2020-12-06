package project;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;




public class album extends JFrame {
	Container contentPane;
	JLabel imageLabel = new JLabel(); //사진
	ImageIcon images[] = new ImageIcon[100];
	JMenuItem file_ex, file_path; //파일 탐색기로 불러오기 메뉴, 파일 경로로 불러오기 메뉴
	JTextArea memoArea;
	JComboBox combo, sort;
	File[] files;
	ObjectOutputStream  oos;
	ObjectInputStream ois;
	
	String filenames[]; // 파일 이름 들어있는 배열
	String imagePath = "C:\\JavaAlbum\\images"; //이미지 저장 경로
	String memoPath = "C:\\JavaAlbum\\memo"; //메모저장 경로
	String oripath; //사진 불러오기 한 경로
	
	static int currentid=0; // 지금 보여주는 사진의 배열 인덱스
	int imagelength=0; //이미지파일 배열 길이
	int COMPARETYPE_NAME = 0;
	int COMPARETYPE_DATE = 1;
	int type = 1;
	
	public album() throws IOException
	{
		setTitle("앨범");
		
		contentPane = getContentPane();
		contentPane.add(imageLabel, BorderLayout.CENTER);
		contentPane.add(new MenuPanel(), BorderLayout.SOUTH);
		contentPane.add(new SearchPanel(), BorderLayout.EAST);
		
		createMenu();
		
		memoArea = new JTextArea(10,20);
		JScrollPane memoscroll = new JScrollPane(memoArea); //메모 panel
		memoscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		memoscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		memoArea.setEditable(true);
		get_memo(filenames[currentid]);
		contentPane.add(memoscroll,BorderLayout.WEST);
		
		pack();
		
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int keycode = e.getKeyCode();
				switch(keycode) {
				case KeyEvent.VK_LEFT:
				{
					ReadImage(type);
					currentid--;
					currentid += imagelength;
					currentid %= imagelength;
					memoArea.setText("");
					combo.setSelectedItem(filenames[currentid]);
					try {
						get_memo(filenames[currentid]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					imageLabel.setIcon(images[currentid]);
					break;
				}
				case KeyEvent.VK_RIGHT:
				{
					ReadImage(type);
					
					currentid++;
					currentid %= imagelength;
					combo.setSelectedItem(filenames[currentid]);
					try {
						get_memo(filenames[currentid]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					imageLabel.setIcon(images[currentid]);
					break;
				}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) { }
		});
		this.requestFocus();
		setFocusable(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	
	
	void SortBy(int s) {
		files = new File(imagePath).listFiles();
		
		switch(s) {
		case 0:
		{
			type = 0;
			files = sortFileList(files, COMPARETYPE_DATE);
			for(File file : files) {
				combo.updateUI();
			}
			break;
		}
		case 1:
		{
			type = 1;
			files = sortFileList(files, COMPARETYPE_NAME);
			for(File file:files) {
				combo.updateUI();
			}
			break;
		}
		}

	}
	
	File[] sortFileList(File[] files, final int compareType) {
		Arrays.sort(files, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {
				String s1 = "";
				String s2 = "";
				
				if(compareType == COMPARETYPE_DATE) {
					s1 = ((File)object1).getName();
					s2 = ((File)object2).getName();
				}
				else if(compareType == COMPARETYPE_DATE) {
					s1 = ((File)object1).lastModified()+"";
					s2 = ((File)object2).lastModified()+"";
				}
				
				return s1.compareTo(s2);
			}
		});
		return files;
	}
	
	void LoadImage() {
		File dir = new File("C:\\JavaAlbum\\images");
		files = dir.listFiles();
		filenames = dir.list();
		imagelength = files.length;
		
		for(int i=0;i<files.length;i++)
		{
			images[i] = new ImageIcon(files[i].getPath());
			int idx = filenames[i].lastIndexOf(".");
			filenames[i] = filenames[i].substring(0,idx);
		}
			
	}
	
	void ReadImage(int type) {
		SortBy(type);
		imagelength = files.length;
		
		for(int i=0;i<files.length;i++)
		{
			images[i] = new ImageIcon(files[i].getPath());
			int idx = files[i].getName().lastIndexOf(".");
			filenames[i] = filenames[i].substring(0,idx);
		}
	}
	
	class SearchPanel extends JPanel {
		public SearchPanel() {
			JTextField SearchF = new JTextField(15);
			JButton SearchBtn = new JButton("GO");
			
			String[] sortBy = {"LEGO","사전순"};
			sort = new JComboBox(sortBy);
			sort.setSelectedIndex(0);
			sort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox) e.getSource();
					int s = (int)cb.getSelectedIndex();
					SortBy(s);
					currentid = 0;
					imageLabel.setIcon(images[currentid]);
				}
			});
			
			combo = new JComboBox(filenames);
		//	combo.setSelectedIndex(0);
			combo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox) e.getSource();
					String name = (String)cb.getSelectedItem();
					changePicture(name);
				}
			});

			add(combo,BorderLayout.PAGE_END);
			add(SearchF);
			add(SearchBtn);
			add(sort);
			
			SearchBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					searchFile(SearchF.getText());
				}
			});
		}
		
		
		
		void changePicture(String name) {
			String p = imagePath + "\\" + name + ".jpg";
			ImageIcon icon = new ImageIcon(p);
			imageLabel.setIcon(icon);
			if(icon != null) {
				imageLabel.setText(null);
			} else {
				imageLabel.setText("no image");
			}
		}
	}
	
	void searchFile(String name) {
		String p = imagePath+"\\"+name+".jpg";
		File file = new File(p);
		if(file.exists()) {
			try {
				new PopupFrame(name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "파일을 찾을 수 없음","Notice",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	
	class MenuPanel extends JPanel {
		public MenuPanel() {
			Mkdir("images");
			Mkdir("memo");
			setBackground(Color.ORANGE);
			JButton leftBtn = new JButton("<-");
			JButton rightBtn = new JButton("->");
			JButton memosave = new JButton("메모 저장");
			JButton delfile = new JButton("I'm LEGOMAN");
			
			this.add(delfile);
			this.add(memosave, BorderLayout.LINE_END);
			this.add(leftBtn,BorderLayout.CENTER);
			this.add(rightBtn);
			
			LoadImage();
			currentid = 0;
			imageLabel.setIcon(images[currentid]);
			
			delfile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					delFile(filenames[currentid]);
				}
			});
			
			memosave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						memo_save(filenames[currentid]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			
			
			leftBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ReadImage(type);
					currentid--;
					currentid += imagelength;
					currentid %= imagelength;
					memoArea.setText("");
					combo.setSelectedItem(filenames[currentid]);
					try {
						get_memo(filenames[currentid]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					imageLabel.setIcon(images[currentid]);
				}
			});
			
			rightBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ReadImage(type);
					
					currentid++;
					currentid %= imagelength;
					combo.setSelectedItem(filenames[currentid]);
					try {
						get_memo(filenames[currentid]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					imageLabel.setIcon(images[currentid]);
				}
			});
			
			
			
		}
	}
	
	String Mkdir(String p) {
		String save_path = "C:\\JavaAlbum\\" + p;
		File Folder = new File(save_path);
		
		if(!Folder.exists()) {
			try {
				Folder.mkdirs();
			}
			catch(Exception e) {
				e.getStackTrace();
			}
		}
		else {
			
		}
		return save_path;
	}

	
	void img_save() throws IOException
	{
		memoArea.setText("");
		String fileName = JOptionPane.showInputDialog(null, "Enter a name to save the file.","File Name", JOptionPane.PLAIN_MESSAGE);
		String Savepath = imagePath + "\\" + fileName + ".jpg";
		File oriFile = new File(oripath);
		File copyFile = new File(Savepath);
		
		Image im = ImageIO.read(oriFile);
		
		try {
			FileInputStream fis = new FileInputStream(oriFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			
			int fileByte = 0;
			
			while((fileByte = fis.read()) != -1) {
				fos.write(fileByte);
			}
			fis.close();
			fos.close();
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		memo_save(fileName);
		combo.addItem(fileName);
		combo.setSelectedItem(fileName);
		combo.updateUI();
		LoadImage();
		imageLabel.setIcon(new ImageIcon(Savepath));
	}
	
	void memo_save(String name) throws IOException {
		String path = memoPath + "\\" + name + ".txt";
		BufferedOutputStream bs = null;
		String text = memoArea.getText();
		String t = "  ";
		try {
			bs = new BufferedOutputStream(new FileOutputStream(path));
			if(text.length()==0) {
				bs.write(t.getBytes());
			} else {
				bs.write(text.getBytes());
			}
			
		} catch(Exception e) {
			e.getStackTrace();
		}finally {
			bs.close();
		}
	}
	void get_memo(String name) throws IOException {
		String path = memoPath + "\\" + name + ".txt";
		
		BufferedReader br = null;
		
		try {
			String text;
			File file = new File(path);
			br = new BufferedReader(new FileReader(file));
			
			while((text = br.readLine()) != null) {
				memoArea.setText(text);
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
	void delFile(String name) {
		int del = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?","File save",JOptionPane.YES_NO_CANCEL_OPTION);
		if(del == 0) {
			String filepath = imagePath+"\\"+name+".jpg";
			String textpath = memoPath + "\\" + name + ".txt";
			File file = new File(filepath);
			File memofile = new File(textpath);
			
			if(files.length == 1) {
				imageLabel.setIcon(images[++currentid]);
				file.delete();
				memofile.delete();
				currentid--;
				return;
			}
			
			if(file.exists()) {
				file.delete();
			} else {
				JOptionPane.showMessageDialog(null, "이미지파일이 존재하지 않습니다.","error",JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(memofile.exists()) {
				memofile.delete();
			}
			combo.removeItem(filenames[currentid]);
			combo.updateUI();
			ReadImage(type);
			JOptionPane.showMessageDialog(null,"파일이 삭제되었습니다.","Notice",JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	void createMenu() { //메뉴바 만들기 함수
		JMenuBar mb = new JMenuBar();
		JMenu fileMenu = new JMenu("Load file");
		file_ex = new JMenuItem("Load to file Explorer");
		file_path = new JMenuItem("Load to path");
		
		
		fileMenu.add(file_ex);
		fileMenu.add(file_path);
		
		file_ex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileChooser();
				int SoN = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?","File save",JOptionPane.YES_NO_CANCEL_OPTION);
				if(SoN == 0) {
					try {
						img_save();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		file_path.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PathChooser();
				int SoN = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?","File save",JOptionPane.YES_NO_CANCEL_OPTION);
				if(SoN == 0) {
					try {
						img_save();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		mb.add(fileMenu);
		this.setJMenuBar(mb);
		
	}
	
	void FileChooser() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif", "png", "jpeg");
		chooser.setFileFilter(filter);
		
		int ret = chooser.showOpenDialog(null);
		if(ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "No file selected","Warning",JOptionPane.WARNING_MESSAGE);
			return;
		}
		String ex_filePath = chooser.getSelectedFile().getPath(); //파일 탐색기에서 받아온 경로
		oripath=ex_filePath;
		
		new Frame(oripath);
	}
	
	void PathChooser() {
		String path = JOptionPane.showInputDialog(null, "불러올 이미지의 경로를 입력하세요.","Enter file path", JOptionPane.PLAIN_MESSAGE);
		oripath = path;
		Image image = null;
		try {
			File sourceImage = new File(path);
			image = ImageIO.read(sourceImage);

		} catch (IOException arg0)
		{
			JOptionPane.showMessageDialog(null, "파일을 찾을 수 없습니다.","Warning",JOptionPane.WARNING_MESSAGE);
			arg0.printStackTrace();
			return;
		}
		new Frame(image);
	}
	
	public static void main(String[] args) throws IOException {
		
		new album();
	}

}






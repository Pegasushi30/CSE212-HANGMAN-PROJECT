package hangman2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;



public class MainGame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// VARİABLES FOR UI
	private	JMenuBar menuBar;
	private	JMenu fileMenu, helpMenu;
	private	JMenuItem newGame, quit, resetGame, scoreBoard, about;
	private	JTextField[] txtfields;
	private	JLabel tipText, notInStr, statusText, countdownText, leftGuess, gameStatusLabel;
	private	JButton[] btnLetters;
	private	ImageIcon icon1, icon2, icon3, icon4, icon5, icon6, icon7;
	private	JLabel jlbl1;
	
	// VARİABLES FOR OPERATİON
	private List<Word> words = new ArrayList<Word>();
	private	Word currentWord = new Word("", "", "", 0);
	private List<String> hiddenWordArray = new ArrayList<>();
	private List<Boolean> userTorF = new ArrayList<>();
	private int guessRight = 6;
	private	boolean gameIsRunning; // if this is true, game is running. Else game is stopped.
	private	boolean gameOver;
	private	int time = 0;
	private	Timer timer;
	private	TimerTask timerTask;
	private String name;
	
	public MainGame() {
		userTorF.add(true);
		// now, we can start game operation.
		startNewGame(true);
		// firstly, we should put menu items for UI.
		setMenuItems();
		// secondly, we should put labels for UI.
		setLabels();
		// now, we should put keyboard buttons for UI.
		setButtons();
		// finally, we should set images for UI.
		setImages();
		//I am changing background color because why not :)
		getContentPane().setBackground(new Color(100,150,200));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == newGame) {
			startNewGame(false);
		  } else if (e.getSource() == resetGame) {
		    resetGame();
		  } else if (e.getSource() == scoreBoard) {
		    showHighScores();
		  } else if (e.getSource() == about) {
		    JOptionPane.showMessageDialog(this, "Made by Burak Eymen Cevik\nStudent ID:20200702123\nburakeymen.cevik@std.yeditepe.edu.tr", "About", JOptionPane.PLAIN_MESSAGE);
		  }
		for (int i = 0; i < btnLetters.length; i++) {
			boolean TorF;
			// keyboard button pressed by mouse.
			if (e.getSource() == btnLetters[i]) {
				JButton pressedButton = (JButton) e.getSource();
				char pressedButtonText = pressedButton.getText().charAt(0);
				int index = currentWord.getWord().indexOf(pressedButtonText);
				
				// if pressedButtonText is in the word.
				for (int i1 = 0; i1 < hiddenWordArray.size(); i1++) {
					if (currentWord.getWord().charAt(i1) == pressedButtonText) {
						TorF=true;
						playSound(TorF);
						hiddenWordArray.set(i1, String.valueOf(currentWord.getWord().charAt(index)));
					}
				}
				
				// if pressedButtonText is not in the word.
				if (index == -1) {
					if(guessRight != 0) {
						TorF=false;
						playSound(TorF);
						guessRight--;
					}
					leftGuess.setText(String.valueOf("Left Guesses : " + guessRight));
					System.out.println("Hak: " + guessRight);
				}
				
				// pressed button is have to be disabled.
				pressedButton.setEnabled(false);
			}
		}
		
		// we should once show alert.
		// so if gameOver is false, show alert.
		if (!gameOver) {
			// we should update our text fields.
			updateTextFields();
			
			updateImage();
			
			// assigning isRunning variable.
			checkGameState();
			
			// if guessRight is 0, we should game over.
			if (!gameIsRunning) {
				timer.cancel();
				gameOver(isWonOrLost());
			}
		}
	}
	
	void checkGameState() {
		if (isWonOrLost() || guessRight == 0 || time == 0) {
			gameIsRunning = false;
		}
		System.out.println("gameIsRunning: " + gameIsRunning);
		System.out.println("isWonOrLost: " + (!gameIsRunning ? isWonOrLost() : "null"));
	}
	
	boolean isWonOrLost() {
		for (int i = 0; i < hiddenWordArray.size(); i++) {
			if (hiddenWordArray.get(i) == "_") {
				// user is lost.
				return false;
			}
		}
		// user is won.
		return true;
	}
	
	void gameOver(boolean isWon) {
		gameOver = true;
		// we should report status of user.
		for(int i=0;i<26;i++) {
			btnLetters[i].setEnabled(false);
		}
		userTorF.add(isWon);
		gameStatusLabel.setText(isWon ? "You Won!" : "You Lost!");
        gameStatusLabel.setForeground(isWon ? Color.green : Color.red);
		
		boolean check =true;
		while(check) {
			try {
				name = JOptionPane.showInputDialog(this, "Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);

			if(name==null)  {
				
				throw new IllegalStateException();}
			
			
			if (!name.matches("[a-zA-Z0-9çöğüışÇÖĞÜİŞ]+")) {
				
			    throw new IllegalArgumentException();}
			if(name!=null && name.matches("[a-zA-Z0-9çöğüışÇÖĞÜİŞ]+")) {
				check=false;
			}
			}
			 catch(IllegalStateException x) {
				JOptionPane.showMessageDialog(this, "Please enter your name!","Missing Information", JOptionPane.ERROR_MESSAGE);
			}
			catch(IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, "Name should include numbers or letters!","Missing Information", JOptionPane.ERROR_MESSAGE);
			    }
				
			}
		
		// we should save user incoming name.
		int timeElapsed = currentWord.getTimeOut() - time;
		saveUserDetails(name, timeElapsed);
		System.out.println(isWon ? "Kazandiniz!" : "Kaybettiniz!");

	}
	
  	private void startTimer() {
  		timer = new Timer();
		time = currentWord.getTimeOut();
		countdownText.setText("Countdown : " + time);
		time--;
		timerTask = new TimerTask() {
			@Override
			public void run() {
				countdownText.setText("Countdown : " + time);
				
				if (!gameIsRunning || time == 0) {
					timer.cancel();
					checkGameState();
					gameOver(isWonOrLost());
				}
				time--;
			}
		};
		timer.scheduleAtFixedRate(timerTask, 1000, 1000);
	  }
	
	
	void setMenuItems() {
		setTitle("Hangman");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(1000,500);
	    setResizable(false);
	    setLayout(null);
	    
	
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		
		about = new JMenuItem("About");
		about.addActionListener(this);
		
		newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);
		
		resetGame = new JMenuItem("Reset Game");
		resetGame.addActionListener(this);
		
		scoreBoard = new JMenuItem("Score Table");
		scoreBoard.addActionListener(this);
		
		quit = new JMenuItem("Quit");
		quit.addActionListener(this);
		
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu); 
		
		helpMenu.add(about);
		fileMenu.add(newGame);
		fileMenu.add(resetGame);
		fileMenu.add(scoreBoard);
		
		AbstractAction quitAction = new AbstractAction("Quit") {
		    
			private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
				int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want to quit?","Message",JOptionPane.YES_OPTION);

				if(dialogButton == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
            }
        };
        
        quitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_DOWN_MASK));
        quit.setAction(quitAction);
        fileMenu.add(quit);
		
		setJMenuBar(menuBar);
	}
	
	void updateLabelTexts() {
		tipText.setText("Tip : " + currentWord.getClue());
        notInStr.setText("Not in string : " + currentWord.getNotInWord());
        statusText.setText("Status : ");
        gameStatusLabel.setText("In Progress");
        gameStatusLabel.setForeground(Color.BLUE);
        countdownText.setText("Countdown : " + currentWord.getTimeOut());
        leftGuess.setText("Left Guesses : " + guessRight);
	}
	
	void setLabelTexts() {
		tipText = new JLabel ("Tip : " + currentWord.getClue());
        notInStr = new JLabel ("Not in string : " + currentWord.getNotInWord());
        statusText = new JLabel ("Status : ");
        gameStatusLabel = new JLabel("In Progress");
        gameStatusLabel.setForeground(Color.BLUE);
        countdownText = new JLabel ("Countdown : " + currentWord.getTimeOut());
        leftGuess = new JLabel("Left Guesses : " + guessRight);
	}
	
	void setLabels() {
		// set our label texts.
		setLabelTexts();
		
		gameStatusLabel.setBounds (75, 100, 350, 30);
        gameStatusLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        tipText.setBounds (25, 20, 350, 30);
        notInStr.setBounds (25, 50, 350, 30);
        countdownText.setBounds (250, 100, 200, 25);
        leftGuess.setBounds(250,35,200,25);
        statusText.setBounds (25, 100, 350, 30);
        
        gameStatusLabel.setBounds (75, 100, 350, 30);
        gameStatusLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        tipText.setBounds (25, 20, 350, 30);
        notInStr.setBounds (25, 50, 350, 30);
        countdownText.setBounds (250, 100, 200, 25);
        leftGuess.setBounds(250,35,200,25);
        statusText.setBounds (25, 100, 350, 30);
        
        add(tipText);
        add(notInStr);
        add(countdownText);
        add(statusText);
        add(leftGuess);
        add(gameStatusLabel);
        setVisible(true);
	}

	void setButtons() {
		btnLetters = new JButton[26];
        int i=0;
        int xyadd=0;
        for(char letter='A';letter<='I';letter++) {
        	btnLetters[i]=new JButton(""+letter);
        	btnLetters[i].setBounds(xyadd+25, 220, 55, 45);
        	btnLetters[i].addActionListener(this);
        	btnLetters[i].setFocusable(false);
        	add(btnLetters[i]);
        	xyadd=xyadd+60;
        	i++;
        }
        i=9;
        xyadd=0;
        for(char letter='J';letter<='R';letter++) {
        	btnLetters[i]=new JButton(""+letter);
        	btnLetters[i].setBounds(xyadd+25, 270, 55, 45);
        	btnLetters[i].addActionListener(this);
        	btnLetters[i].setFocusable(false);
        	add(btnLetters[i]);
        	xyadd=xyadd+60;
        	i++;
        }
        i=18;
        xyadd=0;
        for(char letter='S';letter<='Z';letter++) {
        	btnLetters[i]=new JButton(""+letter);
        	btnLetters[i].setBounds(xyadd+65, 320, 55, 45);
        	btnLetters[i].addActionListener(this);
        	btnLetters[i].setFocusable(false);
        	add(btnLetters[i]);
        	xyadd=xyadd+60;
        	i++;
        }
	}

	void setImages() 
	{
	  icon1 = new ImageIcon("hangman2/images/1.png");
	  jlbl1 = new JLabel(icon1);
	  
	  icon2 = new ImageIcon("hangman2/images/2.png");
	  icon3 = new ImageIcon("hangman2/images/3.png");
	  icon4 = new ImageIcon("hangman2/images/4.png");
	  icon5 = new ImageIcon("hangman2/images/5.png");
	  icon6 = new ImageIcon("hangman2/images/6.png");
	  icon7 = new ImageIcon("hangman2/images/7.png");
	  
	  jlbl1.setBounds(750, 10, 200, 400);
	  
	  this.getContentPane().add(jlbl1);
	}
	
	void updateImage() {
		switch(guessRight) {
			case 6:
				jlbl1.setIcon(icon1);
				break;
			case 5:
				jlbl1.setIcon(icon2);
				break;
			case 4:
				jlbl1.setIcon(icon3);
				break;
			case 3:
				jlbl1.setIcon(icon4);
				break;
			case 2:
				jlbl1.setIcon(icon5);
				break;
			case 1:
				jlbl1.setIcon(icon6);
				break;
			case 0:
				jlbl1.setIcon(icon7);
				break;
		}
	}
	
	void startNewGame(boolean isFirstGame) {
		
		// notify start the game.
		gameIsRunning = true;
		gameOver = false;
		
		// if last game is exists, this function is working.
		clearLastGame();
		
		// when we start a new game, we should select word from "words.txt".
		selectWordFromDB();
		
		if (isFirstGame) {
			// we should update labels from UI.
			setLabelTexts();
		} else {
			updateLabelTexts();
		}
		
		// we should start the timer.
		startTimer();
	}
	
	void resetGame() {
		
		// we need to revert the variables to their original state.
		
		gameIsRunning = true;
		gameOver = false;	
		guessRight = 6;
		leftGuess.setText("Left Guesses : " + guessRight);
		gameStatusLabel.setText("In Progress");
        gameStatusLabel.setForeground(Color.BLUE);
        countdownText.setText("Countdown : " + time);
        
		// we should set enable keyboard buttons.
		for (JButton btn : btnLetters) {
			btn.setEnabled(true);
		}
		
		
		for (int i = 0; i < hiddenWordArray.size(); i++) {
			hiddenWordArray.set(i, "_");
			txtfields[i].setText("_");
		}
		
			// we should update labels from UI.	
		updateLabelTexts();
		timer.cancel();
		startTimer();
		
	}
	
	void clearLastGame(){
		// reset game --> true
		// new game --> false
		if (btnLetters != null) {
			
			// we need to revert the variables to their original state.
			words = new ArrayList<Word>();
			currentWord = new Word("", "", "", 0);
			hiddenWordArray = new ArrayList<>();
			guessRight = 6;
			leftGuess.setText("Left Guesses : " + guessRight);
			gameStatusLabel.setText("In Progress");
	        gameStatusLabel.setForeground(Color.BLUE);
	        timer.cancel();
			
			// we should set enable keyboard buttons.
			for (JButton btn : btnLetters) {
				btn.setEnabled(true);
			}
			
			hiddenWordArray = new ArrayList<>();
			
			
			// we should remove text field components in screen.
			for (int i = 0; i < txtfields.length; i++) {
				remove(txtfields[i]);
			}
			
			// we should repaint screen.
			repaint();
			
		}
	}
	
	void selectWordFromDB() {
		
		// we should new word select from database. Next, we should make the assignments for the variables.
		try (BufferedReader reader = new BufferedReader(new FileReader("hangman2/words.csv"))) {
		      String line;
		      while ((line = reader.readLine()) != null) {
		        String[] parts = line.split(",");
		        parts[2]=parts[2].replaceAll("\\s","");
		        Word word1 = new Word(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
		        words.add(word1);
		      }
		      
		      // we should select random word. 
		      Random random = new Random();
			  int index = random.nextInt(words.size());
			  // assign selected word model.
			  currentWord = words.get(index);
			  System.out.println("current word: " + currentWord.getWord());
			  // now, we should make text fields for UI.
			  txtfields = new JTextField[currentWord.getWord().length()];
			  // we should assign hidden word.
			  for (int i = 0; i < currentWord.getWord().length(); i++) {
				  hiddenWordArray.add("_");
			  }
			  

			  // now, we can put text fields on screen for UI.
			  for (int i = 0; i < currentWord.getWord().length(); i++) {
				  
				  txtfields[i] = new JTextField();
				  txtfields[i].setBounds(30 + i * 70, 150, 50, 50);
				  txtfields[i].setText(hiddenWordArray.get(i));
				  txtfields[i].setEditable(false);
				  txtfields[i].setHorizontalAlignment(SwingConstants.CENTER);
				  txtfields[i].setFont(new Font("Arial", Font.PLAIN, 24));
				  txtfields[i].setBackground(Color.LIGHT_GRAY);
				  txtfields[i].setBorder(new LineBorder(Color.DARK_GRAY, 2));
				  add(txtfields[i]);
			  }
			  
		      
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
	}
	
	void updateTextFields() {
		for (int i = 0; i < currentWord.getWord().length(); i++) {
			if (currentWord.getWord().charAt(i) == hiddenWordArray.get(i).charAt(0)) {
				txtfields[i].setText(String.valueOf(currentWord.getWord().charAt(i)));
			}
		}
	}

	
	private void saveUserDetails(String name, int timeElapsed) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	  
	    try (FileWriter writer = new FileWriter("hangman2/scores.txt", true)) {
	        writer.write(name + "," + dateFormat.format(date) + "," + currentWord.getWord() + "," + currentWord.getWord().length() + "," + timeElapsed + "," + isWonOrLost()+"\n");
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void showHighScores() {
		  JDialog dialog = new JDialog(this, "High Scores", true);
		  dialog.setSize(400, 300);
		  dialog.setLocationRelativeTo(this);
		  dialog.setLayout(new BorderLayout());
		  JTable table = new JTable();
		  table.setModel(new HighScoreTableModel());
		  dialog.add(new JScrollPane(table), BorderLayout.CENTER);
		  dialog.setVisible(true);
	}
	
	void playSound(boolean TorF) {
        try {
            String soundName;
            if (TorF) {
                soundName = "hangman2/sounds/correct.wav";
            } else {
                soundName= "hangman2/sounds/wrong.wav";
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.addLineListener(new LineListener()
            { 
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP)
	                  clip.close();
				
			}
            });
        } catch(Exception e) {
            System.out.println("playSound exception: " + e);
        }

    }
	
}

package hangman2;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HighScoreTableModel extends AbstractTableModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String[]> scores2;
   
    
    
	public HighScoreTableModel() {
	
		loadScores();
	}


	private void loadScores() {
	    List<String[]> scores = new ArrayList<>();
	    scores2 = new ArrayList<>();
	 
	    try (BufferedReader reader = new BufferedReader(new FileReader("hangman2/scores.txt"))) {
	        // Check if the file is empty
	        if (Files.size(Paths.get("hangman2/scores.txt")) == 0) {
	            // The file is empty, so there are no scores to load
	            return;
	        }
	 
	        String line;
	        int points=0;
	 
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts.length >= 6) {
	                boolean b1 = Boolean.parseBoolean(parts[5]); 
	                if(b1) {
	                    points = Integer.parseInt(parts[3]) * 10 - Integer.parseInt(parts[4]);
	                }
	                else {
	                    points = 0;
	                }
	                parts[4] = String.valueOf(points);
	            } else {
	                // The parts array does not have enough elements, so skip this line
	                continue;
	            }
	            scores.add(parts);
	        }
	 
	            	// Sort the list of scores by points in descending order
	            if(scores!=null) {
	            	scores.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2[4]), Integer.parseInt(o1[4])));
	            
	            	// Add the top 10 scores to scores2
	            	for (int i = 0; i < 10 && i < scores.size(); i++) {
	                scores2.add(scores.get(i));
	            	}
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
 
    @Override
    public int getRowCount() {
        return scores2.size();
    }
 
    @Override
    public int getColumnCount() {
        return 6;
    }
 
    @Override
    public Object getValueAt(int row, int col) {
        String[] score = scores2.get(row);
 
        if (col < score.length) {
            return score[col];
        } else {
            return "";
        }
    }
 
    @Override
    public String getColumnName(int col) {
        switch (col) {
        case 0:
            return "Name";
        case 1:
            return "Date";
        case 2:
            return "Word";
        case 3:
            return "Length";
        case 4:
        	return "Points";
        case 5:
        	return "T or F";
        default:
            return "";
        }
    }
}
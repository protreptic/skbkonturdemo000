package skbconturdemo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserQueries {
	
	List<String> userQueries = new ArrayList<String>();
	
	public UserQueries(String path) {
		readQueries(path);
	}
	
	private void readQueries(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));

			int n = Integer.valueOf(reader.readLine());
			
			for (int i = 0; i < n; i++) {
				reader.readLine();
			}
			
			int m = Integer.valueOf(reader.readLine());
			
			for (int i = 0; i < m; i++) {
				userQueries.add(reader.readLine());
			}
			 
			reader.close();
			reader = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		} 

	}
	
	public List<String> getUserQueries() {
		return userQueries;
	}
	
	public Integer getSize() {
		return this.userQueries.size();
	}
	
}

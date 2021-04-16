package fileManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FolderReaderHelper {
	public static final String SAVES_PATH = System.getProperty("user.home") + "/tdt4100/FindingAWay/saves/";
	public static final String LEVELS_PATH = System.getProperty("user.home") + "/tdt4100/FindingAWay/levels/";
	
	
	public static List<String> getItemsInFolder(Path dirPath) {
		List<String> list = new ArrayList<>();
		try {
			File dir = new File(dirPath.toString());
			File[] files = dir.listFiles();
			for (File file: files) {
				if (file.getName().charAt(0) != '.')
					list.add(file.getName());
			}
			return list;
		}
		catch (NullPointerException e) {
			try {
				Files.createDirectories(dirPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		System.out.println(getItemsInFolder(Path.of(System.getProperty("user.home"), "tdt4100", "FindingAWay", "levels")));
	}
}

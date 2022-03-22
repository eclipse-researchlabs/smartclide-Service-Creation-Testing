package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectUtils {
	
	private String findFolder(String statingFolderPath, String folderName) {

		File clone_folder = new File(statingFolderPath);
		
		return findFolderByNamePerLevel(clone_folder, folderName);
	}
	
	private String findFolderByNamePerLevel(File root, String searchName) {
		String folderPath = root.getAbsolutePath();
		List<String> nextLevel = new ArrayList<String>();
		String[] fileNames = getDirectories(root);
		Arrays.asList(fileNames).forEach(fileName -> {
			nextLevel.add(folderPath+File.separator+fileName);
	    });
		
		return findFolderByNamePerLevelRecurse(nextLevel, searchName);

	}
	
	private String findFolderByNamePerLevelRecurse(List<String> folders, String searchName) {
		
		List<String> nextLevel = new ArrayList<String>();
		
		for(String folderPath : folders) {
			System.out.println("checking : "+folderPath);
			File root = new File(folderPath);
			if (root.getName().equals(searchName)) {
				return root.getAbsolutePath();
			}
			
			String[] fileNames = getDirectories(root);
			if(fileNames==null || fileNames.length==0) {
				continue;
			}
			Arrays.asList(fileNames).forEach(fileName -> {
				nextLevel.add(folderPath+File.separator+fileName);
		    });
		}

		if(nextLevel.size()!=0) {
			System.out.println("To next level");
			return findFolderByNamePerLevelRecurse(nextLevel, searchName);
		}
		
		return null;
	}
	
	private String[] getDirectories(File folder) {
		String[] directories = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

}

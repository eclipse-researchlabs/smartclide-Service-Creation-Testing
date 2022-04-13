package uom.smartclide.testgeneration.functionality.utils;

/*
 * Copyright (C) 2021 UoM - University of Macedonia
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */



import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ProjectUtils {
	
	//ProjectUtils.copyFolderToDestination(String sourceFolder, String destinationFolder)
	public static boolean copyFolderToDestination(String sourceFolder, String destinationFolder) {
		File srcDir = new File(sourceFolder);
		File destDir = new File(destinationFolder);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static File[] findFilesStartingWith(String statingFolderPath, String startingString) {
		
		File startingFolder = new File(statingFolderPath);

		File[] matches = startingFolder.listFiles(new FilenameFilter()
		{
		  public boolean accept(File dir, String name)
		  {
			return (name.contains(startingString) && name.endsWith(".jar"));
		  }
		});

		if(matches.length<=0) {
			return null;
		}
		
		System.out.println(Arrays.toString(matches));
		
		return matches;
	}
	
	public static String findFilePath(String statingFolderPath, String fileName) {
		
		File startingFolder = new File(statingFolderPath);

		File[] matches = startingFolder.listFiles(new FilenameFilter()
		{
		  public boolean accept(File dir, String name)
		  {
			return name.equals(fileName);
		  }
		});

		if(matches.length!=1) {
			return null;
		}
		
		//System.out.println(Arrays.toString(matches));
		
		return matches[0].getAbsolutePath();
	}

	
	public static String findFolderPath(String statingFolderPath, String folderName) {
		System.out.println("Seaerching for: '"+folderName+"', in "+statingFolderPath);
		File clone_folder = new File(statingFolderPath);
		
		return findFolderByNamePerLevel(clone_folder, folderName);
	}
	
	private static String findFolderByNamePerLevel(File root, String searchName) {
		String folderPath = root.getAbsolutePath();
		List<String> nextLevel = new ArrayList<String>();
		String[] fileNames = getDirectories(root);
		Arrays.asList(fileNames).forEach(fileName -> {
			nextLevel.add(folderPath+File.separator+fileName);
	    });
		
		return findFolderByNamePerLevelRecurse(nextLevel, searchName);

	}
	
	private static String findFolderByNamePerLevelRecurse(List<String> folders, String searchName) {
		
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
	
	private static String[] getDirectories(File folder) {
		String[] directories = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

}

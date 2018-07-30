package com.cobotview.plugin.ui.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

//import org.eclipse.ui.wizards.datatransfer.ImportOperation;


public class AddFileFolderToProject {
	//IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	//IProject myWebProject;
	public void AddFileToProject(IProject myProject, Map<String, String> itemsData)
			throws CoreException, FileNotFoundException {
		String fileName;
		String path;
		String filePath;
		Iterator<String> iter = itemsData.keySet().iterator();
		if (myProject.exists() && !myProject.isOpen())
			myProject.open(null);// Before we can manipulate a project, we must open it.
		myProject.setHidden(false);
		myProject.refreshLocal(0, null);
		while (iter.hasNext()) {
			fileName = iter.next();
			path = itemsData.get(fileName);
			filePath = path + "\\" + fileName; //$NON-NLS-1$
			System.out.print("filePath=" + filePath + "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile newFile = myProject.getFile(fileName);

			if (!newFile.exists()) {
				FileInputStream fileStream = new FileInputStream(filePath);
				newFile.create(fileStream, false, null);
				newFile.refreshLocal(0, null);
			}
		}
	}
		//IFile newFile = myProject.getFile("calc.exe");

		
	
/*		IFile newFile = myProject.getFile("calc.exe");
    	if (newFile.exists()) {
    		IPath newFilePath = new Path("E:\\Eclipse插件开发\\二进制检测\\calc.exe");
    		newFile.copy(newFilePath, false, null);
    		IFile calc = myProject.getFile("calc.exe");
    	}*/

	public void AddFolderToProject(IProject myProject, String folderPath) throws CoreException, FileNotFoundException {
		if (myProject.exists() && !myProject.isOpen())
			myProject.open(null);
		// IFolder newFolderHandle = createFolderHandle(folderPath);
		// IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFolder(folderPath);
		String name[] = folderPath.split("\\\\"); //$NON-NLS-1$ //folderPath=D:\exe_files
		System.out.print("\\\\");// 结果为\\
		int n = name.length - 1;// 数组最后一个内容就是你要的文件名了
		String folderName = name[n];
		System.out.print("folderName=" + folderName + "\r\n");
		IFolder newFolder = myProject.getFolder(folderName);
		newFolder.create(false, true, null);
		System.out.print("newFolder=" + newFolder + "\r\n");// newFolder=F/vv/exe_files
		IPath iFolderPath = new Path(folderPath);
		java.io.File iFolder = iFolderPath.toFile();
		String[] fileNames = iFolder.list();
		int fileCount = fileNames.length;
		String filePath = null;
		for (int i = 0; i < fileCount; i++) {
			filePath = folderPath + "\\" + fileNames[i]; //$NON-NLS-1$
			System.out.print("filePath=" + filePath + "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			IFile newFile = newFolder.getFile(fileNames[i]);
			FileInputStream fileStream = new FileInputStream(filePath);
			newFile.create(fileStream, false, null);
		}

		//IPath containerFullPath = newFolder.getFullPath();
		//IProgressMonitor monitor;
		//folderPath.
		//newFolder.copy(containerFullPath, true, monitor);
		//int filecount=0;  
        //ImportOperation operation = new ImportOperation(containerFullPath,sourceDirectory, fileSystemStructureProvider,this, fileSystemObjects);
        //@param containerPath the full path of the destination container within the  workspace
        // @param source the root file system object to import
        // @param provider the file system structure provider to use
        // @param overwriteImplementor the overwrite strategy to use
        //获取pathName的File对象  

		//LinkedList list = new LinkedList();//保存待遍历文件夹的列表
		//getFiles(folderPath);

	}
/*	 public void getFiles(String folderPath){
		IPath iFolderPath = new Path(folderPath);
		java.io.File iFolder = iFolderPath.toFile();
		java.io.File[] files = iFolder.listFiles();
		String[] fileNames = iFolder.list();
		for(java.io.File file:files){     
			if(file.isDirectory()){
		      
		       * 递归调用
		       
				getFiles(file.getAbsolutePath());
				Set<String> fileList=null;
				fileList.add(file.getAbsolutePath());
				System.out.println("显示"+folderPath+"下所有子目录及其文件"+file.getAbsolutePath());
		     }else
		     {
		      System.out.println("显示"+folderPath+"下所有子目录"+file.getAbsolutePath());
		     }     
		    }
	}*/
}
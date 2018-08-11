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

/**
 * @author TaoTao
 *
 */
public class CobotViewUtils {
	public static void addFileToProject(IProject myProject, Map<String, String> itemsData)
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
			filePath = path + "\\" + fileName;
			System.out.print("filePath=" + filePath + "\r\n");
			IFile newFile = myProject.getFile(fileName);

			if (!newFile.exists()) {
				FileInputStream fileStream = new FileInputStream(filePath);
				newFile.create(fileStream, false, null);
				newFile.refreshLocal(0, null);

				String fileExtension = newFile.getFileExtension();

				if(fileExtension == null || fileExtension.equals("exe") || fileExtension.equals("dll"))
				{
					String cFileName = newFile.getName();
					String asmFileName = newFile.getName();

					if(fileExtension == null)
					{
						cFileName = cFileName + ".c";
						asmFileName = asmFileName + ".asm";
					}else if(fileExtension.equals("exe") || fileExtension.equals("dll"))
					{
						cFileName = cFileName + ".c";
						asmFileName = asmFileName + ".asm";
					}

					IFile cFile = myProject.getFile(cFileName);
					IFile asmFile = myProject.getFile(asmFileName);

					if(!cFile.exists())
					{
						cFile.create(new FileInputStream(path + "\\" + cFileName), false, null);
						newFile.refreshLocal(0, null);
					}

					if(!asmFile.exists())
					{
						asmFile.create(new FileInputStream(path + "\\" + asmFileName), false, null);
						asmFile.refreshLocal(0, null);
					}
				}
			}
		}
	}

	public static void AddFolderToProject(IProject myProject, String folderPath) throws CoreException, FileNotFoundException {
		if (myProject.exists() && !myProject.isOpen())
			myProject.open(null);
		String name[] = folderPath.split("\\\\");
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
	}
}
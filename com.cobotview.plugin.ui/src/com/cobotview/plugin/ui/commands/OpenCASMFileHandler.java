package com.cobotview.plugin.ui.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import com.cobotview.plugin.ui.navigator.CobotViewNavigator;
import com.cobotview.plugin.ui.utils.AddFileFolderToProject;
import com.cobotview.plugin.ui.wizards.CobotViewNewWizard;

public class OpenCASMFileHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog dialog = new FileDialog(shell, SWT.OPEN|SWT.MULTI);
		dialog.setText("打开C/C++或者ASM文件");
		dialog.setFilterExtensions(new String[] {"*.c;*.cpp;*.cxx;*.h;*.asm","*"});
		dialog.open();
		String[] fileNames = dialog.getFileNames();
		String path = dialog.getFilterPath();
		Map<String,String>itemsData= new HashMap<String,String>();

		int n = fileNames.length;

		for (int i = 0;i < n;i++) {
			itemsData.put(fileNames[i],path);
		}

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object element = selection.getFirstElement();
		IProject project = null;
		TreeViewer view = null;

		if(element instanceof IAdaptable)
		{
            project = (IProject)((IAdaptable)element).getAdapter(IProject.class);
		}

		if(project == null)
		{
			WizardDialog wizardDialog = new WizardDialog(shell, new CobotViewNewWizard());
			wizardDialog.open();
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			project = projects[0];
		}

		if (!itemsData.isEmpty() && project != null)
		{
			try {
				AddFileFolderToProject create = new AddFileFolderToProject();
				create.AddFileToProject(project, itemsData);

				IWorkbenchPart part = HandlerUtil.getActivePart(event);

				CobotViewNavigator explorer = (CobotViewNavigator) part;
				view = explorer.getCommonViewer();

				if (view.isExpandable(project)) {
					view.setExpandedState(project, !view.getExpandedState(project));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(view != null)
		{
			view.refresh();
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}

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
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.cobotview.plugin.ui.navigator.CobotViewNavigator;
import com.cobotview.plugin.ui.utils.AddFileFolderToProject;
import com.cobotview.plugin.ui.wizards.CobotViewNewWizard;

public class OpenCASMFileHandler implements IHandler
{

	@Override
	public void addHandlerListener(IHandlerListener handlerListener)
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setText("打开C/C++或者ASM文件");
		dialog.setFilterExtensions(new String[] { "*.c;*.cpp;*.cxx;*.h;*.asm", "*" });
		String result = dialog.open();

		if (result == null)
		{
			return null;
		}

		String[] fileNames = dialog.getFileNames();
		String path = dialog.getFilterPath();
		Map<String, String> itemsData = new HashMap<String, String>();

		int n = fileNames.length;

		for (int i = 0; i < n; i++)
		{
			itemsData.put(fileNames[i], path);
		}

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object element = selection.getFirstElement();
		IProject project = null;
		TreeViewer view = null;

		if (element != null && element instanceof IAdaptable)
		{
			project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
		}

		if (project == null)
		{
			IProject[] remainedProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			WizardDialog wizardDialog = new WizardDialog(shell, new CobotViewNewWizard());
			int status = wizardDialog.open();

			if (status == Window.CANCEL)
				return null;

			if (status == Window.OK)
			{
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

				if (projects != null && projects.length == 1)
				{
					project = projects[0];
				} else
				{
					if (remainedProjects == null || remainedProjects.length < 1)
						return null;

					if( projects.length == remainedProjects.length + 1)
					{
						project = getNewProject(projects, remainedProjects);
					}
				}
			}
		}

		if (!itemsData.isEmpty() && project != null)
		{
			try {
				AddFileFolderToProject create = new AddFileFolderToProject();
				create.AddFileToProject(project, itemsData);

				IWorkbenchPart part = HandlerUtil.getActivePart(event);

				CobotViewNavigator explorer = (CobotViewNavigator) part;
				view = explorer.getCommonViewer();

				if (view.isExpandable(project))
				{
					view.setExpandedState(project, true);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (view != null)
		{
			view.refresh();
		}

		return null;
	}

	//Compare two arrays of projects and find out the different project from updatedProjects.
	private IProject getNewProject(IProject[] updatedProjects, IProject[] remainedProjects)
	{
		boolean match = false;

		for(IProject updatedProject : updatedProjects)
		{
			for(IProject remainedProject : remainedProjects)
			{
				if(updatedProject.getName().equals(remainedProject.getName()))
				{
					match = true;
				}
			}

			if(!match)
			{
				return updatedProject;
			}
		}

		return null;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean isHandled()
	{
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener)
	{
	}
}

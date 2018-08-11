package com.cobotview.plugin.ui.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

/**
 * @author TaoTao
 *
 */
public class OpenExeFileCommand implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Object element = selection.getFirstElement();
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final IWorkbenchPage page = window.getActivePage();

		if(element instanceof IFile)
		{
			IFile file = (IFile)element;
			IProject project = file.getProject();
			String fileExtension = file.getFileExtension();
			String fileName = file.getName();

			if(fileExtension == null || fileExtension.endsWith("exe") || fileExtension.endsWith("dll"))
			{
				if(fileExtension != null)
				{
					fileName = file.getName();
				}

				if(fileName != null && !fileName.isEmpty())
				{
					try {
						IFile cFile = project.getFile(fileName + ".c");
						IFile asmFile = project.getFile(fileName + ".asm");
						IFile[] files = {asmFile, cFile};
						IDE.openEditors(page, files);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
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
package com.cobotview.plugin.ui.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.search.internal.ui.SearchDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class SearchCHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		SearchDialog dialog= new SearchDialog(window, "org.eclipse.cdt.ui.pdomSearchPage");
		dialog.open();
//		HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().resetPerspective();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}

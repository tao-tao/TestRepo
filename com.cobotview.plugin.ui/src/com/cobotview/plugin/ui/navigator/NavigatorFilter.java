package com.cobotview.plugin.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class NavigatorFilter extends ViewerFilter implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if (element instanceof IFile) {
			IFile file = (IFile) element;

			if (!file.getName().endsWith(".c")) {
				if (file.getName().endsWith(".asm") && !file.getName().endsWith(".dis.asm"))
					return true;

				if (file.getName().endsWith(".exe") || file.getName().endsWith(".out")
						|| file.getName().endsWith(".dll"))
					return true;

				if(file.getFileExtension() == null)
					return true;

				return false;
			}
		}

		return true;
	}
}

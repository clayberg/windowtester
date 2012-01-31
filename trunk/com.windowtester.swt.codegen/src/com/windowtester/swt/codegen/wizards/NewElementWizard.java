/*******************************************************************************
 *  Copyright (c) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *******************************************************************************/
package com.windowtester.swt.codegen.wizards;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.windowtester.internal.debug.Logger;

@SuppressWarnings("restriction")
public abstract class NewElementWizard extends Wizard implements INewWizard {

    private IWorkbench fWorkbench;
    private IStructuredSelection fSelection;

    public NewElementWizard() {
        setNeedsProgressMonitor(true);
    }
                
    /**
     * Subclasses should override to perform the actions of the wizard.
     * This method is run in the wizard container's context as a workspace runnable.
     * @param monitor
     * @throws InterruptedException
     * @throws CoreException
     */
    protected abstract void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException;
    
    /**
     * Returns the scheduling rule for creating the element.
     */
    protected ISchedulingRule getSchedulingRule() {
        return ResourcesPlugin.getWorkspace().getRoot(); // look all by default
    }
    
    
    protected boolean canRunForked() {
        return true;
    }
    
    
    protected void handleFinishException(Shell shell, InvocationTargetException e) {
//        String title= NewWizardMessages.getString("NewElementWizard.op_error.title"); //$NON-NLS-1$
//        String message= NewWizardMessages.getString("NewElementWizard.op_error.message"); //$NON-NLS-1$
//        ExceptionHandler.handle(e, shell, title, message);
    }
    
    /*
     * @see Wizard#performFinish
     */     
    public boolean performFinish() {
        IWorkspaceRunnable op= new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
                try {
                    finishPage(monitor);
                } catch (InterruptedException e) {
                    throw new OperationCanceledException(e.getMessage());
                }
            }
        };
        try {
            getContainer().run(canRunForked(), true, new WorkbenchRunnableAdapter(op, getSchedulingRule()));
        } catch (InvocationTargetException e) {
            handleFinishException(getShell(), e);
            return false;
        } catch  (InterruptedException e) {
            return false;
        }
        return true;
    }
    
	protected void openResource(final IFile resource) {
		final IWorkbenchPage activePage= getActivePage();
		if (activePage != null) {
			final Display display= getShell().getDisplay();
			if (display != null) {
				display.asyncExec(new Runnable() {
					public void run() {
						try {
							IDE.openEditor(activePage, resource, true);
						} catch (PartInitException e) {
							Logger.log("An error occured opening an editor on the generated resource.", e);
						}
					}
				});
			}
		}
	}
    
	private IWorkbenchPage getActivePage() {
		IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	
    protected void warnAboutTypeCommentDeprecation() {
//        String key= IUIConstants.DIALOGSTORE_TYPECOMMENT_DEPRECATED;
//        if (OptionalMessageDialog.isDialogEnabled(key)) {
//            TemplateStore templates= JavaPlugin.getDefault().getTemplateStore();
//            boolean isOldWorkspace= templates.findTemplate("filecomment") != null && templates.findTemplate("typecomment") != null; //$NON-NLS-1$ //$NON-NLS-2$
//            if (!isOldWorkspace) {
//                OptionalMessageDialog.setDialogEnabled(key, false);
//            }
//            String title= NewWizardMessages.getString("NewElementWizard.typecomment.deprecated.title"); //$NON-NLS-1$
//            String message= NewWizardMessages.getString("NewElementWizard.typecomment.deprecated.message"); //$NON-NLS-1$
//            OptionalMessageDialog.open(key, getShell(), title, null, message, MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0);
//        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        fWorkbench= workbench;
        fSelection= currentSelection;
    }
    
    public IStructuredSelection getSelection() {
    	//this is a work-around.  in some cases, the selection is not getting set
    	
    	if (fSelection == null)
    		fSelection = getCurrentSelection();
    
        return fSelection;
    }

    //taken from AbstractOpenWizardAction and modified
	protected IStructuredSelection getCurrentSelection() {
		IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			ISelection selection= window.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection) {
				return (IStructuredSelection) selection;
			}
		}
		return null;
	}

	public IWorkbench getWorkbench() {
    	//!pq: for some reaon the wrokbench is not getting cached by init...
    	if (fWorkbench == null)
    		fWorkbench = PlatformUI.getWorkbench();
        return fWorkbench;
    }

    protected void selectAndReveal(IResource newResource) {
        BasicNewResourceWizard.selectAndReveal(newResource, getWorkbench().getActiveWorkbenchWindow());
    }

}


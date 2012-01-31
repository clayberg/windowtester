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
/**
 * 
 */
package test;

import org.junit.runner.RunWith;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.junit4.runners.JUnit38ClassRunner;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;

@RunWith(JUnit38ClassRunner.class)
public class OpenAndClosePreferencesJUnit3Test extends UITestCaseSWT {
	
	public void testOpenAndClosePrefs() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("Window/Preferences..."));
		ui.wait(new ShellShowingCondition("Preferences"));
		ui.click(new ButtonLocator("Cancel"));
		ui.wait(new ShellDisposedCondition("Preferences"));
	}
}
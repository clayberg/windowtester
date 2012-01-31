/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.model.ModelLocation;

/**
 * @since 3.1
 */
public abstract class MiniEdit {

public abstract void apply();

public abstract boolean canApply();

public abstract ModelLocation getResultingLocation();

public void reapply() {
	apply();
}

public abstract void rollback();

}

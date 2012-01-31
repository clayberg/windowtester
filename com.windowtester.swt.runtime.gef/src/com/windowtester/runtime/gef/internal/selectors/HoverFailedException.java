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
package com.windowtester.runtime.gef.internal.selectors;

import com.windowtester.runtime.WidgetSearchException;

public class HoverFailedException extends WidgetSearchException {

	private static final long serialVersionUID = 3160181219147465944L;

	
	public HoverFailedException(String msg) {
		super(msg);
	}
	
	public HoverFailedException(Throwable cause) {
		super(cause);
	}
}

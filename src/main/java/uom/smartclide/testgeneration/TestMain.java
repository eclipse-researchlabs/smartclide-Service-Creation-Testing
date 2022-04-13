package uom.smartclide.testgeneration;

/*
 * Copyright (C) 2021 UoM - University of Macedonia
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */

import uom.smartclide.testgeneration.functionality.mainFlow.MainFlow;
import uom.smartclide.testgeneration.functionality.utils.ResultObject;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFlow mainFlow = new MainFlow();

		String gitRepoURL = "";
		String gitUsername = "";
		String gitToken = "";
		
		ResultObject ret = mainFlow.start(gitRepoURL, gitUsername, gitToken);

		System.out.println(ret.getStatus());
		System.out.println(ret.getMessage());

	}

}

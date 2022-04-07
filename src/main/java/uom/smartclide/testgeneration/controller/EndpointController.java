package uom.smartclide.testgeneration.controller;

/*
 * Copyright (C) 2021 UoM - University of Macedonia
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import uom.smartclide.testgeneration.functionality.mainFlow.MainFlow;
import uom.smartclide.testgeneration.functionality.utils.ResultObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class EndpointController {

	@GetMapping("/isOnline")
	public String getRepoURL() {
		return "Hello!";
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/generateTests")
	public ResultObject createStructure(@RequestHeader String gitRepoURL, @RequestHeader String gitUsername,
			@RequestHeader String gitToken) {

		if (isEmptyOrNull(gitRepoURL) || isEmptyOrNull(gitUsername) || isEmptyOrNull(gitToken)) {
			return new ResultObject(1, "One or more fields are empty or null");
		}
		MainFlow mainFlow = new MainFlow();
		ResultObject ret = mainFlow.start(gitRepoURL, gitUsername, gitToken);

		return ret;

	}

	private boolean isEmptyOrNull(String str) {
		return (str.isEmpty() || str == null);
	}
}
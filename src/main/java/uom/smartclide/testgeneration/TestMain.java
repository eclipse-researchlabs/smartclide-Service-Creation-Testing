package uom.smartclide.testgeneration;

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

package mainPack;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

public class MainClass {
	public static void main (String[] args) {

		String repoURL = "https://github.com/eclipse-researchlabs/smartclide-service-creation";
		GenerateClass test = new GenerateClass();
		test.cloneProject(repoURL);
	}
}

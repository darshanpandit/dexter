package queryProcessing;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Query {
	public DocumentScore[] search(String[] tokens, int numberOfResults) throws FileNotFoundException, IOException;

}

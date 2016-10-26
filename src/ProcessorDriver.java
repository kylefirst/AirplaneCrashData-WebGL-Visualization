import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* Kyle Liu
 * Runs the methods in DataPreprocessor to process
 * the specified file
 */
public class ProcessorDriver {
	
	public static void main(String[] args) {
		
		String key = null;
		
		try{
			
		//enter the name of the file containing your GoogleGeocodingApiKey here
		BufferedReader br = new BufferedReader(new FileReader("C:/KK/UMCP/GoogleGeoCodingApiKey/OtherApiKey.txt"));
		key = br.readLine();
		
		}catch(IOException e){
			
			System.out.println("file not found");
		}
		
		DataPreprocessor processor = new DataPreprocessor("AirplaneData",key);
		
		//processor.cleanUpFile();
		processor.geotagData();
		
	}

}

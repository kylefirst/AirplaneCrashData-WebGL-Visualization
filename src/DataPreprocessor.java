import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
/*Kyle Liu
 *Removes all incomplete inputs from data file 
 *Converts location names to latitude and longtitude geotag
 *using Google Map's geocoding API
 */
public class DataPreprocessor {

	private String fileName;
	private String apiKey;

	public DataPreprocessor(String fname, String key) {

		fileName = fname;
		apiKey = key;
	}

	//Writes to a new file with all incomplete inputs from data file removed
	public void cleanUpFile() {

		String  nextLine = null;


		try{

			//bufferedReader/Writer for fast IO
			BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("Processed"+fileName + ".txt"));

			//read in file
			while ((nextLine = br.readLine()) != null) {


				String[] sections = nextLine.split(" ");

				//must contain a location to be a valid input
				if(sections.length > 1 && !sections[0].equals("")) {

					bw.write(nextLine + "\n");
				}	
			}   

			bw.flush();
			bw.close();
			br.close();

		}catch(IOException e){

			System.out.println("file not found");
		}
	}

	//converts location names into geotags and JSON format for WebGL globe
	public void geotagData() {

		String  nextLine = null;
		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		GeocodingResult[] results = null;
		
		try{

			//bufferedReader/Writer for fast IO
			BufferedReader br = new BufferedReader(new FileReader("Processed"+fileName + ".txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("Processed"+fileName + ".json", true));

			//read in file, Api limit of 2500 requests/day
			while ((nextLine = br.readLine()) != null) {

				String[] sections = nextLine.split(" ");
			
				//get the name of the location
				String locName = "";
				
				for(int i = 0; i < sections.length-1; i++)
					locName+= sections[i] + " ";
			
				//gets latitude and longitude from location
				try {
					results = GeocodingApi.geocode(context, locName).await();
				} catch (Exception e) {
					e.printStackTrace();
				}
		
				//writes to json file if valid location
				if(results.length > 0)
					bw.write(results[0].geometry.location.toString() + "," + sections[sections.length-1] + ",\n");
			}   

			bw.flush();
			bw.close();
			br.close();

		}catch(IOException e){

			System.out.println("file not found");
		}
	}

}

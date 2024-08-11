package sgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ProcessRecord {

	String prettyJson = null;
	StringBuffer response = new StringBuffer();
	JsonNode rootNode;
	JsonNode resultNode;
	
	public void fetchRecordCreatedTime(String instance_name, String input_record) {
		// TODO Auto-generated method stub
		
		String url="https://"+instance_name+".service-now.com/api/now/table/cmdb_ire_output_target_item/"+input_record;
		//System.out.println("URL : "+ url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=null;
			con = (HttpURLConnection)obj.openConnection();
			String username="phani.admin";
			String password="P@ssw0rd12ee";
			con.setRequestMethod("GET");
			String userpass = username + ":" + password;  
	        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));
	        BufferedReader in=null;
			String inputLine;
	        //System.out.println(basicAuth);  
	        con.setRequestProperty ("Authorization", basicAuth);  
			int responseCode=con.getResponseCode();
			//System.out.println("Response code : "+ responseCode);
			in = new BufferedReader(
				         new InputStreamReader(con.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				 response.append(inputLine);
			}
			in.close();
			
			populateOutputTargetItem(response);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}	
	}

	private void populateOutputTargetItem(StringBuffer response) {
		// TODO Auto-generated method stub
		
		ObjectMapper objectMapper=new ObjectMapper();
		try {
			rootNode = objectMapper.readTree(response.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultNode=rootNode.path("result");
		
		RunId runId=new RunId();
		runId.setLink(resultNode.path("run_id").path("link").textValue());
		runId.setValue(resultNode.path("run_id").path("value").textValue());
		
		SourceRecordId sourceRecordId=new SourceRecordId();
		sourceRecordId.setLink(resultNode.path("source_record_id").path("link").textValue());
		sourceRecordId.setValue(resultNode.path("source_record_id").path("value").textValue());
		
		TargetRecordId targetRecordId=new TargetRecordId();
		targetRecordId.setLink(resultNode.path("target_record_id").path("link").textValue());
		targetRecordId.setValue(resultNode.path("source_record_id").path("value").textValue());
		
		OutputTargetItem oti=new OutputTargetItem();
		oti.setExpected_target_table(resultNode.path("expected_target_table").textValue());
		oti.setRun_id(runId);
		oti.setSource_record_id(sourceRecordId);
		oti.setError_detail(resultNode.path("error_detail").textValue());
		oti.setRun_table(resultNode.path("run_table").textValue());
		oti.setSys_mod_count(resultNode.path("sys_mod_count").intValue());
		oti.setSys_updated_on(LocalDateTime.parse(resultNode.path("sys_updated_on").textValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		oti.setSys_tags(resultNode.path("sys_tags").textValue());
		oti.setWarning_detail(resultNode.path("warning_detail").textValue());
		oti.setSys_id(resultNode.path("sys_id").textValue());
		oti.setSys_updated_by(resultNode.path("sys_updated_by").textValue());
		oti.setSys_created_on(LocalDateTime.parse(resultNode.path("sys_created_on").textValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		oti.setActual_target_table(resultNode.path("actual_target_table").textValue());
		oti.setTarget_record_id(targetRecordId);
		oti.setOperation(resultNode.path("operation").textValue());
		oti.setSource_table(resultNode.path("source_table").textValue());
		oti.setSys_created_by(resultNode.path("sys_created_by").textValue());
		
		System.out.println(oti.getSys_id());
		System.out.println(oti.getSys_created_on());
	}
}

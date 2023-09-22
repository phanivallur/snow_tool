package sgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class InstanceDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String instance_name=args[0];
		String source_sys_id=args[1];
		try {
			InstanceDriver id=new InstanceDriver();
			String sysImportSetRowId=source_sys_id;
			String singleImportSetApiResponse=id.createSingleImportSet(sysImportSetRowId, instance_name);
			String singleRowImportSetId=id.fetchSingleRowImportSetId(singleImportSetApiResponse);
			System.out.println("Single Row Import Set Id : "+singleRowImportSetId);
			String iSetResponse=id.fetchImportSetAPIResponse(singleRowImportSetId, instance_name);
			//System.out.println("Import Set Response : "+ iSetResponse);
			String importSetId=id.createImportSetObject(iSetResponse);
			System.out.println("Corresponding Import Set ID : " + importSetId);
			String transformHistoryApiResponse=id.fetchTransformHistoryApiResponse(importSetId,instance_name);
			String transformHistorySysId=id.obtainTransformHistorySysId(transformHistoryApiResponse);
			System.out.println("Required Run Context Id : "+ transformHistorySysId);
			String otiApiResponse=id.fetchOtiApiResponse(transformHistorySysId, instance_name);
			//System.out.println("Output Target Item Response : "+ otiApiResponse);
			String operation=id.printOutputTargetItemList(otiApiResponse,transformHistorySysId);
			//System.out.println("Operation : "+ operation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String fetchSingleRowImportSetId(String singleImportSetApiResponse) {
		// TODO Auto-generated method stub
		
		List<SingleImportSet> sisList=new ArrayList<SingleImportSet>();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		objectMapper.setDateFormat(df);
		String sisSysId=null;
		
		try {
			JsonNode rootNode=objectMapper.readTree(singleImportSetApiResponse);
			JsonNode resultNode=rootNode.path("result");
			//System.out.println(resultNode);
			/*Iterator<JsonNode> risNodes=resultNode.elements();
			while(risNodes.hasNext()) {
				JsonNode risNode=risNodes.next();
				SingleImportSet singleImportSet=objectMapper.treeToValue(risNode, SingleImportSet.class);
				sisList.add(singleImportSet);
			}
			for(SingleImportSet singleImportSet:sisList) {
				sisSysId=singleImportSet.getResImportSet();
			}*/
			sisSysId=resultNode.get("resImportSet").textValue();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sisSysId;
	}

	private String createSingleImportSet(String sysImportSetRowId, String instance_name) {
		// TODO Auto-generated method stub
		
		String prettyJson=null;
		String url="https://"+instance_name+".service-now.com/api/snc/import_set/"+sysImportSetRowId;
		System.out.println(url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=(HttpURLConnection)obj.openConnection();
			String username="phani.admin";
			String password="P@ssw0rd12ee";
			try {
				con.setRequestMethod("POST");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String userpass = username + ":" + password;  
	        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));  
	        //System.out.println(basicAuth);  
	        con.setRequestProperty ("Authorization", basicAuth);  
			int responseCode=con.getResponseCode();
			//System.out.println("Response code : "+ responseCode);
			BufferedReader in=new BufferedReader(
				         new InputStreamReader(con.getInputStream()));			
		    String inputLine;
		    StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				 	response.append(inputLine);
			}
		    in.close();
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		    JsonNode jsonNode;
			jsonNode = objectMapper.readTree(response.toString());
			prettyJson = objectMapper.writeValueAsString(jsonNode);			
		    //System.out.println(prettyJson);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return prettyJson;
	}

	private String printOutputTargetItemList(String otiApiResponse, String transformHistorySysId) {
		// TODO Auto-generated method stub
		List<OutputTargetItem> otiList=new ArrayList<OutputTargetItem>();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		objectMapper.setDateFormat(df);
		String otiOp=null;
		
		try {
			JsonNode rootNode=objectMapper.readTree(otiApiResponse);
			JsonNode resultNode=rootNode.path("result");
			//System.out.println(resultNode);
			Iterator<JsonNode> otiNodes=resultNode.elements();
			while(otiNodes.hasNext()) {
				JsonNode otiNode=otiNodes.next();
				OutputTargetItem outputTargetItem=objectMapper.treeToValue(otiNode, OutputTargetItem.class);
				otiList.add(outputTargetItem);
			}	
			System.out.println("Source ID\t\t\t\t\tExpected Target Table\t\tActual Target Table\t\tOperation\t\t\t\tTarget Id");
			for(OutputTargetItem oti:otiList) {
				System.out.println(oti.getSource_record_id().getValue()+"\t\t"+oti.getExpected_target_table()+"\t\t"+oti.getActual_target_table()+"\t\t"+oti.getOperation()+"\t\t\t\t"+oti.getTarget_record_id().getValue());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return otiOp;
	}
	
	private String fetchOtiApiResponse(String transformHistorySysId, String instance_name) {
		// TODO Auto-generated method stub
		String prettyJson=null;
		String url="https://"+instance_name+".service-now.com/api/now/table/cmdb_ire_output_target_item?sysparm_query=run_id%3D"+transformHistorySysId;
		System.out.println(url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=(HttpURLConnection)obj.openConnection();
			String username="phani.admin";
			String password="P@ssw0rd12ee";
			try {
				con.setRequestMethod("GET");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String userpass = username + ":" + password;  
	        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));  
	        //System.out.println(basicAuth);  
	        con.setRequestProperty ("Authorization", basicAuth);  
			int responseCode=con.getResponseCode();
			//System.out.println("Response code : "+ responseCode);
			BufferedReader in=new BufferedReader(
				         new InputStreamReader(con.getInputStream()));			
		    String inputLine;
		    StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				 	response.append(inputLine);
			}
		    in.close();
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		    JsonNode jsonNode;
			jsonNode = objectMapper.readTree(response.toString());
			prettyJson = objectMapper.writeValueAsString(jsonNode);			
		    //System.out.println(prettyJson);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return prettyJson;
	}

	private String obtainTransformHistorySysId(String transformHistoryApiResponse) {
		// TODO Auto-generated method stub
		
		List<ImportSetRun> transformHistoryList=new ArrayList<ImportSetRun>();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		objectMapper.setDateFormat(df);
		String transformHistorySysId=null;
		
		try {
			JsonNode rootNode=objectMapper.readTree(transformHistoryApiResponse);
			JsonNode resultNode=rootNode.path("result");
			//System.out.println(resultNode);
			Iterator<JsonNode> transformHistoryNodes=resultNode.elements();
			while(transformHistoryNodes.hasNext()) {
				JsonNode transformHistoryNode=transformHistoryNodes.next();
				ImportSetRun iSetRun=objectMapper.treeToValue(transformHistoryNode, ImportSetRun.class);
				transformHistoryList.add(iSetRun);
			}	
			for(ImportSetRun iSetRun:transformHistoryList) {
				transformHistorySysId=iSetRun.getSys_id();
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transformHistorySysId;
	}
	
	private String fetchTransformHistoryApiResponse(String importSetId, String instance_name) {
		// TODO Auto-generated method stub
		String prettyJson=null;
		String url="https://"+instance_name+".service-now.com/api/now/table/sys_import_set_run?sysparm_query=set%3D"+importSetId;
		System.out.println("Transform History URL : "+ url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=(HttpURLConnection)obj.openConnection();
			String username="phani.admin";
			String password="P@ssw0rd12ee";
			try {
				con.setRequestMethod("GET");
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String userpass = username + ":" + password;  
	        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));  
	        //System.out.println(basicAuth);  
	        con.setRequestProperty ("Authorization", basicAuth);  
			int responseCode=con.getResponseCode();
			//System.out.println("Response code : "+ responseCode);
			BufferedReader in=new BufferedReader(
				         new InputStreamReader(con.getInputStream()));			
		    String inputLine;
		    StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				 	response.append(inputLine);
			}
		    in.close();
		    ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		    JsonNode jsonNode;
			jsonNode = objectMapper.readTree(response.toString());
			prettyJson = objectMapper.writeValueAsString(jsonNode);			
		    //System.out.println(prettyJson);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return prettyJson;
	}

	private String createImportSetObject(String iSetResponse) {
		// TODO Auto-generated method stub
		List<ImportSet> iSetList=new ArrayList<ImportSet>();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		objectMapper.setDateFormat(df);
		String iSetId=null;
		
		try {
			JsonNode rootNode=objectMapper.readTree(iSetResponse);
			JsonNode resultNode=rootNode.path("result");
			//System.out.println(resultNode);
			Iterator<JsonNode> iSetNodes=resultNode.elements();
			while(iSetNodes.hasNext()) {
				JsonNode iSetNode=iSetNodes.next();
				ImportSet iSet=objectMapper.treeToValue(iSetNode, ImportSet.class);
				iSetList.add(iSet);
			}	
			for(ImportSet iSet:iSetList) {
				iSetId=iSet.getSys_id();
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iSetId;
	}

	public String fetchImportSetAPIResponse(String singleRowImportSetId, String instance_name) throws IOException {
		// TODO Auto-generated method stub
		
		String prettyJson = null;
		String url="https://"+instance_name+".service-now.com/api/now/table/sys_import_set?sysparm_query=sys_idSTARTSWITH"+singleRowImportSetId;
		System.out.println("URL : "+ url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=(HttpURLConnection)obj.openConnection();
			String username="phani.admin";
			String password="P@ssw0rd12ee";
			con.setRequestMethod("GET");
			String userpass = username + ":" + password;  
	        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));  
	        //System.out.println(basicAuth);  
	        con.setRequestProperty ("Authorization", basicAuth);  
			int responseCode=con.getResponseCode();
			//System.out.println("Response code : "+ responseCode);
			BufferedReader in = new BufferedReader(
		             new InputStreamReader(con.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     ObjectMapper objectMapper = new ObjectMapper();
		      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		      JsonNode jsonNode = objectMapper.readTree(response.toString());
		      prettyJson = objectMapper.writeValueAsString(jsonNode);
		      //System.out.println(prettyJson);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("prettyJson : "+ prettyJson);
		return prettyJson;
	}
}
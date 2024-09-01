package sgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class InstanceDriver {

	public static void main(String[] args) {
		
		String instance_name=null;
		String input_data=null;
		String record_time=null;
		
		if(args.length==2) {
			instance_name=args[0];
			input_data=args[1];
		} else if(args.length==4) {
			instance_name=args[0];
			input_data=args[1];
			record_time=args[2]+" "+args[3];
		}
		
		if(input_data.contains("db_listlogs")) {
			extractDatabaseLogFilePath(input_data, record_time);	
		} else {
			splitImportSet(instance_name, input_data);
			//ProcessRecord pr=new ProcessRecord();
			//pr.fetchRecordCreatedTime(instance_name, input_data);
		}
		
		// TODO Auto-generated method stub
		
	}

	private static void splitImportSet(String instance_name, String input_data) {
		// TODO Auto-generated method stub
		int i=0;
		InstanceDriver id=new InstanceDriver();
		String importSetResponse=null;
		try {
			importSetResponse = id.fetchImportSetAPIResponse(input_data, instance_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Import Set Response : "+ importSetResponse);
		String importSetNumber=id.createImportSetObject(importSetResponse);
		System.out.println(" Provided sys_id is of import set : " + importSetNumber);
		String importSetRowsResponse=id.fetchImportSetRowsResponse(input_data, instance_name);
		System.out.println("Import Set Rows Response : "+ importSetRowsResponse);
		
		JsonNode rootNode;
		JsonNode resultNode;
		try {
			ObjectMapper objectMapper=new ObjectMapper();
			rootNode = objectMapper.readTree(importSetRowsResponse);
			resultNode=rootNode.path("result");
			ImportSetRow[] listImportSetRow=objectMapper.readValue(resultNode.toString(), ImportSetRow[].class);
			
			for(ImportSetRow importSetRow : listImportSetRow) {
				i++;
			}
			
			System.out.println("Number of rows in import set: "+i);
			int[] numPool=new int[i];
			for(int num=0;num<i;num++) {
				numPool[num]=num+1;
			}
			
			shuffleNum(numPool);
			
			int[] pickedNumbers = Arrays.copyOfRange(numPool, 0, 10);
			//System.out.println("Randomly picked below rows : ");
			for(int num:pickedNumbers) {
				//System.out.println("Processing row : "+listImportSetRow[num].sys_import_row);
				id.reprocessImportSetRow(id, listImportSetRow[num].sys_id, instance_name);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		/*if(importSetRecord) {
			fetchCommentsFromImportSet(source_sys_id);
		} else {
			try {
				InstanceDriver id=new InstanceDriver();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	private static void extractDatabaseLogFilePath(String input_data, String record_time) {
		// TODO Auto-generated method stub
		String fileName=input_data;
		System.out.println("Input for listlogs : "+fileName);
		
		List<String> list=new ArrayList<String>();
		List<DBLogFilePath> dbLogFilePathList=new ArrayList<DBLogFilePath>();
		try(BufferedReader br=Files.newBufferedReader(Paths.get(fileName))){
			list=br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListIterator<String> listIterator=list.listIterator();
		List<LocalDateTime> localTimeStampList=new ArrayList<LocalDateTime>();
		
		while(listIterator.hasNext()) {
			String line=listIterator.next();
			if(line.contains("/glide/mysqld/") && line.contains("mysql-binlog")) {
				//System.out.println(line);
				String[] keys=line.split(" \\| ");
				DBLogFilePath dbLogFilePath=new DBLogFilePath();
				dbLogFilePath.setPort(Integer.parseInt(keys[0].split(" ")[1]));
				//System.out.print(keys[0].split(" ")[1]+"  ");
				dbLogFilePath.setFileType(keys[1]+"  ");
				//System.out.print(keys[1]);
				dbLogFilePath.setFilePath(keys[2]+"  ");
				//System.out.print(keys[2]);
				dbLogFilePath.setFileSize(keys[3]+"  ");
				//System.out.print(keys[3]);
				dbLogFilePath.setFileModified(LocalDateTime.parse(keys[4].split(" \\-")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				//System.out.print(keys[4].split(" \\-")[0]+"  ");
				dbLogFilePathList.add(dbLogFilePath);
				localTimeStampList.add(dbLogFilePath.getFileModified());
			}
		}
		
		ListIterator<LocalDateTime> ltslIterator=localTimeStampList.listIterator();
		while(ltslIterator.hasNext()) {
			System.out.println(ltslIterator.next().toString());
		}
		
		int pos=getRecordTimePosition(record_time, localTimeStampList);	
		System.out.println("Position of given time stamp : "+ pos);
		
		System.out.println(dbLogFilePathList.get(pos).getFilePath());
		
		/*ListIterator<DBLogFilePath> dblfIterator=dbLogFilePathList.listIterator();
		while(dblfIterator.hasNext()) {
			DBLogFilePath dblf=dblfIterator.next();
			LocalDateTime filePathTime=dblf.getFileModified();
			System.out.println(filePathTime.toString());
		}*/
	}
	

	private static int getRecordTimePosition(String record_time, List<LocalDateTime> localTimeStampList) {
		int low = 0;
        int high = localTimeStampList.size()-1;
        //System.out.println("Size of TimeStamp List : "+high);
        @SuppressWarnings("deprecation")
        Locale locale = new Locale("en"); 
  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale);
        LocalDateTime date = LocalDateTime.parse(record_time,formatter);
        ZonedDateTime zoned=date.atZone(ZoneId.of("UTC"));
        ZonedDateTime converted=zoned.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
        //System.out.println("Input Timestamp : "+ converted.toString());

        
        while (low <= high) {
            int mid = (low + high) / 2;
            if (localTimeStampList.get(mid).isBefore(converted.toLocalDateTime())) {
                low = mid + 1;
            } else if (localTimeStampList.get(mid).isAfter(converted.toLocalDateTime())) {
                high = mid - 1;
            } else {
                return mid; // Key found, return the index
            }
        }
        return low; // Key not found, return the insertion point
	}

	public void reprocessImportSetRow(InstanceDriver id, String sys_id, String instance_name) {
		// TODO Auto-generated method stub
		try {
			String sysImportSetRowId=sys_id;
			String singleImportSetApiResponse=id.createSingleImportSet(sysImportSetRowId, instance_name);
			//System.out.println("SingleImportSetResponse : "+singleImportSetApiResponse);
			String singleRowImportSetId=id.fetchSingleRowImportSetId(singleImportSetApiResponse);
			//System.out.println("Single Row Import Set Id : "+singleRowImportSetId);
			String iSetResponse = id.fetchImportSetAPIResponse(singleRowImportSetId, instance_name);
			String importSetId=id.createImportSetObject(iSetResponse);
			System.out.println(importSetId);
			String transformHistoryApiResponse=id.fetchTransformHistoryApiResponse(importSetId,instance_name);
			//System.out.println("Transform History API Response : "+ transformHistoryApiResponse);
			String transformHistorySysId=id.obtainTransformHistorySysId(transformHistoryApiResponse);
			//System.out.println("Required Run Context Id : "+ transformHistorySysId);
			String otiApiResponse=id.fetchOtiApiResponse(transformHistorySysId, instance_name);
			//System.out.println("Output Target Item Response : "+ otiApiResponse);
			//String operation=id.printOutputTargetItemList(sysImportSetRowId, otiApiResponse,transformHistorySysId);
			//System.out.println("Operation : "+ operation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Import Set Response : "+ iSetResponse);
		
	}

	private static void shuffleNum(int[] numPool) {
		// TODO Auto-generated method stub
		Random random=new Random();
		for(int i=numPool.length-1;i>0;i--) {
			int index=random.nextInt(i+1);
			int temp=numPool[index];
			numPool[index]=numPool[i];
			numPool[i]=temp;
		}
	}

	private String fetchImportSetRowsResponse(String importSetSysId, String instance_name) {
		// TODO Auto-generated method stub
		
		StringBuffer response=new StringBuffer();
		//String url="https://"+instance_name+".service-now.com/api/now/table/sys_import_set?sysparm_query=sys_idSTARTSWITH"+singleRowImportSetId;
		String url="https://"+instance_name+".service-now.com/api/now/table/sys_import_set_row?sysparm_query=sys_import_set%3D"+importSetSysId;
		//System.out.println("URL : "+ url);
		try {
			URL obj=new URL(url);
			HttpURLConnection con=null;
			try {
				con = (HttpURLConnection)obj.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				int responseCode=con.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Response code : "+ responseCode);
			BufferedReader in=null;
			try {
				in = new BufferedReader(
				         new InputStreamReader(con.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     String inputLine;
		     try {
				while ((inputLine = in.readLine()) != null) {
				 	response.append(inputLine);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     /*ObjectMapper objectMapper = new ObjectMapper();
		      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		      JsonNode jsonNode=null;
			try {
				jsonNode = objectMapper.readTree(response.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      /*try {
				prettyJson = objectMapper.writeValueAsString(jsonNode);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		      //System.out.println(prettyJson);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("prettyJson : "+ prettyJson);
		return response.toString();
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
			sisSysId=resultNode.get("sys_id").textValue();
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
		String url="https://"+instance_name+".service-now.com/api/snc/singlerowimportset/"+sysImportSetRowId;
		//System.out.println(url);
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

	private String printOutputTargetItemList(String otiApiResponse, String transformHistorySysId, String sourceRowId) {
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
			System.out.println("Result for row with sys_id : "+ sourceRowId);
			System.out.println("Sys ID\t\t\t\t\tExpected Target Table\t\tActual Target Table\t\tOperation\t\t\t\tTarget Id");
			for(OutputTargetItem oti:otiList) {
				System.out.println(oti.getSys_id()+"\t\t"+oti.getExpected_target_table()+"\t\t"+oti.getActual_target_table()+"\t\t"+oti.getOperation()+"\t\t\t\t"+oti.getTarget_record_id().getValue());
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
		//System.out.println(url);
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
		//System.out.println("Transform History URL : "+ url);
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
				iSetId=iSet.getNumber();
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
		//System.out.println("URL : "+ url);
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
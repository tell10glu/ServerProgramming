import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class MyClient {
	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the file path for first matrix");
		JSONObject mtr1 = readMatrixFromFile(scanner.nextLine());
		System.out.println("Enter the file path for second matrix");
		JSONObject mtr2 = readMatrixFromFile(scanner.nextLine());
		System.out.println("Enter Process\nFor Sum : 1 \nFor Sub: 2 \nFor Multiply : 3 \nFor Exit : 0");
		String operationStr = scanner.nextLine();	
		int operation  = -1;
		try {
			operation = Integer.parseInt(operationStr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Wrong input");
			System.exit(0);
		}
		String	response;
		Socket clientSocket=new Socket("localhost",MyServer.PORT_ADRESS);
		DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		JSONObject object = new JSONObject();
		object.put("operation", operation);
		object.put("matrix1", mtr1);
		object.put("matrix2", mtr2);
		String requestForServer =object.toString();
		outToServer.writeBytes(requestForServer+'\n');
		response=inFromServer.readLine();
		
		JSONObject obj = new JSONObject(response);
		String mtr = obj.getString("matrix");
		String[] responseMatrixSplitted = mtr.split(",");
		for(int i=0;i<responseMatrixSplitted.length;i++){
			System.out.println(responseMatrixSplitted[i]);
		}
		clientSocket.close();
		scanner.close();
	}
	
	public static JSONObject readMatrixFromFile(String filePath) throws IOException, JSONException{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String mtr = "";
		String str = null;
		while((str=reader.readLine())!=null){
			mtr+=str;
			mtr+=",";
		}
		int columnCount =mtr.split(",")[0].split(" ").length;
		JSONObject object = new JSONObject();
		object.put("columns", columnCount);
		object.put("rows", mtr.split(",").length);
		mtr.replace("\n", ",");
		object.put("matrix", mtr);
		reader.close();
		return object;
	}
	
}

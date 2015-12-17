import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class MyServer {
	public static final int PORT_ADRESS = 6161;
	public static void main(String[] args) throws IOException, JSONException {

		String clientSentence;
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(PORT_ADRESS);
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Server Received :" + clientSentence);
			String response = handleUserRequest(clientSentence);
			outToClient.writeBytes(response+'\n');
		}
	}
	private static String handleUserRequest(String receivedData) throws JSONException{
		String response = null;
		JSONObject object = new JSONObject(receivedData);
		int operation = object.getInt("operation");
		JSONObject mtr1Object = object.getJSONObject("matrix1");
		JSONObject mtr2Object = object.getJSONObject("matrix2");
		int[][] mtr1 = new int[mtr1Object.getInt("rows")][mtr1Object.getInt("columns")];
		int[][] mtr2 = new int[mtr2Object.getInt("rows")][mtr2Object.getInt("columns")];
		matrixStringHandler(mtr1Object,mtr1);
		matrixStringHandler(mtr2Object,mtr2);
		for(int i =0;i<mtr1.length;i++){
			for (int j = 0; j < mtr1[0].length; j++) {
				System.out.print(mtr1[i][j]+" ");
				
			}
			System.out.println();
		}
		switch (operation) {
		case 1:
			try {
				response = matrixToResponse(sumMatrix(mtr1, mtr2));
			} catch (Exception e) {
				response = e.getMessage();
			}
			break;
		case 2:
			try {
				response = matrixToResponse(subMatrix(mtr1, mtr2));
			} catch (Exception e) {
				response = e.getMessage();
			}
			break;
		case 3:
			
			try {
				int[][] multipliedMatrix = multiplicar(mtr1, mtr2);
				response = matrixToResponse(multipliedMatrix);
			} catch (Exception e) {
				e.printStackTrace();
				response = "Matrix Size Error!";
			}
			
			break;
		default:
			response =  "Wrong Input";
			break;
		}
		return response;
	}
	private static String matrixToResponse(int[][] mtr) throws JSONException{
		String str = "";
		for(int i =0;i<mtr.length;i++){
			for(int j =0;j<mtr[i].length;j++){
				str+=mtr[i][j];
				str+=" ";
			}
			str+=",";
		}
		JSONObject resObject = new JSONObject();
		resObject.put("rows", mtr.length);
		resObject.put("columns", mtr[0].length);
		resObject.put("matrix", str);
		return resObject.toString();
	}
	private static void matrixStringHandler(JSONObject mtrObject, int[][] mtr) throws JSONException{
		String mtrString = mtrObject.getString("matrix");
		String[] mtrRows  = mtrString.split(",");
		for(int i =0;i<mtrRows.length;i++){
			String[] mtrColumns = mtrRows[i].split(" ");
			for(int j =0;j<mtrColumns.length;j++){
				mtr[i][j] = Integer.parseInt(mtrColumns[j]);
			}
		}
	}
	public static int[][] sumMatrix(int[][] A,int[][] B ){
		if (A.length != B.length ) {
            throw new IllegalArgumentException("A and B Has Different Rows ");
        }
		if (A[0].length != B[0].length ) {
            throw new IllegalArgumentException("A and B Has Different Columns ");
        }
		
		int[][] sumMatrix = new int[A.length][A[0].length];
		for(int i =0;i<A.length;i++){
			for(int j =0;j<A[i].length;j++){
				sumMatrix[i][j] = A[i][j]+B[i][j];
			}
		}
		return sumMatrix;
	}
	public static int[][] subMatrix(int[][] A,int[][] B ){
		if (A.length != B.length ) {
            throw new IllegalArgumentException("A and B Has Different Rows ");
        }
		if (A[0].length != B[0].length ) {
            throw new IllegalArgumentException("A and B Has Different Columns ");
        }
		
		int[][] sumMatrix = new int[A.length][A[0].length];
		for(int i =0;i<A.length;i++){
			for(int j =0;j<A[i].length;j++){
				sumMatrix[i][j] = A[i][j]-B[i][j];
			}
		}
		return sumMatrix;
	}
    public static int[][] multiplicar(int[][] A, int[][] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }
        int[][] C = new int[aRows][bColumns];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }


}

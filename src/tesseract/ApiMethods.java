package tesseract;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class ApiMethods {

    String jsonAES = null;
    String mainURL = "https://sandbox.tesseract.mx/api/v2";
    String  output;
    String AES;
    String piezasAES[];

    InputStreamReader in;
    BufferedReader br;

    public enum EncryptionMode{
        ENCRYPT,DECRYPT
    }



    public String  encryptAES(/*String user, String pass, String cve,*/EncryptionMode encMode, byte[] message){
        try {
            String enc = Base64.getEncoder().encodeToString(("HOISPnlmpsrVvdbsgAXl"+ ":" +"10SMvKvd0AAOyGyFuHDG9jr99VMsalkZIplgWIOQ").getBytes(StandardCharsets.UTF_8));
            jsonAES = "{\"transformation\": \"AES_CBC_PKCS5Padding\",\"mode\":\""+encMode+"\",\"message\":\""+message+"\",\"encode\":\"BASE64\"}";
            URL url = new URL(mainURL + "/institution/aes/"+"978877C478FB38D6D18FEF0CCE27311D9449DF754F75B5B4D167F672FDC18809");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return true;
                }
            });
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Basic " + enc);
            OutputStream os = con.getOutputStream();
            os.write(jsonAES.getBytes("UTF-8"));
            os.close();
            in = new InputStreamReader(con.getInputStream());
            br = new BufferedReader(in);
            while ((output = br.readLine()) != null) {
                piezasAES = output.split("\"");
                AES=piezasAES[3];
                System.out.println("AES = " + AES);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AES;
    }

    public String  dencryptAES(/*String user, String pass, String cve,*/EncryptionMode encMode, String message){
        try {
            String enc = Base64.getEncoder().encodeToString(("HOISPnlmpsrVvdbsgAXl"+ ":" +"10SMvKvd0AAOyGyFuHDG9jr99VMsalkZIplgWIOQ").getBytes(StandardCharsets.UTF_8));
            jsonAES = "{\"transformation\": \"AES_CBC_PKCS5Padding\",\"mode\":\""+encMode+"\",\"message\":\""+message+"\",\"encode\":\"BASE64\"}";
            URL url = new URL(mainURL + "/institution/aes/"+"978877C478FB38D6D18FEF0CCE27311D9449DF754F75B5B4D167F672FDC18809");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return true;
                }
            });
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Basic " + enc);
            OutputStream os = con.getOutputStream();
            os.write(jsonAES.getBytes("UTF-8"));
            os.close();
            in = new InputStreamReader(con.getInputStream());
            br = new BufferedReader(in);
            while ((output = br.readLine()) != null) {
                piezasAES = output.split("\"");
                AES=piezasAES[3];
                System.out.println("AES = " + AES);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AES;
    }


}

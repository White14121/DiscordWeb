import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/Backend")
public class Backend extends HttpServlet
{
	private final static String SEND_URL
	  ="https://discord.com/api/webhooks/1017070500217880596/nY4hYfpJN3_KXIUAqhbEAhApSb1fF-5MZF8fXXDgvbD0O2pZS9bfOkCQy75rwKek6k5y";
	
   public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
   throws ServletException
   {
      try{
         //フォームデータの取得
         String carname = request.getParameter("name");
         discord(carname);
         //サーブレットコンテキストの取得
         ServletContext sc = getServletContext();

         //リクエストの転送
         if(carname.length() != 0){
             sc.getRequestDispatcher("/Result.jsp")
               .forward(request, response);
         }
         else{
            sc.getRequestDispatcher("/error.html")
              .forward(request, response);
         }
      }
      catch(Exception e){    
         e.printStackTrace();
      }
   } 
	public static void discord(String message){  
	//JSONオブジェクトを生成する
	  String name = "Tired man";
	  String avaUrl = "https://webcg.ismcdn.jp/mwimgs/f/e/730wm/img_fe775f05cc82d6f4ba1ed4495ec399dd312688.jpg";
	  String json = null;
	try {
		json = setJsonObj( message, name, avaUrl);
	} catch (Exception e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}
	  
	  System.out.println("--------送信JSON--------");
	  System.out.println(json);
	  System.out.println("------------------------");
	  
	  //HTTPリクエストを送る
	  try {
		requestWeb(json, SEND_URL);
	} catch (Exception e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}
	  System.out.println("送信しました");
	}
	 
	void draw(){
	}
	 
	/**
	 * JSONオブジェクトを生成する
	 * <pre>
	 * Discordでよく使う最低限のJSONオブジェクトを生成します。
	 * </pre>
	 * @param content   : メッセージ
	 * @param username ： 送信者名
	 * @param avatarUrl : 送信者画像URL
	 **/
	static String setJsonObj(String content, String username, String avatarUrl){
		JSONObject json = new JSONObject();
	  
	  json.put("content",content);
	  
	  if( username != null && username.length() > 0 ){  
	    //送信者名が有効な場合
	    json.put("username", username);
	  }
	  
	  if( avatarUrl != null && avatarUrl.length() > 0  ){  
	    //送信者画像URLが有効な場合
	    json.put("avatar_url", avatarUrl);
	  }
	  
	  return json.toString();
	}
	 
	/**
	 * HTTPリクエストを送る
	 * <pre>
	 * 指定されたURLへ、HTTPリクエストでJSONオブジェクトを送ります。
	 * </pre>
	 * @param json : 送信電文（主文）
	 * @param url ： 送信先URL
	 **/
	static void requestWeb(String  json, String url){
	  
	  try{
	    //送信先URLを指定してHttpコネクションを作成する
	    URL sendUrl = new URL(url);    
	    HttpsURLConnection con = (HttpsURLConnection)sendUrl.openConnection();
	    
	    //リクエストヘッダをセット
	    con.addRequestProperty("Content-Type", 
	        "application/JSON; charset=utf-8");
	    con.addRequestProperty("User-Agent", "DiscordBot");
	    //URLを出力利用に指示
	    con.setDoOutput(true);
	    //要求方法にはPOSTを指示
	    con.setRequestMethod("POST");
	 
	    //要求を送信する
	    // POSTデータの長さを設定
	    con.setRequestProperty("Content-Length", String.valueOf(json.length()));
	    //リクエストのbodyにJSON文字列を書き込む
	    OutputStream stream = con.getOutputStream();
	    stream.write(json.getBytes("UTF-8"));
	    stream.flush();
	    stream.close();
	        
	    // HTTPレスポンスコード
	    final int status = con.getResponseCode();
	    if (status != HttpURLConnection.HTTP_OK 
	        && status != HttpURLConnection.HTTP_NO_CONTENT) {
	      //異常
	      System.out.println("error:" + status );
	    } 
	    
	    //後始末 
	    con.disconnect();
	    
	  } catch(MalformedURLException e  ){
	    //例外
	    e.printStackTrace();
	  } catch(IOException e){
	    //例外
	    e.printStackTrace();
	  }
	}
}

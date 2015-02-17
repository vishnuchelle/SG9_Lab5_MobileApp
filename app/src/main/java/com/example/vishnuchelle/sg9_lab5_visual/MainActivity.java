package com.example.vishnuchelle.sg9_lab5_visual;

import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button button;
//    private String param1 = "0";
//    private String param2 = "http://www.universeofsymbolism.com/images/lion-2.jpg";
    private EditText classET;
    private EditText urlET;
    private TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        classET = (EditText)findViewById(R.id.clas);
        urlET = (EditText)findViewById(R.id.url);
        resultTV = (TextView)findViewById(R.id.result);
        classET.setText("0");
        urlET.setText("http://www.universeofsymbolism.com/images/lion-2.jpg");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAsyncTask asyncTask = new MyAsyncTask(classET.getText()+"",urlET.getText()+"");
                asyncTask.execute();
            }
        });
    }

    private String getResponse(String p1,String p2){
        try {
            String address = "http://visual-recognition-nodejs-sg9.mybluemix.net/";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(address);

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("classifier", p1));
            pairs.add(new BasicNameValuePair("url", p2));
            post.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity());

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyAsyncTask extends AsyncTask<Void,Void,String>{

        private String p1 = "";
        private String p2 = "";
        public MyAsyncTask(String p1,String p2){
            super();
            this.p1 = p1;
            this.p2 = p2;
        }
        @Override
        protected String doInBackground(Void... params) {
            String response = getResponse(p1,p2);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement jsElement = parser.parse(response);
            System.out.println("--->" + response + "---");
//            JSONObject jsonObject
            return gson.toJson(jsElement);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultTV.setText(s);
        }
    }
}

package pneumax.mobilestoresystem.connected;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExecuteThreeParameter extends AsyncTask<String, Void, String> {


    private Context context;
    public ExecuteThreeParameter(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            okhttp3.RequestBody data = new FormBody.Builder()
                    .add( strings[0], strings[1])
                    .add( strings[2], strings[3])
                    .add( strings[4], strings[5])
                    .build();

            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            builder.url(strings[6]).post(data).build();
            okhttp3.Request request = builder.build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor())
                    .build();
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body().string();
            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
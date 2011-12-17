package com.idreamsky.ktouchread.yunpay;

import android.app.Activity;
import android.os.AsyncTask;

public final class AliyunPayAsyncTask extends AsyncTask<Object, Object, Object> {

    public interface Listener {
        void onPreExecute();

        void onPostExecute(String result);

        void onProgressUpdate(Object... values);

        void onCancelled();
    }

    private AliyunPayProxy pay;

    public AliyunPayAsyncTask(Activity activity) {
        pay = new AliyunPayProxy(activity);
    }

    public void unbind() {
        pay.unbind();
    }

    public boolean submit(String orderInfo) {
        pay.unbind();
        if (pay.bind()) {
            this.execute(orderInfo);
            return true;
        }

        return false;
    }

    @Override
    protected void onPreExecute() {
        if (listener != null)
            listener.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {

        final String orderInfo = (String) params[0];
        return pay.pay(orderInfo);
    }

    @Override
    protected void onPostExecute(Object result) {
        if (listener != null)
            listener.onPostExecute((String) result);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if (listener != null)
            listener.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        if (listener != null)
            listener.onCancelled();
    }

    private Listener listener = null;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

}
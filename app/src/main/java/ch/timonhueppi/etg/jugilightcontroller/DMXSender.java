package ch.timonhueppi.etg.jugilightcontroller;

import android.os.AsyncTask;

import ch.bildspur.artnet.ArtNetClient;

public class DMXSender extends AsyncTask<byte[], Integer, String> {

    public static String IPAddress = "127.0.0.1";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(byte[]... params) {
        byte[] dmxData = params[0];
        ArtNetClient artnet = new ArtNetClient();
        artnet.start();

        artnet.unicastDmx(IPAddress, 0, 0, dmxData);

        artnet.stop();

        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}

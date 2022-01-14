package ch.timonhueppi.etg.jugilightcontroller;

import android.os.AsyncTask;

import ch.bildspur.artnet.ArtNetClient;

/**
 * Handles all DMX Communication to Interface, asynchronous to the UI
 *
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/14
 */
public class DMXSender extends AsyncTask<byte[], Integer, String> {

    /**
     * static IP Address variable, universal access to all of the apps classes
     */
    public static String IPAddress = "127.0.0.1";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * this happens async to UI
     *
     * @param params data to work with; 512 dmx channels
     * @return message for post exec method
     */
    @Override
    protected String doInBackground(byte[]... params) {
        byte[] dmxData = params[0];
        //instantiates Artnet DMX client
        ArtNetClient artnet = new ArtNetClient();
        artnet.start();

        //sends dmx data
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

package ch.timonhueppi.etg.jugilightcontroller;

import android.os.AsyncTask;

import ch.bildspur.artnet.ArtNetClient;

public class DMXSender extends AsyncTask<byte[], Integer, String> {

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Do something like display a progress bar
    }

    // This is run in a background thread
    @Override
    protected String doInBackground(byte[]... params) {
        byte[] dmxData = params[0];
        ArtNetClient artnet = new ArtNetClient();
        artnet.start();

        // send data to localhost
        artnet.unicastDmx("192.168.10.140", 0, 0, dmxData);

        artnet.stop();

        publishProgress(1);

        return "this string is passed to onPostExecute";
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Do things like hide the progress bar or change a TextView
    }

}

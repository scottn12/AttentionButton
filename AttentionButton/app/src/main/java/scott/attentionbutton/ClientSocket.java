package scott.attentionbutton;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientSocket extends AsyncTask<Button, Void, Button> {

    private boolean succ = true;

    // Send Attention
    protected Button doInBackground(final Button... button) {
        final String SERVER_ADDR = "<SERVER IP ADDRESS>";
        final int PORT = <SERVER PORT NUMBER>;
        // Create Socket
        Socket socket = new Socket();
        try {
            System.out.println("Creating Socket...");
            InetAddress addr = InetAddress.getByName(SERVER_ADDR);
            socket.connect(new InetSocketAddress(addr, PORT), 3000); // Try to connect, timeout after 3 sec otherwise
        }
        catch (Exception e) {
            Log.e("Exception", "socket creation failed: " + e.toString());
            succ = false;
            return button[0]; // Don't try to send if socket fails
        }
        // Send Message
        try {
            System.out.println("Sending Message...");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeChars("gimme attention D:<");
            out.flush();
            System.out.println("Message sent!");
            socket.close();
        }
        catch (Exception e) {
            Log.e("Exception", "send failed: " + e.toString());
            succ = false;
        }
        return button[0];
    }

    // Change button visuals after sending attention
    @Override
    protected void onPostExecute(final Button button) {
        AttentionButton.changeButton(button, succ);
    }
}
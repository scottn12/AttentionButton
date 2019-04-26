package scott.attentionbutton;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;

public class AttentionButton extends AppCompatActivity {

    private static final String LIGHT_RED = "#ffff4444"; // Default
    private static final String CYAN = "#ff33b5e5"; // Successful
    private static final String DARK_RED = "#ffcc0000"; // Failure

    private static Drawable drawButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        final Button button = findViewById(R.id.button);
        drawButton = button.getBackground(); // Get drawable to change background later
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Disable button & change button to sending status
                button.setEnabled(false);
                button.setText(R.string.attention_sending);
                drawButton.setColorFilter(Color.parseColor(CYAN), PorterDuff.Mode.SRC_ATOP);
                new ClientSocket().execute(button); // Send Attention
            } // End onClick
        });
    }

    // Change button color/message accordingly
    public static void changeButton(final Button button, boolean succ) {
        if (succ) {
            button.setText(R.string.attention_sent);
        }
        else {
            button.setText(R.string.attention_failed);
            drawButton.setColorFilter(Color.parseColor(DARK_RED), PorterDuff.Mode.SRC_ATOP);
        }

        // Wait 3 Second and Reset Attention Status
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(R.string.attention_needed);
                drawButton.setColorFilter(Color.parseColor(LIGHT_RED), PorterDuff.Mode.SRC_ATOP);
                button.setEnabled(true);
            }
        }, 3000);
    }
}
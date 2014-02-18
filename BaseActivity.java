package wingman.dodger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MainGamePanel(this));
        Log.d("loglog", "view added");
    }

    @Override
    protected void onDestroy() {
        Log.d("loglog", "destorying");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("loglog", "view added");
        super.onStop();
    }


}

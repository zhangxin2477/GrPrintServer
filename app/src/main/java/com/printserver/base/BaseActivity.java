package com.printserver.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * Created by zhangxin on 2015/9/2.
 */
public class BaseActivity extends Activity {

    protected void onCreate(Bundle keyInt) {
        super.onCreate(keyInt);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //BaseHelp.ShowDialog(this, "您确定退出吗？", 0);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }

        if (keyCode==KeyEvent.KEYCODE_HOME){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void CloseActivity(){
        this.finish();
        this.onDestroy();
    }

}

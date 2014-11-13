package id11303563.asmith.dnd;

import android.app.Activity;

/**
 * @author Alex
 * This class acts as a parent class between CreateMinionActivity and CreateCharacterActivity
 * in order to get the current application data.
 */

public class GameActivity extends Activity {
	protected GameApplication getPlayerApplication() {
		GameApplication pa = (GameApplication)getApplication();
		return pa;
	}
}

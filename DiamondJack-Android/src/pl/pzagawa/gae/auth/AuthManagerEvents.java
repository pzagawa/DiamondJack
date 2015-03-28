package pl.pzagawa.gae.auth;

import android.content.Intent;

public interface AuthManagerEvents
{
	void onSuccess();
	void onFailure(AuthManagerError error, String description);
	void onUserInput(Intent intent);
}

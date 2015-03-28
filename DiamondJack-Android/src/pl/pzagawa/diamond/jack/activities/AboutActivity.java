package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.R;
import pl.pzagawa.diamond.jack.Utils;
import pl.pzagawa.diamond.jack.ui.CommonActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity
	extends CommonActivity
{
	private TextView aboutTextLabelLine1; 

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
		aboutTextLabelLine1 = (TextView)this.findViewById(R.id.labelAboutLine1);
    }

	@Override
	protected void onStart()
	{
		super.onStart();		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();

		//setup content
		final String appVersion = Utils.getAppVersionName(AboutActivity.this);
		
		//name and version
		final String line1 = getString(R.string.app_name) + "\n" + getString(R.string.about_text_version) + " " + appVersion;
		
		aboutTextLabelLine1.setText(line1);
	}
	
}

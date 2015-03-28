package pl.pzagawa.diamond.jack.activities;

import pl.pzagawa.diamond.jack.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdaterListItem
{
	private View view;

	private ProgressBar progressBar;
	private ImageView imageView;
	private TextView textView;
	
	public UpdaterListItem(LayoutInflater inflater, String text)
	{
		this.view = inflater.inflate(R.layout.activity_updater_list_item, null);

		progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		imageView = (ImageView)view.findViewById(R.id.imageView);
		textView = (TextView)view.findViewById(R.id.textView);
		
		progressBar.setVisibility(View.VISIBLE);
		imageView.setVisibility(View.GONE);
		textView.setText(text);		
	}

	public View getView()
	{
		return view;
	}

	public void done()
	{
		progressBar.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);
	}
	
}

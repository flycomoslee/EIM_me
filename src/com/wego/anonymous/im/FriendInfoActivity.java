package com.wego.anonymous.im;

import com.wego.anonymous.activity.ActivitySupport;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import csdn.shimiso.eim.R;

public class FriendInfoActivity extends ActivitySupport {
	private Button titleBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_info);
		init();
	}

	private void init() {
		getEimApplication().addActivity(this);
		titleBack = (Button) findViewById(R.id.title_back);
		titleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}

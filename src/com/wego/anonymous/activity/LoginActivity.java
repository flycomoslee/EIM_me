package com.wego.anonymous.activity;

import com.wego.anonymous.manager.XmppConnectionManager;
import com.wego.anonymous.model.LoginConfig;
import com.wego.anonymous.task.LoginTask;
import com.wego.anonymous.util.StringUtil;
import com.wego.anonymous.util.ValidateUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import csdn.shimiso.eim.R;

/**
 * 
 * ��¼.
 * 
 */
public class LoginActivity extends ActivitySupport {
	private EditText edt_username, edt_pwd;
	private CheckBox rememberCb, autologinCb, novisibleCb;
	private Button btn_login = null;
	private LoginConfig loginConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}

	protected void init() {
		try {
			loginConfig = getLoginConfig();
			
			initXmppServer();
			
			// ���Ϊ�Զ���¼
			if (loginConfig.isAutoLogin()) {
				LoginTask loginTask = new LoginTask(LoginActivity.this, loginConfig);
				loginTask.execute();
			}
			edt_username = (EditText) findViewById(R.id.ui_username_input);
			edt_pwd = (EditText) findViewById(R.id.ui_password_input);
			rememberCb = (CheckBox) findViewById(R.id.remember);
			autologinCb = (CheckBox) findViewById(R.id.autologin);
			novisibleCb = (CheckBox) findViewById(R.id.novisible);
			btn_login = (Button) findViewById(R.id.ui_login_btn);

			// ��ʼ���������Ĭ��״̬
			edt_username.setText(null != loginConfig.getUsername() ? loginConfig.getUsername() : "");
			edt_pwd.setText(null != loginConfig.getPassword() ? loginConfig.getPassword() : "");
			rememberCb.setChecked(loginConfig.isRemember());
			rememberCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (!isChecked)
						autologinCb.setChecked(false);
				}
			});
			autologinCb.setChecked(loginConfig.isAutoLogin());
			novisibleCb.setChecked(loginConfig.isNovisible());

			btn_login.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (checkData() && validateInternet()) {
						String username = edt_username.getText().toString();
						String password = edt_pwd.getText().toString();

						// �ȼ�¼�¸������Ŀǰ״̬,��¼�ɹ���ű���
						loginConfig.setPassword(password);
						loginConfig.setUsername(username);
						loginConfig.setRemember(rememberCb.isChecked());
						loginConfig.setAutoLogin(autologinCb.isChecked());
						loginConfig.setNovisible(novisibleCb.isChecked());

						LoginTask loginTask = new LoginTask(LoginActivity.this,
								loginConfig);
						loginTask.execute();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initXmppServer() {
//		String xmppHost = StringUtil.doEmpty("115.28.163.135");
		String xmppHost = StringUtil.doEmpty("10.10.10.139");
		loginConfig.setXmppHost(xmppHost);
		XmppConnectionManager.getInstance().init(loginConfig);
		LoginActivity.this.saveLoginConfig(loginConfig);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// У��SD��
		checkMemoryCard();
		// �������Ͱ汾
		validateInternet();
		// ��ʼ��xmpp����
		XmppConnectionManager.getInstance().init(loginConfig);
	}

	private boolean checkData() {
		boolean checked = false;
		checked = (!ValidateUtil.isEmpty(edt_username, "��¼��") && !ValidateUtil.isEmpty(edt_pwd, "����"));
		return checked;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final EditText xmppHostText = new EditText(context);
		xmppHostText.setText(loginConfig.getXmppHost());
		switch (item.getItemId()) {
		case R.id.menu_login_set:
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("����������")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(xmppHostText)
					.setMessage("�����÷�����IP��ַ")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();// ȡ��������
								}
							}).create().show();

			break;
		case R.id.menu_relogin:
			Intent intent = new Intent();
			intent.setClass(context, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_exit:
			isExit();
			break;
		}
		return true;

	}

	@Override
	public void onBackPressed() {
		isExit();
	}
}

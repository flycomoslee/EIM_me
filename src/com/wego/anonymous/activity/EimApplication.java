package com.wego.anonymous.activity;

import java.util.LinkedList;
import java.util.List;

import com.wego.anonymous.manager.XmppConnectionManager;

import android.app.Activity;
import android.app.Application;

/**
 * 
 * �������˳�Ӧ��.
 * 
 * @author shimiso
 */
public class EimApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();

	// ����Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		XmppConnectionManager.getInstance().disconnect();
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
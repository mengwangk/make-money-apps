package com.mymobkit.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.mymobkit.R;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.ui.activity.VideoStreamingActivity;
import com.mymobkit.ui.base.BaseFragment;
import com.mymobkit.ui.fragment.CameraSettingsFragment;
import com.mymobkit.ui.fragment.ControlPanelFragment;
import com.mymobkit.ui.fragment.ControlPanelSettingsFragment;
import com.mymobkit.ui.fragment.FunctionViewFragment;
import com.mymobkit.ui.fragment.HelpFragment;
import com.mymobkit.ui.fragment.MessagingFragment;
import com.mymobkit.ui.fragment.SurveillanceSettingsFragment;

/**
 * Application specific configuration class.
 * 
 */
public final class AppConfig {

	/**
	 * Logging tag to easily differentiate the log entries for this application.
	 */
	public static final String LOG_TAG_APP = MyMobKitApp.getContext().getString(R.string.app_name);

	/**
	 * Parameter used to indicate the function type.
	 */
	public static final String FUNCTION_PARAM = "function";

	/**
	 * Parameter used to indicate the HTTP port
	 */
	public static final String HTTP_LISTENING_PORT_PARAM = "http_listening_port";

	
	public static final boolean DELIVER_DIAGNOSTIC_DATA = false;
	
	/***
	 * Menu list setup.
	 * 
	 */
	public static enum MenuListSetup {

		// Sub menu items
		WEB_CONTROL_PANEL(50, 1, MyMobKitApp.getContext().getString(R.string.label_control_panel), R.drawable.ic_control_panel, IntentType.FRAGMENT, null),
		MESSAGING(51, 1, MyMobKitApp.getContext().getString(R.string.label_messaging), R.drawable.ic_messaging, IntentType.FRAGMENT, null),
		VIDEO_STREAMING(52, 1, MyMobKitApp.getContext().getString(R.string.label_video_streaming), R.drawable.tab_group_camera_selected, IntentType.ACTIVITY, null, 52),
		CAMERA_MONITOR(53, 1, MyMobKitApp.getContext().getString(R.string.label_camera_monitor), R.drawable.ic_my_cameras, IntentType.FRAGMENT, null),
		CAMERA_SETTINGS(54, 1, MyMobKitApp.getContext().getString(R.string.label_title_settings), R.drawable.tab_group_settings_selected, IntentType.FRAGMENT, null),
		FACE_DETECTION(55, 1, MyMobKitApp.getContext().getString(R.string.label_face_detection), R.drawable.ic_face_detection, IntentType.ACTIVITY, null),
		MESSAGE_GATEWAY(60, 1, MyMobKitApp.getContext().getString(R.string.label_messaging_service), R.drawable.ic_face_detection, IntentType.FRAGMENT, null),
		
		// OCR(53, 1, MyMobKitApp.getContext().getString(R.string.label_ocr),
		// R.drawable.ic_ocr, IntentType.FRAGMENT, null),

		// Root menu items
		HOME(0, 0, MyMobKitApp.getContext().getString(R.string.label_title_home), R.drawable.tab_group_home_selected, IntentType.FRAGMENT, null),

		UTILITIES(10, 0, MyMobKitApp.getContext().getString(R.string.label_title_utilities), R.drawable.tab_group_utilities_selected, IntentType.FRAGMENT,
				new ArrayList<MenuListSetup>(Arrays.asList(WEB_CONTROL_PANEL, MESSAGING))
		),

		CAMERA(20, 0, MyMobKitApp.getContext().getString(R.string.label_title_camera), R.drawable.ic_surveillance, IntentType.FRAGMENT,
				new ArrayList<MenuListSetup>(Arrays.asList(VIDEO_STREAMING, CAMERA_MONITOR, CAMERA_SETTINGS))
		),

		FEEDBACK(30, 0, MyMobKitApp.getContext().getString(R.string.label_title_feedback), R.drawable.tab_group_feedback_selected, IntentType.FRAGMENT, null),

		HELP(40, 0, MyMobKitApp.getContext().getString(R.string.label_title_help), R.drawable.tab_group_help_selected, IntentType.FRAGMENT, null);

		private int code;
		private String title;
		private int icon;
		private List<MenuListSetup> subMenu;
		private int level;
		private IntentType intentType;
		private int resultCode;

		/**
		 * Constructor.
		 * 
		 * @param code
		 * @param level
		 * @param title
		 * @param icon
		 * @param intentType
		 * @param subMenu
		 */
		private MenuListSetup(final int code, final int level, final String title, final int icon,
				final IntentType intentType, final List<MenuListSetup> subMenu) {
			this.code = code;
			this.level = level;
			this.title = title;
			this.icon = icon;
			this.subMenu = subMenu;
			this.intentType = intentType;
			this.resultCode = 0;
		}

		private MenuListSetup(final int code, final int level, final String title, final int icon,
				final IntentType intentType, final List<MenuListSetup> subMenu, int resultCode) {
			this.code = code;
			this.level = level;
			this.title = title;
			this.icon = icon;
			this.subMenu = subMenu;
			this.intentType = intentType;
			this.resultCode = resultCode;
		}

		public String getTitle() {
			return this.title;
		}

		public int getIcon() {
			return this.icon;
		}

		public int getCode() {
			return this.code;
		}

		public int getLevel() {
			return this.level;
		}

		public int getResultCode() {
			return this.resultCode;
		}

		public List<MenuListSetup> getSubMenu() {
			return this.subMenu;
		}

		public IntentType getIntentType() {
			return this.intentType;
		}

		private static final SparseArray<MenuListSetup> codeToEnum = new SparseArray<MenuListSetup>(4);
		static {
			for (MenuListSetup ft : values())
				codeToEnum.put(ft.getCode(), ft);
		}

		public static MenuListSetup fromCode(final int code) {
			return codeToEnum.get(code);
		}

		public static Intent createActivityIntent(final MenuListSetup f) {
			if (f.getIntentType() != IntentType.ACTIVITY)
				return null;
			Intent intent = null;
			switch (f) {

			case VIDEO_STREAMING:
				intent = new Intent(MyMobKitApp.getContext(), VideoStreamingActivity.class);
				break;
			default:
				break;
			}
			return intent;
		}

		public static BaseFragment createFragment(final MenuListSetup m) {

			if (m.getIntentType() != IntentType.FRAGMENT)
				return null;

			BaseFragment fragment = null;
			switch (m) {
			case WEB_CONTROL_PANEL:
				fragment = ControlPanelSettingsFragment.newInstance();
				break;
			case MESSAGING:
				fragment = MessagingFragment.newInstance();
				break;
			case HOME:
				List<MenuListSetup> homeMenuList = getRootMenu();
				fragment = FunctionViewFragment.newInstance(homeMenuList);
				break;
			case UTILITIES:
				List<MenuListSetup> utilitiesMenuList = new ArrayList<MenuListSetup>(1);
				utilitiesMenuList.add(UTILITIES);
				fragment = FunctionViewFragment.newInstance(utilitiesMenuList);
				break;
			case CAMERA:
				List<MenuListSetup> cameraMenuList = new ArrayList<MenuListSetup>(1);
				cameraMenuList.add(CAMERA);
				fragment = FunctionViewFragment.newInstance(cameraMenuList);
				break;
			case CAMERA_MONITOR:
				fragment = HelpFragment.newInstance();
				break;
			case CAMERA_SETTINGS:
				fragment = SurveillanceSettingsFragment.newInstance();
				break;
			case FEEDBACK:
				fragment = HelpFragment.newInstance();
				break;
			case HELP:
				fragment = HelpFragment.newInstance();
				break;

			default:
				break;
			}
			return fragment;
		}

		public static List<MenuListSetup> getRootMenu() {
			List<MenuListSetup> rootMenuList = new ArrayList<MenuListSetup>(1);
			for (MenuListSetup m : values()) {
				if (m.level == 0) {
					rootMenuList.add(m);
				}
			}
			return rootMenuList;
		}
	}

	/**
	 * The intent type to create based on the function.
	 * 
	 */
	public static enum IntentType {
		ACTIVITY,
		FRAGMENT
	}
}

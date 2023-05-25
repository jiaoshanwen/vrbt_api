package com.sinontech.tools.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WrbtInterfaceInfo {
	public static String APPKEY = "3000011668";
	public static String APPSCREAT = "9474D90B53878140";
	public static String CHANNEL_ID = "3000011668";
	public static final String BASEURL = "http://api.10155.com";
	public static final String RESOURCEBASE = "https://listen.10155.com/listener";
}

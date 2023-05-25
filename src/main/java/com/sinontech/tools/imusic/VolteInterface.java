package com.sinontech.tools.imusic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.sinontech.tools.common.PageData;
import com.sinontech.tools.hn.ShopInterface;

import net.sf.json.JSONObject;

/**
 * 爱音乐能力平台接口
 * @author guchao
 *
 */
public class VolteInterface {
	private static final Logger logger = Logger.getLogger(VolteInterface.class);

	
	/**
	* @Title: queryaccountinfo
	* @Description: TODO(用户信息查询)
	* @param phonenumber 号码 必填
	* @return String    返回类型
	* res_code	String	是	返回参数码
	  res_message	String	否	返回参数描述
	  phonenumber	String	是	手机号
	  chargeType	Integer	是	付费类型 -1：未知付费类型 1：预付费； 0：后付费
	  ringStatus	Integer	是	Volte彩铃状态   1=开户 2=销户 
	  openTime	String	否	Volte彩铃开户时间，格式：yyyy-MM-dd HH:mm:ss
	  lastUpdateTime	String	否	最后一次Volte彩铃状态更新时间，格式：yyyy-MM-dd HH:mm:ss
	  userStatus	Integer	否	用户状态[ 1：正常； 2：单停； 3：双停； 4：申请停机]
	*/
	public static String queryaccountinfo(String phonenumber,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/account/","queryaccountinfo",".json",header);
	}
	
	/**
	 * 包月产品订购关系查询接口
	 * @param phonenumber
	 * @param packageId
	 * @param header {}
	 * @return
	 * package_id：套餐id
	 * count_down_num：剩余次数,当次数为－1时，表示无次数限制，大于0时，表示剩余次数
	 * order_time：订购时间,如2013-09-05 00:00:00
	 * unsubscribe_time：退订时间,如2013-09-13 00:00:00
	 * status：套餐状态, 0未退订, 1等待, 2退订
	 */
	public static String querypackagelist(String phonenumber,String packageId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", phonenumber));
		nvps.add(new BasicNameValuePair("package_id", packageId));
		nvps.add(new BasicNameValuePair("is_count_down_num", "1"));
		return HttpUtils.httpGetTool(nvps,"/v2/package/packageservice/","querypackagelist",".json",header);
	}
	/**
	 * 一键开通视频彩铃订购包月产品(音乐盒代计费)下单
	 * @param phonenumber
	 * @param packageId
	 * @param randomKey
	 * @return
	 * res_code：返回结果码
	 * res_message：返回结果描述
	 * order_no：订单号，用户后续查询订单状态下单成功时，一定返回
	 */
	public static String async_openaccount_orderpackage_bycrbt(String phonenumber,String packageId,String randomKey,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("packageId", packageId));
		nvps.add(new BasicNameValuePair("randomKey", randomKey));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/onekey/","async_openaccount_orderpackage_bycrbt",".json",header);
	}
	/**
	 * 一键开通视频彩铃订购包月产品(音乐盒代计费)验证码下发
	 * @param phonenumber
	 * @param packageId
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String async_open_order_sendrandom(String phonenumber,String packageId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("packageId", packageId));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/onekey/","async_open_order_sendrandom",".json",header);
	}
	/**
	 * 视频彩铃一键接口订单详情查询
	 * @param orderNo
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * order_no：订单号
	 * mdn：用户号码
	 * package_id：包月产品id
	 * state：订单状态 0:新建 1:处理中 2:处理完成
	 * open_vrbt_flag：视频彩铃开户结果0:未执行 1:开户成功  2:开户失败 3:结果确认中
	 * open_vrbt_code：视频彩铃开户返回码
	 * open_vrbt_desc：视频彩铃开户描述
	 * order_package_flag：包月产品订购结果 0:未执行 1:订购成功  2:订购失败  3:结果确认中
	 * order_package_code：包月产品订购返回码
	 * order_package_desc：包月产品订购描述
	 */
	public static String query_order(String orderNo,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("orderNo", orderNo));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/onekey/","query_order",".json",header);
	}
	/**
	 * 包月产品退订接口
	 * @param phonenumber
	 * @param packageId
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String unsubscribebyemp(String phonenumber,String packageId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", phonenumber));
		nvps.add(new BasicNameValuePair("package_id", packageId));
		return HttpUtils.httpPostTool(nvps,"/v2/package/packageservice/","unsubscribebyemp",".json",header);
	}
	/**
	 * 验证码下发
	 * @param phonenumber
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String sendrandom(String phonenumber,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", phonenumber));
		return HttpUtils.httpPostTool(nvps,"/v2/music/crbtservice/","sendrandom",".json",header);
	}
	/**
	 * 视频彩铃销户（需短信验证码）
	 * @param phonenumber
	 * @param randomKey
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * order_no:销户订单号
	 */
	public static String closeaccountwithrandom(String phonenumber,String randomKey,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("randomKey", randomKey));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/account/","closeaccountwithrandom",".json",header);
	}
	/**
	 * 彩铃开户验证码下发接口
	 * @param phonenumber
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String opencrbt_sendrandomkey(String phonenumber,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", phonenumber));
		return HttpUtils.httpPostTool(nvps,"/v3/crbtservice/","opencrbt_sendrandomkey",".json",header);
	}
	/**
	 * 视频彩铃开户（需短信验证码）
	 * @param phonenumber
	 * @param randomKey
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * order_no:销户订单号
	 */
	public static String openaccountwithrandom(String phonenumber,String randomKey,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("randomKey", randomKey));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/account/","openaccountwithrandom",".json",header);
	}
	/**
	 * 退订视频音乐盒
	 * @param phonenumber
	 * @param boxCode
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String delbox(String phonenumber,String boxCode,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("boxCode", boxCode));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringtone/","delbox",".json",header);
	}
	
	/**
	 * 浏览铃音库铃音接口
	 * @param phonenumber
	 * @param toneType 铃音类型：1 单首铃音,2 铃音盒,3 所有
	 * @param startNum 开始记录数
	 * @param endNum 结束记录数
	 * @param header
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * toneList:铃音库铃音列表:List<PersonLibItem>
	 * PersonLibItem内字段
	 * ringId：铃音/音乐盒编码
	 * name：铃音/音乐盒名称
	 * price：铃音/音乐盒价格，单位为分
	 * validDate：铃音/音乐盒有效期
	 * singer：歌手名称
	 * type：类型：0：铃音，1：音乐盒
	 * @return
	 */
	public static String browsepersonallib(String phonenumber,String toneType,String startNum,String endNum,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("toneType", toneType));
		nvps.add(new BasicNameValuePair("startNum", startNum));
		nvps.add(new BasicNameValuePair("endNum", endNum));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringtone/","browsepersonallib",".json",header);
	}
	/**
	 * 使用包月产品权益免费订购视频铃音接口
	 * 前置流程:用户已订购视频彩铃所属的包月产品
	 * @param phoneNumber
	 * @param toneCode
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * packageId ：视频彩铃所属的包月产品 ID 
	 */
	public static String addtonefreeonproduct(String phoneNumber,String toneCode,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phoneNumber));
		nvps.add(new BasicNameValuePair("toneCode", toneCode));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringtone/","addtonefreeonproduct",".json",header);
	}
	/**
	 * 退订视频铃音接口
	 * @param phonenumber
	 * @param toneCode
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String delring(String phonenumber,String toneCode,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("toneCode", toneCode));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringtone/","delring",".json",header);
	}
	/**
	 * 增加视频铃音设置接口
	 * @param phonenumber
	 * @param setType 铃音播放对象设置类型： 1：个人默认铃音设置（适用于全部主叫号码） 2：特定主叫号码组铃音设置
	 * @param callerGroupId 当settype=1时，空,当settype=2时，填特定主叫号码组ID
	 * @param toneCodes 视频铃音或视频音乐盒编码，多个用逗号隔开
	 * @param timeType 分时属性，默认为1： 1：该设置无分时播放属性 2：该设置具备分时播放属性
	 *  说明：2表示设置每天指定开始时间startTime到结束时间endTime之间的生效时间，其中startTime、endTime取值范围为00:00:00到23:59:59，startTime < endTime。
	 * @param startTime 铃音设置每日生效开始时间，timeType为1：0；timeType为2hh:mm:ss， 如10:00:00；
	 * @param endTime 铃音设置每日生效结束时间，timeType为1：0；timeType为2：hh:mm:ss， 如17:59:59；
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * settingId:设置成功时返回的铃音设置id
	 */
	public static String addringset(String phonenumber,String setType,String callerGroupId,
			String toneCodes,String timeType,String startTime,String endTime,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("setType", setType));
		nvps.add(new BasicNameValuePair("callerGroupId", callerGroupId));
		nvps.add(new BasicNameValuePair("toneCodes", toneCodes));
		nvps.add(new BasicNameValuePair("timeType", timeType));
		nvps.add(new BasicNameValuePair("startTime", startTime));
		nvps.add(new BasicNameValuePair("endTime", endTime));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","addringset",".json",header);
	}
	/**
	 * 增加视频铃音设置接口
	 * @param phonenumber
	 * @param setType 铃音播放对象设置类型： 1：个人默认铃音设置（适用于全部主叫号码） 2：特定主叫号码组铃音设置
	 * @param callerGroupId 当settype=1时，空,当settype=2时，填特定主叫号码组ID
	 * @param toneCodes 视频铃音或视频音乐盒编码，数组的形式[111,222]
	 * @param timeType 分时属性，默认为1： 1：该设置无分时播放属性 2：该设置具备分时播放属性
	 *  说明：2表示设置每天指定开始时间startTime到结束时间endTime之间的生效时间，其中startTime、endTime取值范围为00:00:00到23:59:59，startTime < endTime。
	 * @param startTime 铃音设置每日生效开始时间，timeType为1：0；timeType为2hh:mm:ss， 如10:00:00；
	 * @param endTime 铃音设置每日生效结束时间，timeType为1：0；timeType为2：hh:mm:ss， 如17:59:59；
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String updateringset(String phonenumber,String settingId,String setType,String callerGroupId,
			String toneCodes,String timeType,String startTime,String endTime,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("settingId", settingId));
		nvps.add(new BasicNameValuePair("setType", setType));
		nvps.add(new BasicNameValuePair("callerGroupId", callerGroupId));
		nvps.add(new BasicNameValuePair("toneCodes", toneCodes));
		nvps.add(new BasicNameValuePair("timeType", timeType));
		nvps.add(new BasicNameValuePair("startTime", startTime));
		nvps.add(new BasicNameValuePair("endTime", endTime));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","updateringset",".json",header);
	}
	/**
	 * 删除视频铃音设置接口
	 * @param phonenumber
	 * @param settingId 铃音设置id号
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String delringset(String phonenumber,String settingId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("settingId", settingId));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","delringset",".json",header);
	}
	/**
	 * 查询视频铃音设置接口
	 * @param phonenumber
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * ringsetlist:铃音设置列表 List<SetItem>
	 * SetItem字段：
	 * settingId：铃音设置id号
	 * setType：铃音播放对象设置类型：1：个人默认铃音设置（适用于全部主叫号码）2：特定主叫号码组铃音设置
	 * callerGroupId：当settype=1时，空；当settype=2时，填特定主叫号码组ID
	 * toneCodes：视频铃音或视频音乐盒编码；String[]
	 * timeType：分时属性，默认为1：1：该设置无分时播放属性 ;2：该设置具备分时播放属性
	 * 说明：2表示设置每天指定开始时间startTime到结束时间endTime之间的生效时间，其中startTime、endTime取值范围为00:00:00到23:59:59，startTime < endTime。
	 * startTime:铃音设置每日生效开始时间，timeType为1：0；timeType为2： hh:mm:ss， 如10:00:00；
	 * endTime:铃音设置每日生效结束时间，timeType为1：0；timeType为2： hh:mm:ss， 如17:59:59；
	 */
	public static String queryringset(String phonenumber,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","queryringset",".json",header);
	}
	/**
	 * 设置视频铃音播放模式接口
	 * @param phonenumber
	 * @param playMode 0：设置固定播放 1：设置随机播放
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 */
	public static String setplaymode(String phonenumber,String playMode,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		nvps.add(new BasicNameValuePair("playMode", playMode));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","setplaymode",".json",header);
	}
	/**
	 * 查询视频铃音播放模式接口
	 * @param phonenumber
	 * @param header
	 * @return
	 * res_code：执行结果代码 0000 成功
	 * res_message：执行结果描述
	 * playMode:0：固定播放 1：随机播放
	 */
	public static String queryplaymode(String phonenumber,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("phoneNumber", phonenumber));
		return HttpUtils.httpPostTool(nvps,"/v3/vrbtservice/ringset/","queryplaymode",".json",header);
	}
	/**
	 * 视频彩铃信息查询接口
	 * @param resourceId 视频彩铃id或者资源id 
	 * @param header
	 * @return
	 * code:执行结果代码 0000成功， 其它失败。
	 * message:执行结果描述 
	 * data:返回的json数据
	 * video_name :视频名 
	 * actor_name :歌手名 
	 * resource_id :资源id 
	 * package_id :视频所属的包月产品id 
	 * ring_id :彩铃id 
	 * price :下载价格(单位:分)
	 * expiration_date :产品有效期 yyyy-mm-dd 
	 * imageList :图片数组
	 * 		image字段
	 * 			path ：图片地址
	 * 			format ：图片格式
	 * 			type ：类型： 1:封面 2：微信分享封面 
	 * fileList ：视频数组
	 * 		file字段：
	 * 			type ：文件类型： 1=彩铃(订购) 2=彩铃(试播) 	
	 * 			quality：质量 1=9:16竖屏（540*960） 2=16:9横屏（640*360） 3=4:3横屏（640*480） 4=1:1横屏（640*640） 5=横屏（1024*768） 6=横屏（1440*1080） 
						    7=竖屏（480*640） 8=竖屏（768*1024）9=竖屏（1080*1440） 10=横屏（1280*720） 11=横屏（1920*1080） 12=横屏（960*540） 13=竖屏（720*1280） 14=竖屏（1080*1920）
			    file_path ：文件路径 
			    file_format ：文件后缀 
			    video_frame_rate ：帧率
			    video_format：视频格式 
			    video_bitrate ：视频比特率 
			    audio_rate ：音频采样率
			    audio_format：音频格式 
			    audio_bitrate ：音频比特率 
			    channel ：音频声道： 1:单声道 2:立体声
			    file_size ：文件大小(字节)
			    duration ：播放时长(毫秒) 
	 */			
	public static String query(String resourceId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("resourceId", resourceId));
		return HttpUtils.httpPostTool(nvps,"/v3/qkservice/video/","query",".json",header);
	}
	/**
	 * EMP计费发起接口（调用此接口，如果返回成功且fee_type=1表示已经扣费成功，用户不会收到验证码，如果fee_type=2 表示EMP计费，用户会收到订购验证码）
	 * @param mobile
	 * @param resourceId
	 * @param header
	 * @return 
	 * res_code 执行结果代码
	 * res_message 执行结果描述
	 * fee_type 计费类型 1：铃音盒代扣费 2：emp计费 ，
	 */
	public static String emplanunched(String mobile,String resourceId,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", mobile));
		nvps.add(new BasicNameValuePair("package_id", resourceId));
		nvps.add(new BasicNameValuePair("column", ""));
		return HttpUtils.httpPostTool(nvps,"/v2/package/packageservice/","emplanunched",".json",header);
	}
	/**
	 * 包月产品订购（EMP计费确认）接口
	 * @param mobile
	 * @param resourceId
	 * @param randomKey 订购验证码
	 * @param header
	 * @return
	 */
	public static String subscribebyemp(String mobile,String resourceId,String randomKey,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mdn", mobile));
		nvps.add(new BasicNameValuePair("package_id", resourceId));
		nvps.add(new BasicNameValuePair("random_key", randomKey));
		nvps.add(new BasicNameValuePair("column", ""));
		return HttpUtils.httpPostTool(nvps,"/v2/package/packageservice/","subscribebyemp",".json",header);
	}
	/**
	 * 第三方会员直冲权益领取接口
	 * @param user_id 用户ID
	 * @param user_id_type用户ID类型 1： 电信手机号码
	 * @param partner 领取会员的合作方类型 1：QQ音乐
									2：网易云音乐
									3：酷狗音乐
									4：酷我音乐
									5：蜻蜓，
									6：懒人听书
									7：喜马拉雅
									8：优酷
									9：爱奇艺
									10：芒果TV
									11：腾讯视频
	 * @param header
	 * @return
	 */
	public static String receivePartenerVip(String user_id,String user_id_type,String partner,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", user_id));
		nvps.add(new BasicNameValuePair("user_id_type", user_id_type));
		nvps.add(new BasicNameValuePair("partner", partner));
		nvps.add(new BasicNameValuePair("partner_id", ""));
		return HttpUtils.httpPostTool(nvps,"/v3/payservice/thirdpartymember/","receive_partener_vip",".json",header);
	}
	/**
	 * 第三方会员直冲用户有效权益查询接口
	 * @param user_id 用户ID
	 * @param user_id_type 用户ID类型 1： 电信手机号码
	 * @param header
	 * @return
	 */
	public static String getValidByUserid(String user_id,String user_id_type,PageData header){
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", user_id));
		nvps.add(new BasicNameValuePair("user_id_type", user_id_type));
		return HttpUtils.httpPostTool(nvps,"/v3/payservice/thirdpartymember/query/order/","get_valid_by_userid",".json",header);
	}
	
	/**
	 * 
	* @Title: openOrderLanched
	* @Description: TODO(调用该接口生成订单，并返回计费认证 H5 页面地址，
	* 前端引导用户在该计费认证页面 完成计费。 一键开户+订购指的是开通彩铃功能(音频或视频)，并且订购指定包月产品。)
	* @param @param mobile用户号码,非必填，若前端未知用户号码，用户访问计费确认页面时用户可自行输入
	* @param @param productId 包月产品ID
	* @param @param returnUrl 计费认证页面操作后的返回地址
	* @param @param verifyType 校验类型
								0：免密登录 1：短信验证码。
								若接入方能判断用户当前是使用3G/4G/5G等移动数据方式上网时，该参数传0；
								若接入方能判断用户当前是使用wifi方式上网时，该参数传1；
								若接入方对用户上网方式不可知时，该参数传0。
	* @param @param column
	* @param @param remark
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @author hongrui
	 */
	public static String openOrderLanched(String mobile,String productId,String ringId,String returnUrl,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("product_id",productId)); 
		list.add(new BasicNameValuePair("return_url",returnUrl));
		list.add(new BasicNameValuePair("verify_type","1"));
		list.add(new BasicNameValuePair("column",""));
		list.add(new BasicNameValuePair("remark",""));
		list.add(new BasicNameValuePair("is_message","1"));
		list.add(new BasicNameValuePair("ring_id",ringId));
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "confirm_open_order_launched_ex", ".json",header));
	}
	/**
	 * 调用该接口生成订单。并返回计费认证H5 页面地址。用户跳到计费认证页面完成计费。
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String orderLanched(String mobile,String productId,String returnUrl,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("product_id",productId)); 
		list.add(new BasicNameValuePair("return_url",returnUrl));
		list.add(new BasicNameValuePair("verify_type","1"));
		list.add(new BasicNameValuePair("column",""));
		list.add(new BasicNameValuePair("remark",""));
		list.add(new BasicNameValuePair("is_message","1"));
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "confirm_order_launched_ex", ".json",header));
	}
	/**
	 * H5 计费下单发起接口（EMP 计费）
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String orderLanchedEmp(String mobile,String productId,String returnUrl,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("product_id",productId)); 
		list.add(new BasicNameValuePair("return_url",returnUrl));
		list.add(new BasicNameValuePair("column",""));
		list.add(new BasicNameValuePair("remark",""));
		list.add(new BasicNameValuePair("is_message","1"));
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "confirm_order_launched_emp", ".json",header));
	}
	/**
	 * 计费下单发起接口（内容计费
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String orderLanchedContentfee(String mobile,String productId,String returnUrl,String ringId,String tid,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("ring_id",ringId));
		list.add(new BasicNameValuePair("product_id",productId)); 
		list.add(new BasicNameValuePair("return_url",returnUrl));
//		list.add(new BasicNameValuePair("tid",tid));
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "confirm_order_launched_contentfee", ".json",header));
	}
	/**
	 * 
	* @Title: query_h5_order
	* @Description: TODO(通过H5计费订单号查询订单详情)
	* @param @param order_no
	* @param @param phone
	* @param @return    设定文件
	* @return String    返回类型
	* @author hongrui
	 */
	public static String query_h5_order(String order_no,String phone,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("order_no",order_no)); 
		list.add(new BasicNameValuePair("mdn",phone)); 
		return HttpUtils.httpGetTool(list, "/v3/packageservice/", "query_h5_order", ".json",header);
	}
	
	/**
	 * 彩铃计费下单发起接口（音、视频）,并下发短信
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String ismpOrderLaunched(String mobile,String productId,String returnUrl,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("product_id",productId)); 
		list.add(new BasicNameValuePair("return_url",returnUrl));
		list.add(new BasicNameValuePair("column",""));
		list.add(new BasicNameValuePair("remark",""));
		list.add(new BasicNameValuePair("is_message","1"));
		String orderResult = HttpUtils.httpPostTool(list, "/v3/packageservice/", "ismp_order_launched", ".json",header);
		JSONObject jsonResult = JSONObject.fromObject(orderResult);
		logger.info("下单返回："+jsonResult.toString());
		String code = jsonResult.getString("res_code");
		if(code.equals("0")) {
			String orderNo = jsonResult.getString("order_no");
			String sendResult = ismpVerifyCodeSend(mobile,orderNo,header);
			JSONObject sendJson = JSONObject.fromObject(sendResult);
			String sendCode = sendJson.getString("res_code");
			if(sendCode.equals("0")) {
				sendJson.put("orderNo", orderNo);
			}
			return sendJson.toString();
		}else {
			return jsonResult.toString();
		}
	}
	/**
	 * 计费验证码下发接口
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String ismpVerifyCodeSend(String mobile,String orderNo,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("order_no",orderNo)); 
		String channelId = header.getString("channelId");
		if(StringUtils.isNotBlank(channelId)) {
			list.add(new BasicNameValuePair("channel_id",channelId));
		}else {
			list.add(new BasicNameValuePair("channel_id",Configuration.CHANNELID));
		}
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "ismp_verify_code_send", ".json",header));
	}
	/**
	 * 计费订单确认接口
	 * @param mobile
	 * @param productId
	 * @param returnUrl
	 * @param header
	 * @return
	 */
	public static String ismpConfirmOrder(String mobile,String orderNo,String verifyCode,PageData header){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile",mobile));
		list.add(new BasicNameValuePair("order_no",orderNo)); 
		list.add(new BasicNameValuePair("verify_code",verifyCode));
		return (HttpUtils.httpPostTool(list, "/v3/packageservice/", "ismp_confirm_order", ".json",header));
	}
	/**
	 * 上次视频文件
	 * @param filePath
	 * @param header
	 * @return
	 * @throws Exception
	 */
	public static String uploadvrbtfiles(String filePath,PageData header){
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	 URL fileurl = new URL(filePath);    
 	        HttpURLConnection conn = (HttpURLConnection)fileurl.openConnection();    
 	                //设置超时间为3秒  
 	        conn.setConnectTimeout(3*1000);  
 	        //防止屏蔽程序抓取而返回403错误  
 	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
 	        //得到输入流  
 	        InputStream inputStream = conn.getInputStream();   
 	        HttpPost httpPost = new HttpPost(Configuration.BASEURL+"/v3/diyvrbtservice/upload/uploadvrbtfiles.json");// 创建httpPost
 	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
 	        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
 	        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //加上此行代码解决返回中文乱码问题
 	        String fileName = null;
 	        fileName = System.currentTimeMillis()+".mp4";
 	        builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
 	       HttpUtils.setHeader(httpPost, null,header);
 	        HttpEntity entity = builder.build();
 	        httpPost.setEntity(entity);
 	        
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                 return null;
            }
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	/**
	 * DIY 视频彩铃入库
	 * @param videoName 视频名称
	 * @param actorName 创作者名称
	 * @param phone 手机号
	 * @param type 铃音类型，1=私有，2=公开
	 * @param callback 通知回调接口地址
	 * @param filePath 原始视频 CEPHS3 绝对地址
	 * @param packageId 视频彩铃 DIY 包月产品 ID
	 * @param header
	 * @return
	 */
	public static String applyDiy(String videoName,String actorName,String phone,String type,String callback,String filePath,String packageId,PageData header){
		List<NameValuePair> requestList = new ArrayList<NameValuePair>();
		List<NameValuePair> headerList = new ArrayList<NameValuePair>();
		headerList.add(new BasicNameValuePair("videoName",videoName));
		headerList.add(new BasicNameValuePair("actorName",actorName)); 
		headerList.add(new BasicNameValuePair("phone",phone)); 
		headerList.add(new BasicNameValuePair("type",type));
		headerList.add(new BasicNameValuePair("callback",callback));
		headerList.add(new BasicNameValuePair("filePath",filePath));
		requestList.add(new BasicNameValuePair("videoName",videoName));
		requestList.add(new BasicNameValuePair("actorName",actorName)); 
		requestList.add(new BasicNameValuePair("phone",phone)); 
		requestList.add(new BasicNameValuePair("type",type));
		requestList.add(new BasicNameValuePair("callback",callback));
		requestList.add(new BasicNameValuePair("filePath",filePath));
		requestList.add(new BasicNameValuePair("packageId",packageId));
		
		requestList.add(new BasicNameValuePair("autoPublish","")); 
		requestList.add(new BasicNameValuePair("description","")); 
		requestList.add(new BasicNameValuePair("musicName","")); 
		requestList.add(new BasicNameValuePair("originalActorName","")); 
		requestList.add(new BasicNameValuePair("words","")); 
		requestList.add(new BasicNameValuePair("cv","")); 
		requestList.add(new BasicNameValuePair("videoConverts","")); 
		requestList.add(new BasicNameValuePair("pictures","")); 
		
		return (HttpUtils.httpPostTool(requestList,headerList, "/v3/diyvrbtservice/diy/", "applydiy", ".json",header));
	}
	
	public static String querypublishresult(String taskCode,PageData header){
		List<NameValuePair> requestList = new ArrayList<NameValuePair>();
		requestList.add(new BasicNameValuePair("taskCode",taskCode));
		return (HttpUtils.httpPostTool(requestList,null, "/v3/diyvrbtservice/diy/", "querypublishresult", ".json",header));
	}

	public static void main(String[] args) {
		PageData header = new PageData();
		header.put("appKey", "2000000002484");
		header.put("appSecret", "WCWnPP7ngPJH");
		System.out.println(unsubscribebyemp("13362273197","135999999999999000271",header));
	}
}

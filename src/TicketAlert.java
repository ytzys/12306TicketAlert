import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TicketAlert {
	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.trustStore",
				"F:\\WorkSpace\\TicketAlert\\jssecacerts");
		Executor executor = Executors.newSingleThreadExecutor();
		Runnable task = new Runnable() {

			@Override
			public void run() {
				String tmp = "";
				URL url;
				try {
					url = new URL(
							"https://kyfw.12306.cn/otn/leftTicket/queryT?leftTicketDTO.train_date=2015-12-20&leftTicketDTO.from_station=JIK&leftTicketDTO.to_station=BJP&purpose_codes=ADULT");
					byte[] buffer = new byte[1024 * 8];
					int readSize;
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						while ((readSize = is.read(buffer)) >= 0) {
							bos.write(buffer, 0, readSize);
						}
						tmp = bos.toString();
					}
					JSONObject jsonObject = new JSONObject(tmp);
					JSONArray data = jsonObject.optJSONArray("data");
					JSONObject targetTrain = (JSONObject) data.get(1);
					JSONObject queryLeftNewDTO = targetTrain
							.optJSONObject("queryLeftNewDTO");
					String yw_num = queryLeftNewDTO.optString("yw_num");

					System.out.println("yw_num:" + yw_num);
//					Thread.sleep(3000);
					if (yw_num.equals("无")) {
						JOptionPane.showMessageDialog(null, "有票啦！！有票啦！！有票啦！！",
								"查询结果", JOptionPane.INFORMATION_MESSAGE);
						// Runtime.getRuntime()
						// .exec("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe www.baidu.com");
//						Robot robot = new Robot();
//						robot.mouseMove(166, 886);
//						robot.mousePress(InputEvent.BUTTON1_MASK);
//						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} 
//				catch (AWTException e) {
//					e.printStackTrace();
//				} 
//				catch (InterruptedException e) {
//					e.printStackTrace();
//				}

			}
		};
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executor.execute(task);
		}
	}
}

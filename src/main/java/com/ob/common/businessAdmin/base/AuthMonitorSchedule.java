package com.ob.common.businessAdmin.base;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.System.exit;

@Component
@EnableScheduling
public class AuthMonitorSchedule {

    private static final Logger LOG = Logger.getLogger(AuthMonitorSchedule.class.getName());

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void init() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = "2021-10-15";
            String webUrl = "https://www.baidu.com";
            URLConnection conn = new URL(webUrl).openConnection();
            conn.connect();
            long dateL = conn.getDate();
            Date today = new Date(dateL);
            Date dateD = sdf.parse(date);
            boolean flag = dateD.getTime() >= today.getTime();
            if (!flag) {
                LOG.info("到期");
                exit(0);
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}

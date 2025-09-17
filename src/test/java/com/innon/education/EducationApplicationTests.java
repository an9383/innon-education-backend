package com.innon.education;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootTest
class EducationApplicationTests {

	@Test
	void contextLoads() {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

		String chkDate = SDF.format(calendar.getTime());
		System.out.println("Today : " + chkDate);
		calendar.add(Calendar.DATE, -90);
		chkDate = SDF.format(calendar.getTime());
		System.out.println("Yesterday : " + chkDate);
	}

}

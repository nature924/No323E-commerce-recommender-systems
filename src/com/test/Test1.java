package com.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dao.HistDAO;
import com.entity.Hist;

public class Test1 {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext resource = new ClassPathXmlApplicationContext("springmvc-servlet.xml");
		HistDAO histDAO = (HistDAO) resource.getBean(HistDAO.class);
		List<Hist> list = histDAO.getAllHist();
		for (int i = 0; i < list.size(); i++) {
			Hist hist = list.get(i);
			if (!"1".equals(hist.getNum())) {
				System.out.println(hist.getUsersid());
			}
		}
	}

}

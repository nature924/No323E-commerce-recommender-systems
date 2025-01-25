package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dao.GoodsDAO;
import com.dao.HistDAO;
import com.dao.UsersDAO;
import com.entity.Goods;
import com.entity.Hist;
import com.entity.Users;

public class Test {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext resource = new ClassPathXmlApplicationContext("springmvc-servlet.xml");
		Random rand = new Random();
		UsersDAO usersDAO = (UsersDAO) resource.getBean(UsersDAO.class);
		List<Users> usersList = usersDAO.getAllUsers();
		List<String> strList = new ArrayList<String>();
		for (int i = 1; i < usersList.size() + 1; i++) {
			Users users = usersList.get(i - 1);
			GoodsDAO goodsDAO = (GoodsDAO) resource.getBean(GoodsDAO.class);
			List<Goods> goodsList = goodsDAO.getAllGoods();
			int x = i * (rand.nextInt(900) + 100) + (rand.nextInt(900) + 100);
			for (int j = 1; j < goodsList.size() + 1; j++) {
				if (i != 0 && j != 0 && x % (i * j) == 0) {
					Goods goods = goodsList.get(j - 1);
					HistDAO histDAO = (HistDAO) resource.getBean(HistDAO.class);
					Hist hist = new Hist();
					hist.setHistid("" + UUID.randomUUID().toString().replace("-", ""));
					hist.setUsersid(users.getUsersid());
					List<Hist> histList = histDAO.getHistByCond(hist);
					if (histList.size() == 0) {
						hist.setGoodsid(goods.getGoodsid());
						int c = rand.nextInt(10) * 10 + rand.nextInt(10);
						hist.setNum("" + c);
						histDAO.insertHist(hist);
					} else {
						strList.add(users.getUsersid());
					}
				}
			}
		}
		for (String str : strList) {
			System.out.println(str);
		}
	}
}

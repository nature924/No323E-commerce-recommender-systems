package com.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.GoodsDAO;
import com.dao.HistDAO;
import com.dao.UsersDAO;
import com.entity.Goods;
import com.entity.Hist;
import com.entity.Users;
import com.service.RecommendService;

@Service("recommendService")
public class RecommendServiceImpl implements RecommendService {
	@Autowired
	@Resource
	private HistDAO histDAO;
	// 注入DAO对象
	@Autowired
	@Resource
	private GoodsDAO goodsDAO;
	@Autowired
	@Resource
	private UsersDAO usersDAO;
	private String userid = "";
	// 相似用户集合
	private List<List<Object>> similarityUsers = null;
	// 推荐所有商品集合
	private List<String> targetRecommendGoods = null;
	// 浏览过商品集合
	private List<String> commentedGoods = null;
	// 用户在商品浏览集合中的位置
	private int targetUserIndex = 0;
	// 目标用户浏览过的商品
	private List<String> targetUserCommentedGoods = null;

	private String[] goods = null;

	@Override
	public List<Goods> getRecommend(String userid) {
		this.userid = userid;
		// 建立用户数组 除了当前用户外 随机抽取9个用户
		String[] users = new String[10];
		users[0] = this.userid;
		List<Users> usersList = this.usersDAO.getUsers(this.userid);
		System.out.println("users == > " + usersList.size());
		for (int i = 0; i < 9; i++) {
			int j = i + 1;
			int tbNum = usersList.size();
			if (i < tbNum) {
				users[j] = usersList.get(i).getUsersid();
			} else {
				users[j] = "0";
			}
		}
		List<Goods> goodsList = this.goodsDAO.getAllGoods();
		this.goods = new String[goodsList.size()];
		for (int j = 0; j < goodsList.size(); j++) {
			this.goods[j] = goodsList.get(j).getGoodsid();
		}
		// 建立浏览二维数组 用户浏览了商品 1 未浏览 0 之后计算用户的相似度
		int[][] allUserGoodshist = new int[10][goodsList.size()];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < goodsList.size(); j++) {
				String goodsid = this.goods[j];
				Hist hist = new Hist();
				hist.setUsersid(users[i]);
				hist.setGoodsid(goodsid);
				List<Hist> histList = this.histDAO.getHistByCond(hist);
				if (histList.size() == 0) {
					allUserGoodshist[i][j] = 0;
				} else {
					Hist h = histList.get(0);
					allUserGoodshist[i][j] = Integer.parseInt(h.getNum());
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < goodsList.size(); j++) {
				if (j == goodsList.size() - 1) {
					System.out.print(allUserGoodshist[i][j]);
				} else {
					System.out.print(allUserGoodshist[i][j] + " ,");
				}
			}
			System.out.println("");
		}
		this.targetUserCommentedGoods = new ArrayList<String>();
		Hist hist = new Hist();
		hist.setUsersid(this.userid);
		List<Hist> histList = this.histDAO.getHistByCond(hist);
		for (int i = 0; i < histList.size(); i++) {
			// 转换目标用户商品浏览列表
			this.targetUserCommentedGoods.add(histList.get(i).getHistid());
		}

		// 计算用户相似度，排序
		this.calcUserSimilarity(allUserGoodshist, goodsList.size());
		// 计算商品推荐度，排序
		this.calcRecommendGoods(allUserGoodshist, goodsList.size());
		// 处理推荐商品列表
		this.handleRecommendGoods(allUserGoodshist, goodsList.size());
		String rommendId = "";
		for (int i = 0; i < this.targetRecommendGoods.size(); i++) {
			String item = this.targetRecommendGoods.get(i);
			if (!commentedGoods.contains(item)) {
				if (i == this.targetRecommendGoods.size() - 1) {
					rommendId += item;
				} else {
					rommendId += item + ",";
				}
			}
		}
		String[] str = rommendId.split(",");
		List<Goods> hotList = new ArrayList<Goods>();
		List<Goods> goodList = new ArrayList<Goods>();
		int goodsize = 0;
		if (!"".equals(rommendId)) {
			for (String x : str) {
				Goods g = this.goodsDAO.getGoodsById(x);
				goodList.add(g);
				hotList.add(g);
			}
			if (hotList.size() < 10) {
				goodsize = 10 - goodList.size();
				List<Goods> list = this.goodsDAO.getGoodsByHot();
				for (int i = 0; i < goodsize; i++) {
					Goods x = list.get(i);
					hotList.add(x);
				}
			} else if (goodList.size() > 10) {
				hotList = new ArrayList<Goods>();
				for (int i = 0; i < 10; i++) {
					Goods x = goodList.get(i);
					hotList.add(x);
				}
			}
		} else {
			hotList = this.goodsDAO.getGoodsByHot();
		}
		return hotList;
	}

	private void calcRecommendGoods(int[][] allUserGoodshist, int goodsNum) {
		this.targetRecommendGoods = new ArrayList<String>();
		List<List<Object>> recommendGoods = new ArrayList<List<Object>>();
		List<Object> recommendGood = null;
		double recommdRate = 0, sumRate = 0;
		for (int i = 0; i < goodsNum; i++) {
			recommendGood = new ArrayList<Object>();
			recommendGood.add(i);
			recommdRate = allUserGoodshist[Integer.parseInt(similarityUsers.get(0).get(0).toString())][i]
					* Double.parseDouble(similarityUsers.get(0).get(1).toString())
					+ allUserGoodshist[Integer.parseInt(similarityUsers.get(1).get(0).toString())][i]
							* Double.parseDouble(similarityUsers.get(1).get(1).toString());
			recommendGood.add(recommdRate);
			recommendGoods.add(recommendGood);
			sumRate += recommdRate;
		}
		System.out.println("sumRate  == > " + sumRate / goodsNum);
		recommendGoods = compare(recommendGoods);
		for (List<Object> tList : recommendGoods) {
			System.out.println(tList.get(1));
		}
		// 大于平均推荐度的商品才有可能被推荐
		for (int i = 0; i < recommendGoods.size(); i++) {
			List<Object> item = recommendGoods.get(i);
			if (Double.parseDouble(item.get(1).toString()) > sumRate / goodsNum) { // 大于平均推荐度的商品才有可能被推荐
				System.out.println("goods= = >" + goods[Integer.parseInt(item.get(0).toString())]);
				this.targetRecommendGoods.add(goods[Integer.parseInt(item.get(0).toString())]);
			}
		}
		for (String x : this.targetRecommendGoods) {
			System.out.println("x= = >" + x);
		}
	}

	/**
	 * 把推荐列表中用户已经浏览过的商品剔除
	 */
	private void handleRecommendGoods(int[][] allUserGoodshist, int goodsNum) {
		int[] user2hist = new int[goodsNum];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < goodsNum; j++) {
				user2hist[j] = allUserGoodshist[i][j];
			}
		}
		commentedGoods = new ArrayList<String>();
		for (int i = 0; i < user2hist.length; i++) {
			if (allUserGoodshist[0][i] != 0) {
				commentedGoods.add(goods[i]);
			}
		}
	}

	/**
	 * 获取两个最相似的用户
	 */
	private void calcUserSimilarity(int[][] allUserGoodshist, int goodsNum) {
		int[] user2hist = new int[goodsNum];
		List<List<Object>> tmpList = new ArrayList<List<Object>>();

		for (int i = 0; i < 10; i++) {
			if (i == targetUserIndex) {
				for (int j = 0; j < goodsNum; j++) {
					user2hist[j] = allUserGoodshist[i][j];
				}
				continue;
			}
			List<Object> userSimilarity = new ArrayList<Object>();
			int[] user1hist = new int[goodsNum];
			for (int j = 0; j < goodsNum; j++) {
				user1hist[j] = allUserGoodshist[i][j];

			}
			userSimilarity.add(i);
			userSimilarity.add(calcTwoUserSimilarity(user1hist, user2hist, goodsNum));
			tmpList.add(userSimilarity);
		}
		this.similarityUsers = compare(tmpList);
	}

	/**
	 * 根据用户数据，计算用户相似度
	 * 
	 * @param user1hist
	 * @param user2hist
	 * @return
	 */
	private static double calcTwoUserSimilarity(int[] user1hist, int[] user2hist, int goodsNum) {
		double sum = 0;
		for (int i = 0; i < goodsNum; i++) {
			sum += Math.pow(user1hist[i] - user2hist[i], 2);
		}
		return Math.sqrt(sum);
	}

	/**
	 * 集合排序
	 */
	private static List<List<Object>> compare(List<List<Object>> tmpList) {
		for (int i = 0; i < tmpList.size(); i++) {
			for (int j = 0; j < tmpList.size() - i; j++) {
				List<Object> t1 = tmpList.get(i);
				List<Object> t2 = tmpList.get(j);
				if (Double.parseDouble("" + t1.get(1)) > Double.parseDouble("" + t2.get(1))) {
					List<Object> tmp = new ArrayList<Object>();
					tmp = t1;
					tmpList.set(i, t2);
					tmpList.set(j, tmp);
				}
			}
		}
		return tmpList;
	}

	public HistDAO getHistDAO() {
		return histDAO;
	}

	public void setHistDAO(HistDAO histDAO) {
		this.histDAO = histDAO;
	}

	public GoodsDAO getGoodsDAO() {
		return goodsDAO;
	}

	public void setGoodsDAO(GoodsDAO goodsDAO) {
		this.goodsDAO = goodsDAO;
	}

	public UsersDAO getUsersDAO() {
		return usersDAO;
	}

	public void setUsersDAO(UsersDAO usersDAO) {
		this.usersDAO = usersDAO;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<List<Object>> getSimilarityUsers() {
		return similarityUsers;
	}

	public void setSimilarityUsers(List<List<Object>> similarityUsers) {
		this.similarityUsers = similarityUsers;
	}

	public List<String> getTargetRecommendGoods() {
		return targetRecommendGoods;
	}

	public void setTargetRecommendGoods(List<String> targetRecommendGoods) {
		this.targetRecommendGoods = targetRecommendGoods;
	}

	public List<String> getCommentedGoods() {
		return commentedGoods;
	}

	public void setCommentedGoods(List<String> commentedGoods) {
		this.commentedGoods = commentedGoods;
	}

	public int getTargetUserIndex() {
		return targetUserIndex;
	}

	public void setTargetUserIndex(int targetUserIndex) {
		this.targetUserIndex = targetUserIndex;
	}

	public List<String> getTargetUserCommentedGoods() {
		return targetUserCommentedGoods;
	}

	public void setTargetUserCommentedGoods(List<String> targetUserCommentedGoods) {
		this.targetUserCommentedGoods = targetUserCommentedGoods;
	}

	public String[] getGoods() {
		return goods;
	}

	public void setGoods(String[] goods) {
		this.goods = goods;
	}

}

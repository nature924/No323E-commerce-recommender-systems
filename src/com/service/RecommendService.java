package com.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.entity.Goods;

@Service("recommendService")
public interface RecommendService {
	public List<Goods> getRecommend(String userid);
}

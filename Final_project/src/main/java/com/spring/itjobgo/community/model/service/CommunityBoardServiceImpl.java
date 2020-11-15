package com.spring.itjobgo.community.model.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.itjobgo.community.model.dao.CommunityBoardDao;
import com.spring.itjobgo.community.model.vo.CB_ATTACHMENT;
import com.spring.itjobgo.community.model.vo.CommunityBoard;
import com.spring.itjobgo.portfolio.model.vo.Attachment;

@Service
public class CommunityBoardServiceImpl implements CommunityBoardService {

@Autowired
private CommunityBoardDao dao;

@Autowired
private SqlSessionTemplate session;

@Override
public List<CommunityBoard> selectBoardList() {
	// TODO Auto-generated method stub
	return dao.selectBoardList(session);
}

@Override
public int insertCommunityBoard(CommunityBoard cb, List<CB_ATTACHMENT> files) {
	
	int result=dao.insertCommunityBoard(session,cb);
	
	if(result==0) throw new RuntimeException("입력오류");
	if(result>0) {
		if(!files.isEmpty()) {
			//files에 데이터가 있으면
			for(CB_ATTACHMENT file:files) {
				result=dao.insertAttachment(session,file);
				if(result==0)throw new RuntimeException("입력오류");
			}
		}
		
	}
	return result;
}









}

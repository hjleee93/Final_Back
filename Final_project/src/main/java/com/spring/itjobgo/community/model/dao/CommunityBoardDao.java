package com.spring.itjobgo.community.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.spring.itjobgo.community.model.vo.CB_ATTACHMENT;
import com.spring.itjobgo.community.model.vo.CommunityBoard;
import com.spring.itjobgo.portfolio.model.vo.Attachment;

public interface CommunityBoardDao {
	
	List<CommunityBoard> selectBoardList(SqlSessionTemplate session);
	
	//글작성
	int insertCommunityBoard(SqlSessionTemplate session, CommunityBoard CB);
	
	//첨부파일 insert
	int insertAttachment(SqlSessionTemplate session, CB_ATTACHMENT cb_attach);

}

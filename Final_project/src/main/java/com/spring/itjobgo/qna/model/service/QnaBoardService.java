package com.spring.itjobgo.qna.model.service;

import java.util.List;

import com.spring.itjobgo.qna.model.vo.QB_ATTACHMENT;
import com.spring.itjobgo.qna.model.vo.QnaBoard;

public interface QnaBoardService {

	List<QnaBoard> selectQnaBoard();
	
	int insertQnaBoard(QnaBoard qnaboard, List<QB_ATTACHMENT> files);
	
	
	
	
	
	
}

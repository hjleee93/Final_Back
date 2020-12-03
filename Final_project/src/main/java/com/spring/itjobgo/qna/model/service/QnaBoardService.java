package com.spring.itjobgo.qna.model.service;

import java.util.List;

import com.spring.itjobgo.qna.model.vo.QB_ATTACHMENT;
import com.spring.itjobgo.qna.model.vo.QB_COMMENT;
import com.spring.itjobgo.qna.model.vo.QnaBoard;

public interface QnaBoardService {

	List<QnaBoard> selectQnaBoard();
	
	int insertQnaBoard(QnaBoard qnaboard, List<QB_ATTACHMENT> files);
	
	QnaBoard selectQnaBoardOne(int qnaSeq, boolean hasRead);
	
	int deleteBoard(int qnaSeq);
	
	QB_ATTACHMENT selectAttach(int qnaSeq);
	
	int updateBoard(QnaBoard qb, List<QB_ATTACHMENT>files);
	
	int updateBoard(QnaBoard qb);
	
	int insertComment(QB_COMMENT cm);

	int insertCommentText(int qbBoardNo);
	
	
	
}

package spring.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.dto.BoardDto;
import spring.domain.entity.BoardEntity;

public interface BoardService {

	void saveAndUpload(BoardDto dto, MultipartFile file) throws IOException;

	void saveAndUpload(BoardDto dto, Part file) throws IOException;

	void multipart(BoardDto dto, MultipartFile[] files) throws IOException ;

	ModelAndView getFileBoard(int pno);

	BoardEntity detail(long no);

	void fileDown(long fno, HttpServletResponse response)throws IOException;

	void delete(long bno);

	void delFile(long fno);

	void update(BoardDto dto);

	ModelAndView getFileBoard2();

	ModelAndView addList(int i);

	

	

	

	

}

package spring.domain.dto;

import java.util.List;

import lombok.Data;
import spring.domain.entity.BoardEntity;
import spring.domain.entity.FileEntity;

@Data
public class BoardDto {

	private long bno;
	private String subject;
	private String contents;
	private String creatorId;
	private List<FileEntity> fileList;
	
	//BoardEntity toEntity = new BoardEntity(bno, subject, contents, creatorId, fileList);
	public BoardEntity toEntity() {
		return new BoardEntity(bno, subject, contents, creatorId, fileList);
	}
	
	
}

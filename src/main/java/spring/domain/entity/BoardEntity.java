package spring.domain.entity;

import java.time.LocalDateTime;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@DynamicInsert
@DynamicUpdate
@ToString
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "jboard")
@Getter
@NoArgsConstructor
//@SequenceGenerator(name = "seq_gen",sequenceName = "seq_jboard", allocationSize = 1,initialValue = 1)
public class BoardEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen") //매핑처리
	private long bno;
	
	@Column(nullable = false)
	private String subject;
	
	@Column(nullable = false)
	private String contents;
	
	@Column(nullable = false)
	private String creatorId;
	
	@Column
	@ColumnDefault("0")
	private int readCount;
	
	//@EntityListeners(AuditingEntityListener.class)가 처리해줌
	@CreatedDate
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime updatedDate;
	
	//테이블끼리 연결할때 자바소스 기준으로 생각하자!( JPA로 자바소스를 사용해서 테이블을 만들고 있으니깐)
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "bno")
	private List<FileEntity> files;
	
	
	//dto를 entity로 바꾸기위해서 using field 만들자!
	public BoardEntity(long bno, String subject, String contents, String creatorId, List<FileEntity> files) {
		this.bno = bno;
		this.subject = subject;
		this.contents = contents;
		this.creatorId = creatorId;
		this.files = files;
	}

	//update할때 수정할거
	public void update(String subject, String contents) {
		this.subject = subject;
		this.contents = contents;
	}

	
	
}

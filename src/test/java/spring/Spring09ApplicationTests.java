package spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import spring.domain.entity.BoardEntity;
import spring.domain.entity.BoardEntityRepository;

@SpringBootTest
class Spring09ApplicationTests {
	@Autowired
	BoardEntityRepository repo;
	
	// @Test
	void contextLoads() {
		for(int i=101; i<1000; i++) {
			String subject="제목"+i;
			String contents="내용"+i;
			String creatorId = "guest"+(i%5+1);
			BoardEntity entity = new BoardEntity(0, subject, contents, creatorId, null);
			repo.save(entity);
		}
		
	}

}

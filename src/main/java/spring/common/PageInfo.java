package spring.common;

import lombok.Data;

@Data
public class PageInfo {

	int from;
	int to;
	int pageTot;
	
	public PageInfo(int pno, int pageTot) {
		//페이지번호를 출력 할 개수
		this.pageTot=pageTot;
		int pageBlock = 10;
		
		//1~10:1번블록
		//11~20:2번블록
		int blockNo=pno/pageBlock;
		// 1~9/10=0+1  10/10=1
		if(pno%pageBlock>0) blockNo++;
		
		this.to = blockNo*pageBlock; //1*10
		this.from = to-pageBlock+1;
		
		//마지막페이지 체크
		if(to>pageTot) {
			to=pageTot;
		}
	}
	
	
	
	
}

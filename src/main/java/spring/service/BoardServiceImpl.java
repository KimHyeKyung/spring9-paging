package spring.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import spring.common.FileUtils;
import spring.common.PageInfo;
import spring.domain.dto.BoardDto;
import spring.domain.entity.BoardEntity;
import spring.domain.entity.BoardEntityRepository;
import spring.domain.entity.FileEntity;
import spring.domain.entity.FileEntityRepository;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	BoardEntityRepository repo;
	
	@Autowired
	FileEntityRepository fRepo;
	
	@Autowired
	FileUtils fileUtils;
	
//---------------------------------------------------------------------------------	
	
	@Override
	public void saveAndUpload(BoardDto dto, MultipartFile file) throws IOException {
		
		//file upload 처리과정
		long fileSize = file.getSize();//파일 사이즈
		
		//fileName을 어디에 업로드할겁니까(path경로) -> static폴더에 upload폴더로
		ClassPathResource cpr = new ClassPathResource("static/upload");
		File dir = cpr.getFile();//위치정보를 file형태로 변환
		String fileName = file.getOriginalFilename(); //파일이름
		
		
		System.nanoTime();
		
		//파일위치와 파일이름을 dest에 저장
		File dest = new File(dir, fileName);
		
		//업로드처리
		file.transferTo(dest);
	}

	@Override
	public void saveAndUpload(BoardDto dto,Part file) throws IOException  {
		long fileSize = file.getSize();
		ClassPathResource cpr = new ClassPathResource("static/upload");
		File dir = cpr.getFile();//위치정보를 file형태로 변환
		String fileName = file.getSubmittedFileName();
		
		System.out.println("fileName : + "+fileName);
		
		//-------------------------------------------------------------------------
		// 파일을 파일이름과 확장자로 나누고 
		// 이름뒤에 숫자를 붙여서 새로운 파일이름으로 변경
		//fileName : aaa.png
		// .문자 기준으로 문자열 나누기
		String[] strs = fileName.split("\\.");
		
		String newName=strs[0]; //aaa
		String fileExtension=strs[1]; //png
		newName += "_" + System.nanoTime()+"."; //aaa_01293486.
		newName +=	fileExtension;	//aaa_01293486.png
		
		System.out.println(newName);
		//-------------------------------------------------------------------------
		//오늘 날짜를 폴더이름이로 만들기위해서
		//Formatter로 년/월/일 설정 후 디렉토리 생성
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		String fm = now.format(format); // 포멧 적용된 날짜
		
		File nowDir = new File(dir, fm);
		if(nowDir.exists()==false){ //결과가 존재하지않으면(디렉토리가 없으면)
			nowDir.mkdir();
		}
		//-------------------------------------------------------------------------		

		file.write(nowDir.getPath() + "/" + newName);
		
	}
	
	
	@Override
	public void multipart(BoardDto dto, MultipartFile[] files) throws IOException {
		
		//파일선택이 없을때는 파일처리 안함.
		if(files != null && !files[0].isEmpty()) {
			List<FileEntity> fileList = fileUtils.fileUploadList(files);
			dto.setFileList(fileList);
			
		}
			///////////////////////파일 업로드 처리 끝//////////////////////////
			
			//dto로 넘어온 데이터를 entity로 셋팅해서 처리합시다!
			repo.save(dto.toEntity());
	}
//--------------------------------------------------------------------
	@Override
	public ModelAndView getFileBoard(int pno) {
		Sort sort = Sort.by(Direction.DESC, "bno");
		
		//페이징 처리(페이지번호(0부터 시작), 게시글 수)
		int index=pno-1;
		Pageable pageable = PageRequest.of(index, 10, sort);
		
		Page<BoardEntity> page = repo.findAll(pageable);
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		
		
		PageInfo pageInfo = new PageInfo(pno, page.getTotalPages());
		
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", page.getContent());
		mv.addObject("pageInfo", pageInfo);
		
		return mv;
	}
 
	@Override
	public BoardEntity detail(long bno) {
		BoardEntity result = repo.findById(bno).orElse(null);
		return result;
	}

	
	@Override
	public void fileDown(long fno, HttpServletResponse response) throws IOException{
		FileEntity result=fRepo.findById(fno).orElse(null);
		
		fileUtils.fileDownload(result, response);
		
	}

	@Override
	public void delete(long bno) {
		repo.deleteById(bno);
		
	}

	@Override
	public void delFile(long fno) {
		fRepo.deleteById(fno);
	}

	@Override
	public void update(BoardDto dto) {
		//save하기위해서 먼저 저장된 데이터를 검색해서 가져오자
		BoardEntity entity = repo.findById(dto.getBno()).orElse(null);
		
		//가져왔으니 변경할 정보만 수정하자
		entity.update(dto.getSubject(), dto.getContents());
		
		//변경된 데이터save하기
		repo.save(entity);
	}

	@Override
	public ModelAndView getFileBoard2() {
		
		Sort sort = Sort.by(Direction.DESC, "bno");
		
		//페이징 처리(페이지번호(0부터 시작), 게시글 수)
		int index=0;
		Pageable pageable = PageRequest.of(index, 10, sort);
		
		Page<BoardEntity> page = repo.findAll(pageable);
		ModelAndView mv = new ModelAndView();
		mv.addObject("page",page);
		System.out.println(page.getNumber());
		return mv;
	}

	@Override
	public ModelAndView addList(int i) {
		Sort sort = Sort.by(Direction.DESC, "bno");
		
		Pageable pageable = PageRequest.of(i, 10, sort);
		
		Page<BoardEntity> page = repo.findAll(pageable);
		ModelAndView mv = new ModelAndView();
		mv.addObject("page",page);
		
		return mv;
	}




	

	
}

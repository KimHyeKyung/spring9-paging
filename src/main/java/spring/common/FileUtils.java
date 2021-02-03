package spring.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import spring.domain.entity.FileEntity;

@Component
public class FileUtils {
	
	public List<FileEntity> fileUploadList(MultipartFile[] files) throws IOException {
		
		//파일업로드 처리
		ClassPathResource cpr=new ClassPathResource("static/upload");
		File dir=cpr.getFile();
		//////////////////////////////////////////////////
		//오늘 날짜를 폴더이름으로 만들기위해서
		//Formatter로 연월일(yyyyMMdd) 설정후 디렉토리 생성
		//////////////////////////////////////////////////
		DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now=LocalDateTime.now();
		String fm=now.format(format);//포멧이 적용된 날짜
		
		File nowDir=new File(dir, fm);
		if(nowDir.exists()==false) {
		nowDir.mkdir();
		}
		//////////////////////////////////////////////////
		
		List<FileEntity> fileList=new Vector<FileEntity>();
		
		
		//파일확인
		for(MultipartFile mf : files) {
			
			long fileSize=mf.getSize();
			
			String OriginalName=mf.getOriginalFilename();
			
						
			//////////////////////////////////////////////////
			//파일을 파일이름 과 확장자로 나누고
			//이름뒤에 숫자를 붙여서 새로운 파일이름으로 변경
			//////////////////////////////////////////////////
			String[] strs=OriginalName.split("[.]");// "\\."
			String newFileName=strs[0];
			String fileExtension=strs[1];
			newFileName += "_"+System.nanoTime()+".";
			newFileName += fileExtension;			
			////////////////////////////////////////////////////
	
			//오늘날짜의 dir 아래 새로만들어진 파일이름으로 업로드..
			mf.transferTo(new File(nowDir, newFileName));
			System.out.println("파일업로드 완료!");
			
			String path=File.separator + "upload" + File.separator + fm + File.separator;
			//System.out.println(path);
			//File Entity 객체 만들기
			FileEntity entity=new FileEntity(OriginalName, newFileName, path, fileSize);
			//list에 추가
			fileList.add(entity);
			
			}
		
		return fileList;
}
	
	public void fileDownload(FileEntity result, HttpServletResponse response) throws IOException{
		ClassPathResource cpr=new ClassPathResource("static");
		File resource=cpr.getFile();
		//저장소default위치+파일저장된 폴더위치+파일명
		String filePath=resource.getPath()+result.getPath()+result.getNewFileName();
		File file=new File(filePath);
		//byte[] bytes=new byte[(int)result.getFileSize()];
		
		///파일의 내용을 읽어서 byte[]로 저장/////////
		byte[] bytes=new byte[(int)file.length()];
		FileInputStream fis=null;
		fis=new FileInputStream(file);
		
		fis.read(bytes);
		
		if(fis!=null)fis.close();
		////////////////////////////////////////
		response.setContentType("appliction/octet-stream");
		response.setContentLength(bytes.length);
		String fileName=URLEncoder.encode(result.getOriginalName(), "UTF-8");
		response.setHeader("Content-Disposition", "attachment; fileName="+fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	
}

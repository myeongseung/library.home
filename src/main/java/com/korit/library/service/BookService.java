package com.korit.library.service;

import com.korit.library.exception.CustomValidationException;
import com.korit.library.repository.BookRepository;
import com.korit.library.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BookService {

    @Value("${file.path}") //yml에 등록해놓은 경로를 스프링 어노테이션 Value에 경로저장
    private String filePath; //그럼 filePath가 경로를 가지고 있음.
    @Autowired
    private BookRepository bookRepository;

    public List<BookMstDto> searchBook(SearchReqDto searchReqDto){
        searchReqDto.setIndex();
        return bookRepository.searchBook(searchReqDto);
    }

    public List<CategoryDto> getCategories(){
        return bookRepository.findAllCategory();
    }

    public void registerBook(BookReqDto bookReqDto){
        duplicateBookCode(bookReqDto.getBookCode());
        bookRepository.saveBook(bookReqDto);
    }

    private void duplicateBookCode(String bookCode){
        BookMstDto bookMstDto = bookRepository.findBookByBookCode(bookCode);
        if(bookMstDto != null){
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("bookCode", "이미 존재하는 도서코드입니다.");

            throw new CustomValidationException(errorMap);
        }
    }

    public void modifyBook(BookReqDto bookReqDto){
        bookRepository.updateBookByBookCode(bookReqDto);
    }

    public void patchmodifyBook(BookReqDto bookReqDto){
        bookRepository.maintainUpdateBookByBookCode(bookReqDto);
    }

    public void deleteBook(BookReqDto bookReqDto){
        bookRepository.deleteBookByBookCode(bookReqDto);
    }

    public void registerBookImages(String bookCode, List<MultipartFile> files){
        if(files.size() < 1){
            Map<String, String> errorMap = new HashMap<String, String>();
            errorMap.put("files","이미지를 선택하세요.");

            throw new CustomValidationException(errorMap);
        }

        List<BookImageDto> bookImageDtos = new ArrayList<BookImageDto>(); //이미지들을 리스트로 저장

        files.forEach(file -> {
            String originFileName = file.getOriginalFilename(); //원래의 이름명
            String extention = originFileName.substring(originFileName.lastIndexOf("."));//뒤에 확장자명 png
            String tempFileName = UUID.randomUUID().toString().replaceAll("-","") + extention; //우리가 변형시킬 saveName

            Path uploadPath = Paths.get(filePath + "book/" + tempFileName);
            File f = new File(filePath+"book");

            if(!f.exists()){
                f.mkdir();
            }
            try {
                Files.write(uploadPath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BookImageDto bookImageDto = new BookImageDto().builder()
                    .bookCode(bookCode)
                    .saveName(tempFileName)
                    .originName(originFileName)
                    .build();

            bookImageDtos.add(bookImageDto); //리스트에 넣고
        });
        bookRepository.registerBookImages(bookImageDtos); //리스트에 담긴 정보를 데이터 베이스에 넣는다.
    }

    public List<BookImageDto> getBooks(String bookCode){
        return bookRepository.findBookImagesAll(bookCode);
    }

    public void removeBookImage(int imageId){
        BookImageDto bookImageDto = bookRepository.findBookImageByImageId(imageId);
        if(bookImageDto == null){
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error!","존재하지 않는 이미지 ID입니다.");

            throw new CustomValidationException(errorMap);
        }

        if(bookRepository.deleteBookImg(imageId) > 0){
            File file = new File(filePath + "book/" + bookImageDto.getSaveName());
            if(file.exists()){
                file.delete();
            }
        }
    }
}


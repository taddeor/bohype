package com.bohype.rest;

import com.bohype.bean.BookmarkBean;
import com.bohype.model.Bookmark;
import com.bohype.repository.BookmarkRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class BookMarkRestController {
    private final BookmarkRepository bookmarkRepository;
    @GetMapping("/bookmarks")
    public Set<BookmarkBean> getBookmark(){
            final var bookmarks = bookmarkRepository.findAll();
        return bookmarks.stream().map(mapping()).collect(Collectors.toSet());
    }

    private Function<Bookmark,BookmarkBean> mapping(){
        return  bookmark -> {
            BookmarkBean bookmarkBean = new BookmarkBean();
            BeanUtils.copyProperties(bookmark,bookmarkBean);
            return bookmarkBean;
        };
    }

    @PostMapping(value = "/bookmarks", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void postBookmark(@RequestBody BookmarkBean bookmarkBeans){
        final var bookmarkBeans1 = Optional.ofNullable(bookmarkBeans);
        if (bookmarkBeans1.isPresent()) {
            final var bookmarkBean = bookmarkBeans1.get();
            final var byTitle = bookmarkRepository.findByTitleAndLink(bookmarkBean.getTitle(), bookmarkBean.getLink());
            if (byTitle.isEmpty()) {
                bookmarkRepository.save(bookmarkBeans1.map(mappingBean()).get());
            }

        }
    }


    @PostMapping(value = "/bookmarks/upload", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void postUploadBookmark( @RequestPart MultipartFile document ) throws IOException {
        final var objectMapper = new ObjectMapper();
        TypeReference<List<BookmarkBean>> typeRef = new TypeReference<List<BookmarkBean>>() {};
        final var bookmarkBeans = objectMapper.readValue(document.getInputStream(), typeRef);
        for (BookmarkBean bookmarkBean : bookmarkBeans) {
            //no duplicate
            final var byTitle = bookmarkRepository.findByTitleAndLink(bookmarkBean.getTitle(), bookmarkBean.getLink());
            if (byTitle.isEmpty()) {
                bookmarkRepository.save(byTitle.get());
            }
        }
    }

    private Function<BookmarkBean,Bookmark> mappingBean(){
        return  bookmark -> {
            Bookmark bookmarkBean = new Bookmark();
            BeanUtils.copyProperties(bookmark,bookmarkBean);
            return bookmarkBean;
        };
    }
}

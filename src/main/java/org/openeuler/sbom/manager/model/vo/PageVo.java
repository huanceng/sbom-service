package org.openeuler.sbom.manager.model.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties({ "pageable", "sort" })
public class PageVo<T> extends PageImpl<T> {

    public PageVo(PageImpl<T> pageImpl) {
        super(pageImpl.getContent(), pageImpl.getPageable(), pageImpl.getTotalElements());
    }

    public PageVo(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageVo(List<T> content) {
        super(content);
    }
}

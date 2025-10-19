package per.liyl.gateway.response;

import java.util.List;

public class PageResponse<T> extends Response{

    private Integer pageNum;

    private Integer pageSize;

    private Long totalNum;

    // private List<T> contents;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public void setContents(List<T> contents) {
    }

    public List<T> getContents() {
        return null;
    }
}

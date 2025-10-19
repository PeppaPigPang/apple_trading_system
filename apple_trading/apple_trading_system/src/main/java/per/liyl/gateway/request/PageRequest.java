package per.liyl.gateway.request;

public class PageRequest {

    private int pageNum = 0;

    private int pageSize = 10;

    public int getOffset(){
        return pageNum == 0 ? 0 : (pageNum - 1) * pageSize;
    }

    public int getH2PageNum(){
        return pageNum == 0 ? 0 : (pageNum - 1);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

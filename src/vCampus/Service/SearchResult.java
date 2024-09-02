package vCampus.Service;

import java.util.List;
import java.io.Serializable;

public class SearchResult<T> implements Serializable {
    private int total;
    private int currentPage;
    private int pageSize;
    private List<T> result;

    public SearchResult(int total, int currentPage, int pageSize, List<T> result) {
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.result = result;
    }

    // Getters and Setters
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}

package com.example.bot.model;

import com.google.gson.annotations.Expose;

public class BodyDetail {

    @Expose
    private ItemsDetail items;
    @Expose
    private Long numOfRows;
    @Expose
    private Long pageNo;
    @Expose
    private Long totalCount;

    public ItemsDetail getItems() {
        return items;
    }

    public void setItems(ItemsDetail items) {
        this.items = items;
    }

    public Long getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Long numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}

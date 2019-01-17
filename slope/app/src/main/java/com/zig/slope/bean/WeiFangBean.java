package com.zig.slope.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17120 on 2018/11/16.
 */

public class WeiFangBean implements Serializable {
    private int pageTotal;
    private int rowNumber;
    private List<WeiFang> list;
    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<WeiFang> getList() {
        return list;
    }

    public void setList(List<WeiFang> list) {
        this.list = list;
    }
}

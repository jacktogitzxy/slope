package com.zig.slope.common.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 17120 on 2018/8/22.
 */

public class HisBean implements Serializable
{
    private int pageTotal;
    private int rowNumber;
    private List<HisReport> inspectList;

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

    public List<HisReport> getInspectList() {
        return inspectList;
    }

    public void setInspectList(List<HisReport> inspectList) {
        this.inspectList = inspectList;
    }
}

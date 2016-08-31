package com.tencent.qchat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cliffyan on 2016/8/30.
 */
public class StaffData {

    private Integer total;
    private List<StaffRow> rows = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<StaffRow> getRows() {
        return rows;
    }

    public void setRows(List<StaffRow> rows) {
        this.rows = rows;
    }
}

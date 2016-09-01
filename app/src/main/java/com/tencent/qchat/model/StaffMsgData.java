package com.tencent.qchat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cliffyan on 2016/9/1.
 */
public class StaffMsgData {
    private Integer total;
    private List<StaffMsgRow> rows = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<StaffMsgRow> getRows() {
        return rows;
    }

    public void setRows(List<StaffMsgRow> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "StaffMsgData{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}

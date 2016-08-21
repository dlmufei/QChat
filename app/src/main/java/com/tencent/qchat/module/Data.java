
package com.tencent.qchat.module;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private Integer total;
    private List<Row> rows = new ArrayList<Row>();

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return The rows
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * @param rows The rows
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Data{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}

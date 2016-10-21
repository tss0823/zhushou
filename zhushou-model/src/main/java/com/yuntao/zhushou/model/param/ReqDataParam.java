package com.yuntao.zhushou.model.param;

import com.yuntao.zhushou.model.domain.ReqContent;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by shan on 2016/8/17.
 */
public class ReqDataParam extends ReqContent {

    private List<DataMap> headerList;

    private List<DataMap> dataList;


    public List<DataMap> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<DataMap> headerList) {
        this.headerList = headerList;
    }

    public List<DataMap> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataMap> dataList) {
        this.dataList = dataList;
    }
}

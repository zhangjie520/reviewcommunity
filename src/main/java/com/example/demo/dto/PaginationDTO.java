package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: codeape
 * @Date: 2021/1/17 19:25
 * @Version: 1.0
 */
@Data
public class
PaginationDTO<T> {
    private List<T> data;
    private boolean showFirstPage;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showEndPage;
    private Integer currentPage;
    private Integer totalPage;
    private List<Integer> pages=new ArrayList<>();

    public void setPagiNation(Integer currentPage
    ){


        this.currentPage=currentPage;
        //判断当前页左三右三是否合法，放入pages
        this.pages.add(currentPage);
        for (int i=1;i<=3;i++){
            if (currentPage-i>0){
                this.pages.add(0,currentPage-i);
            }
            if (currentPage+i<=this.totalPage){
                this.pages.add(currentPage+i);
            }
        }
        //判断是否有第一页，最后一页，前一页，后一页
        if (pages.contains(1)){
            this.showFirstPage=false;
        }else {
            this.showFirstPage=true;
        }
        if (pages.contains(this.totalPage)){
            this.showEndPage=false;
        }else {
            this.showEndPage=true;
        }
        if (currentPage!=1){
            this.showPrevious=true;
        }else{
            this.showPrevious=false;
        }
        if (currentPage == totalPage) {
            this.showNext=false;
        }else {
            this.showNext=true;
        }

    }
}

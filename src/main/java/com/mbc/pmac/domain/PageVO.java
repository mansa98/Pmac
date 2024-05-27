package com.mbc.pmac.domain;


import lombok.Data;

@Data
public class PageVO {
	
	/** 시작 페이지 */
	private int startPage;
	
	/** 마지막 페이지 */
	private int endPage;
	
	/** 총 페이지 수 */
	private int maxPage;
	
	/** 현재 페이지 */
	private int currPage;
	
	/** 총 인원/게시글 수 */
	private int listCount;	
	
	/** 이전 페이지 */
	private int prePage;
	
	/** 다음 페이지 */
	private int nextPage;
	
	public static int getMaxPage(int listCount, int limit) {
		// return (int)((double)listCount/limit+0.95); //0.95를 더해서 올림 처리
		return (int)((Math.floor((listCount - 1) / limit)) + 1);
	}
	
//	public static int getMaxPage(int listCount, int limit) {
//		return (int)((double)listCount/limit+0.95); //0.95를 더해서 올림 처리
//	}
	
	public static int getStartPage(int currPage) {
		// return  (((int) ((double)currPage / limit + 0.9)) - 1) * limit + 1;
		return (int)((Math.floor((currPage - 1) / 10)) * 10 + 1);
	}
	
	public static int getStartPage(int currPage, int limit) {
		return  (((int) ((double)currPage / limit + 0.9)) - 1) * limit + 1;
	}
	
	public static int getEndPage(int currPage) {
		
		return (int)(((Math.floor((currPage - 1) / 10)) + 1) * 10);
	}
	
	public static int getEndPage(int currPage, int limit) {
		return (int)(Math.floor(((currPage - 1)/limit) + 1) * limit);
	}
		
} //

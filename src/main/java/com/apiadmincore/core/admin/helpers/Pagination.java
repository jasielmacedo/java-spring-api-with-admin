package com.apiadmincore.core.admin.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Pagination 
{
	public static List<Integer> mountPagination(long totalItens, long limit)
	{		
		long pages = totalItens / limit;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i = 1; i <= pages; i++)
		{
			list.add(i);
		}
		
		return list;
	}
	
	public static final List<Integer> pg2pg(List<Integer> pages, int corner , int currentPage)
	{
		int total = pages.size();
		
		int view = (corner * 2) + 1;
		if(view > total)
			view = total;
		
		int max = total - currentPage;
		
		if(max < 0)
			max = 0;
		
		int min = currentPage-1;
		
		if(currentPage > corner && currentPage < total-corner)
		{
			if(max > corner)
				max = corner;
			
			if(min > corner)
				min = corner;
		}else {
			if(max > min)
				max = (view-1)-min;
			else
				min = (view-1)-max;
		}
		
		ArrayList<Integer> newPages = new ArrayList<Integer>();
		
		int i =0;
		for(i = min; i > 0; i--)
		{
			newPages.add(pages.get(((currentPage+1)-(i+1))-1));
		}
		
		newPages.add(currentPage);
		
		for(i = 0; i < max; i++)
		{
			newPages.add(pages.get((currentPage+(i+1))-1));
		}
		
		return newPages;
	}
}

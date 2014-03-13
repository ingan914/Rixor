package com.projectrixor.rixor.scrimmage.region;

import java.util.List;

import lombok.Getter;
import com.projectrixor.rixor.scrimmage.filter.Filter;

public class FilteredRegion {
	
	@Getter List<RegionGroup> regions;
	@Getter List<Filter> filters;
	
	public FilteredRegion(List<RegionGroup> regions, List<Filter> filters) {
		this.regions = regions;
		this.filters = filters;
	}
	
}

package com.example.dspagingrecyclerviewexample.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularMovieResponse {

@SerializedName("page")
@Expose
private Integer page;
@SerializedName("results")
@Expose
private List<MovieListData> results = null;
@SerializedName("total_pages")
@Expose
private Integer totalPages;
@SerializedName("total_results")
@Expose
private Integer totalResults;

public Integer getPage() {
return page;
}

public void setPage(Integer page) {
this.page = page;
}

public List<MovieListData> getResults() {
return results;
}

public void setResults(List<MovieListData> results) {
this.results = results;
}

public Integer getTotalPages() {
return totalPages;
}

public void setTotalPages(Integer totalPages) {
this.totalPages = totalPages;
}

public Integer getTotalResults() {
return totalResults;
}

public void setTotalResults(Integer totalResults) {
this.totalResults = totalResults;
}

}
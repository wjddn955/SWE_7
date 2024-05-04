package com.example.unimovie.rec;

import lombok.Data;

@Data
public class Movie {
    private String title;
    private String genre;
	private String season;
	public String rating;
    
	public Movie() {}

	public Movie(String season, String title, String genre, String rating) {

		this.season = season;
	    this.title = title;
        this.genre = genre;
		this.rating = rating;
	}

	
    public boolean equals(Movie m) {
        if (this == m) return true;
        if (!(m instanceof Movie)) return false;
        Movie movie = (Movie) m;
        return (this.title == m.getTitle()) &&
               (this.season == m.getSeason()) &&
               (this.genre == m.getGenre()) &&
               (this.rating == m.getRating());
    }

   
    public int hashCode() {
        return (int)Double.parseDouble(this.rating);
    }

 
    public String toString() {
        return "Movie{" +
               "title=\'" + title + '\'' +
               ", genre=\'" + genre + '\'' +
               ", season=\'" + season + '\'' +
               ", rating=\'" + rating + '\'' +
               '}';
    }

    public void setSeason(String season) {
		this.season = season;
	}

    public void setTitle(String title) {
		this.title = title;
	}

    public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getSeason() {
		return this.season;
	}

	public String getRating() {
		return this.rating;
	}

	public String getTitle() {
		return this.title;
	}
}


package br.danton.grawards.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
@Entity
@Table(name = "movie", indexes = {
	@Index(name = "ix_mv_winner_year", columnList = "winner, year", unique = false)
})
@NamedQueries({
	@NamedQuery(name = "Movie.find", query = "select m from Movie m where UPPER(:title) in('', UPPER(m.title))"),
	@NamedQuery(name = "Movie.delete", query = "delete from Movie m where m.id = :id"),
	@NamedQuery(name = "Movie.deleteAll", query = "delete from Movie")
})
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movie")
	@SequenceGenerator(name = "seq_movie", sequenceName = "seq_movie")
	private Integer id;

	@Column(nullable = false)
	private Integer year;

	@Column(nullable = false)
	private String title;

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "movie_studio",
			joinColumns = @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ms_movie_id")),
			inverseJoinColumns = @JoinColumn(name = "studio_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ms_studio_id")),
			indexes = {
				@Index(name = "uk_ms_movie_studio", columnList = "movie_id, studio_id", unique = true),
				@Index(name = "uk_ms_studio_movie", columnList = "studio_id, movie_id", unique = false)
			}
	)
	private List<Studio> studios;

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "movie_producer",
			joinColumns = @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mp_movie_id")),
			inverseJoinColumns = @JoinColumn(name = "producer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mp_producer_id")),
			indexes = {
				@Index(name = "uk_mp_movie_producer", columnList = "movie_id, producer_id", unique = true),
				@Index(name = "uk_mp_producer_movie", columnList = "producer_id, movie_id", unique = false)
			}
	)
	private List<Producer> producers;

	@Column(nullable = false)
	private Boolean winner;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Studio> getStudios() {
		return studios;
	}

	public void setStudios(List<Studio> studios) {
		this.studios = studios;
	}

	public List<Producer> getProducers() {
		return producers;
	}

	public void setProducers(List<Producer> producers) {
		this.producers = producers;
	}

	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

}

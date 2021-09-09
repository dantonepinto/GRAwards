package br.danton.grawards.model;

import br.danton.grawards.dto.StudioTO;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
@Entity
@Table(name = "studio", indexes = {
	@Index(name = "uk_studio_name", columnList = "name", unique = true)
})
@NamedQueries({
	@NamedQuery(name = "Studio.find", query = "select s from Studio s where UPPER(:name) in('', UPPER(s.name))"),
	@NamedQuery(name = "Studio.delete", query = "delete from Studio s where s.id = :id"),
	@NamedQuery(name = "Studio.deleteAll", query = "delete from Studio")
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "Studio.findByMinMaxIntervalBetweenAwards", query = ""
			+ "with ms_winner as (\n"
			+ "	select ms.*, mv.year from movie_studio ms join movie mv on mv.id = ms.movie_id where mv.winner = true\n"
			+ "), s as (\n"
			+ "	select s.name, (flwg_ms.year - prev_ms.year) as _interval, prev_ms.year as prev_win, flwg_ms.year as following_win\n"
			+ "		from studio s\n"
			+ "		inner join ms_winner flwg_ms on flwg_ms.studio_id = s.id\n"
			+ "		inner join ms_winner prev_ms on prev_ms.studio_id = s.id and  prev_ms.year <= flwg_ms.year and prev_ms.movie_id <> flwg_ms.movie_id\n"
			+ "		left  join ms_winner midd_ms on midd_ms.studio_id = s.id and (prev_ms.year <  midd_ms.year and midd_ms.year < flwg_ms.year)\n"
			+ "	where\n"
			+ "		midd_ms.studio_id is null\n"
			+ ")\n"
			+ "select distinct * from s where _interval in(\n"
			+ "	select min(_interval) from s\n"
			+ "	union all\n"
			+ "	select max(_interval) from s\n"
			+ ")\n"
			+ "order by _interval, prev_win, name\n"
			+ "", resultSetMapping = "StudioTO")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "StudioTO", classes = {
		@ConstructorResult(targetClass = StudioTO.class, columns = {
			@ColumnResult(name = "name"),
			@ColumnResult(name = "_interval"),
			@ColumnResult(name = "prev_win"),
			@ColumnResult(name = "following_win")
		})
	})
})
public class Studio {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_studio")
	@SequenceGenerator(name = "seq_studio", sequenceName = "seq_studio")
	private Integer id;

	@Column(nullable = false, length = 255)
	private String name;

	public Studio() {
	}

	public Studio(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

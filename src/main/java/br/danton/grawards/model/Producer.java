package br.danton.grawards.model;

import br.danton.grawards.dto.ProducerTO;
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
@Table(name = "producer", indexes = {
	@Index(name = "uk_producer_name", columnList = "name", unique = true)
})
@NamedQueries({
	@NamedQuery(name = "Producer.find", query = "select p from Producer p where UPPER(:name) in('', UPPER(p.name))"),
	@NamedQuery(name = "Producer.delete", query = "delete from Producer p where p.id = :id"),
	@NamedQuery(name = "Producer.deleteAll", query = "delete from Producer")
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "Producer.findByMinMaxIntervalBetweenAwards", query = ""
			+ "with mp_winner as (\n"
			+ "	select mp.*, mv.year from movie_producer mp join movie mv on mv.id = mp.movie_id where mv.winner = true\n"
			+ "), p as (\n"
			+ "	select p.name, (flwg_mp.year - prev_mp.year) as _interval, prev_mp.year as prev_win, flwg_mp.year as following_win\n"
			+ "		from producer p\n"
			+ "		inner join mp_winner flwg_mp on flwg_mp.producer_id = p.id\n"
			+ "		inner join mp_winner prev_mp on prev_mp.producer_id = p.id and  prev_mp.year <= flwg_mp.year and prev_mp.movie_id <> flwg_mp.movie_id\n"
			+ "		left  join mp_winner midd_mp on midd_mp.producer_id = p.id and (prev_mp.year <  midd_mp.year and midd_mp.year < flwg_mp.year)\n"
			+ "	where\n"
			+ "		midd_mp.producer_id is null\n"
			+ ")\n"
			+ "select distinct * from p where _interval in(\n"
			+ "	select min(_interval) from p\n"
			+ "	union all\n"
			+ "	select max(_interval) from p\n"
			+ ")\n"
			+ "order by _interval, prev_win, name\n"
			+ "", resultSetMapping = "ProducerTO")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "ProducerTO", classes = {
		@ConstructorResult(targetClass = ProducerTO.class, columns = {
			@ColumnResult(name = "name"),
			@ColumnResult(name = "_interval"),
			@ColumnResult(name = "prev_win"),
			@ColumnResult(name = "following_win")
		})
	})
})
public class Producer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_producer")
	@SequenceGenerator(name = "seq_producer", sequenceName = "seq_producer")
	private Integer id;

	@Column(nullable = false, length = 255)
	private String name;

	public Producer() {
	}

	public Producer(String name) {
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

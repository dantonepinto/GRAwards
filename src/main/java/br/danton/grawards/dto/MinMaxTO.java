package br.danton.grawards.dto;

import java.util.List;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class MinMaxTO<E> {

	private List<E> min;
	private List<E> max;

	public List<E> getMin() {
		return min;
	}

	public void setMin(List<E> min) {
		this.min = min;
	}

	public List<E> getMax() {
		return max;
	}

	public void setMax(List<E> max) {
		this.max = max;
	}

}

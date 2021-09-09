package br.danton.grawards.dto;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class StudioTO {

	private String studio;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;

	public StudioTO() {
	}

	public StudioTO(String studio, Integer interval, Integer previousWin, Integer followingWin) {
		this.studio = studio;
		this.interval = interval;
		this.previousWin = previousWin;
		this.followingWin = followingWin;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getPreviousWin() {
		return previousWin;
	}

	public void setPreviousWin(Integer previousWin) {
		this.previousWin = previousWin;
	}

	public Integer getFollowingWin() {
		return followingWin;
	}

	public void setFollowingWin(Integer followingWin) {
		this.followingWin = followingWin;
	}

}

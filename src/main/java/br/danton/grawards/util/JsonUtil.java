package br.danton.grawards.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Danton Estevam Pinto <dantonepinto@gmail.com>
 */
public class JsonUtil {

	public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + "; charset=utf-8";
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}

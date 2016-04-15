package co.xarx.trix.services.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostSearchParams {

	private String query;
	private Integer author;
	private List<Integer> stations;
	private String state;
	private String from;
	private String until;
	private List<Integer> categories;
	private List<String> tags;
}

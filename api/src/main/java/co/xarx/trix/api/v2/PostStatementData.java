package co.xarx.trix.api.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostStatementData extends AbstractStatementData {

	private String query;
	private List<Integer> authors = new ArrayList<>();
	private List<Integer> stations = new ArrayList<>();
	private String state;
	private String from;
	private String until;
	private List<Integer> categories = new ArrayList<>();
	private List<String> tags = new ArrayList<>();
	private List<String> orders = new ArrayList<>();
}

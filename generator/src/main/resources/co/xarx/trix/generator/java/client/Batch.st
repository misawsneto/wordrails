Batch(package, facade, entities) ::=

<<package $package$;

import java.util.ArrayList;
import java.util.List;

public class Batch {
	private I$facade$ facade;
	private List<Command> commands;
		
	Batch(I$facade$ facade) {
		this.facade = facade;
		this.commands = new ArrayList<Command>();
	}

	public void execute() {
		facade.postBatch(commands);
	}

$entities:BatchMethods(); separator="\n\n"$
}

class Command {
	String command = getClass().getName();
}

$entities:EntityCommands(); separator="\n\n"$>>
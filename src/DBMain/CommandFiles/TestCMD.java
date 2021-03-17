package DBMain.CommandFiles;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;

public class TestCMD {
	CMDType command;

	//testing on this level may be done by black box testing? I need to look further at this to work out
	//how to access protected methods + handle exceptions being thrown

	public TestCMD(CMDType command) {
		this.command = command;
		testIsItFinalCommand();
	}

	private void testIsItFinalCommand() {
		assert(command.isThisCommandEnd(null));
		assert(!command.isThisCommandEnd("hello"));
//		assert(command.isDBNameValid("hello"));
	}
}

package DBMain.ParseExceptions;

import DBMain.DBEnums.DomainType;

public abstract class DomainException extends SyntaxError{
	protected DomainType domain;
}

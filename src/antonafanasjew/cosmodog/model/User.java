package antonafanasjew.cosmodog.model;

public class User extends CosmodogModel {

    private static final long serialVersionUID = -3589928560915799256L;

    private String userName;

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }
    
}

package world;

import com.gigaspaces.annotation.pojo.SpaceId;

public class TileTuple {
	
	int tileid;
	
	public TileTuple(){
		
	}
	
	public TileTuple(int id){
		tileid = id;
	}
	
    @SpaceId
    public Integer getSsn() {
        return tileid;
    }
    
    public void setSsn(Integer ssn) {
        this.tileid = ssn;
    }


}

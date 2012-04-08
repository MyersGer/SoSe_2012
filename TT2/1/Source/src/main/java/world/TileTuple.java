package world;

import com.gigaspaces.annotation.pojo.SpaceId;

public class TileTuple {
	
	Integer tileid;
	
	public TileTuple(){
		
	}
	
	public TileTuple(Integer id){
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

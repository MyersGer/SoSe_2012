package world;

import com.gigaspaces.annotation.pojo.SpaceId;

public class AreaTuple {
	
	Integer areaId;
	
	public AreaTuple(){
		
	}
	
	public AreaTuple(Integer areaId){
		this.areaId = areaId;
	}
	
    @SpaceId
    public Integer getSsn() {
        return areaId;
    }
    
    public void setSsn(Integer ssn) {
        this.areaId = ssn;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaTuple other = (AreaTuple) obj;
		if (areaId == null) {
			if (other.areaId != null)
				return false;
		} else if (!areaId.equals(other.areaId))
			return false;
		return true;
	}

}

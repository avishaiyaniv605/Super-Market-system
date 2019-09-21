package PersistenceLayer.Inventory;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.Product;

import java.io.Serializable;

public class StockProductKey implements Serializable {
    private Product pID;
    private BranchProfile branch;

    public Product getpID() {
        return pID;
    }

    public void setpID(Product pID) {
        this.pID = pID;
    }

    public BranchProfile getBranch() {
        return branch;
    }

    public void setBranch(BranchProfile branch) {
        this.branch = branch;
    }

    public StockProductKey(Product pID, BranchProfile branch) {
        this.pID = pID;
        this.branch = branch;
    }
    public StockProductKey() {
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((branch == null) ? 0 : branch.hashCode());
        result = prime * result + pID.hashCode();
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
        StockProductKey other = (StockProductKey) obj;
        if (branch == null) {
            if (other.branch != null)
                return false;
        } else if (!branch.equals(other.branch))
            return false;
        if (pID != other.pID)
            return false;
        return true;
    }

}

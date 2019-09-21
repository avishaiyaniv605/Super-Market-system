package BusinessLayer.Inventory;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.Inventory.Orders.OrderItem;

import java.util.List;

public interface minimumAlert {

    boolean placeOrder(List<OrderItem> items, BranchProfile branch);

}

package BusinessLayer.DeliveryModule;

import java.util.Comparator;

public class TruckComparor implements Comparator<TruckProfile> {


    @Override
    public int compare(TruckProfile o1, TruckProfile o2) {

         return (o1.getMaxWeight()-o1.getTruckWeight())- (o2.getMaxWeight()-o2.getTruckWeight());
    }
}

package BusinessLayer.Enums;


public  enum  EnumTypeJob {

    shiftManager("Shift Manager"),
    storekeeper("Storekeeper"),
    cashier("Cashier"),
    driver("Driver"),
    logisticsManager("Logistics Manager"),
    storeManger("Store Manager");

    private String value;
    EnumTypeJob(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    public static EnumTypeJob getEnum(String value){
        for(EnumTypeJob v: values()){
            if(v.getValue().equalsIgnoreCase(value)) return v;
        }
        throw new IllegalArgumentException();
    }
    public String toString(){
        return this.value;
    }
}


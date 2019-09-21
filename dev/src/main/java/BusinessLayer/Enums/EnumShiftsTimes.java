package BusinessLayer.Enums;

import BusinessLayer.Parsers.EmployeeParser;

public enum EnumShiftsTimes {
    Morning{
        public String toString(){
            return "Morning: ["+ EmployeeParser.START_MORNING_SHIFT+"-"+EmployeeParser.END_MORNING_SHIFT+"]";
        }
    },
    Evening {
        public String toString(){
            return "Evening: ["+EmployeeParser.START_EVENING_SHIFT+"-"+EmployeeParser.END_EVENING_SHIFT+"]";
        }

    }
}

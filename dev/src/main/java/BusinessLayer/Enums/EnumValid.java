package BusinessLayer.Enums;

public enum  EnumValid {
    year,
    month,
    salary,
    termsOfEmployment{
        public String toString(){
            return "terms of employment";
        }
    },
    jobs,
    timeShift{
        public String toString(){
            return "\n"+"1.\t"+EnumShiftsTimes.Morning.toString()+'\n'+
                    "2.\t"+EnumShiftsTimes.Evening.toString()+'\n';
        }
    },
    ID,
    shiftsTimes,
    password,
    numberPositvie{
        public String toString(){
            return "a positive number";
        }
    },
    license

}

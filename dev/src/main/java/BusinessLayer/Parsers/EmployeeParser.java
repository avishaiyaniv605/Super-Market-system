package BusinessLayer.Parsers;

import BusinessLayer.Enums.License;
import BusinessLayer.EmployeeModule.*;
import BusinessLayer.Enums.EnumShiftsTimes;
import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Enums.EnumValid;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings({"Duplicates", "deprecation"})
public class EmployeeParser {

    public static final Integer START_EVENING_SHIFT = 15;
    public static final Integer END_EVENING_SHIFT = 21;
    public static final Integer START_MORNING_SHIFT = 8;
    public static final Integer END_MORNING_SHIFT = 15;

    private DatabaseManager databaseManager;
    private EmployeeProfile employeeProfileLogin;

    //region <Initial>
    /**
     * constructor
     */
    public EmployeeParser(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.databaseManager = databaseManager;
        this.employeeProfileLogin = null;

    }

    /**
     * add manger and branches
     */

    //region <Methods of Employee profile>

    /**
     * add a new Employee
     * @param firstName
     * @param lastName
     * @param eId
     * @param bankAccount
     * @param salary
     * @param termsOfEmployment
     * @param isManager
     * @return
     */
    public boolean register(String firstName, String lastName, String eId, String bankAccount, Integer salary, String termsOfEmployment, Integer isManager) {
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            Date date = new Date();
            if (choosenEmployeeProfile != null) return false;
            EmployeeProfile newEmployeeProfile = new EmployeeProfile(firstName, lastName, eId, bankAccount,
                    salary, termsOfEmployment, date, isManager);
            databaseManager.getSession().save(newEmployeeProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * for login to the system with ID Employee
     * @param eId
     * @return
     */
    public boolean login(String eId) {
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            if (choosenEmployeeProfile != null ) {
                employeeProfileLogin = choosenEmployeeProfile;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * @return EmployeeProfile that login
     */
    public EmployeeProfile getLoggedInEmployee() {
        return this.employeeProfileLogin;
    }

    /**
     * check if exist Employee with the same id
     * @param eId
     * @return
     */
    public boolean checkIsManager(String eId) {
        try {
            EmployeeProfile chooseEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            if (chooseEmployeeProfile != null) {
                return chooseEmployeeProfile.getIsManager() == 1;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * return String with the details of the employee
     * @param eId
     * @return
     */
    public String getEmployeeDetails(String eId) {
        String res = null;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);

            List<EmployeeConstraints> employeeConstraints = getListConstraintsEmployee(eId);

            List<EmployeeJobs> employeeJobsList = getEmployeeJobs(eId);

            if (choosenEmployeeProfile != null) {
                res = choosenEmployeeProfile.toString() + "\n";
                if (employeeConstraints != null) {
                    if (!employeeConstraints.isEmpty()) {
                        res = res + "\nConstraints:" + "\n";
                    }
                    for (EmployeeConstraints e : employeeConstraints) {
                        res = res + e.toString() + "\n";
                    }
                }
                if (employeeJobsList != null) {
                    if (!employeeJobsList.isEmpty()) {
                        res = res + "\nJobs:" + "\n";
                    }
                    for (EmployeeJobs e : employeeJobsList) {
                        res = res + e.toString() + "\n";
                    }
                }
            }
        } catch (Exception e) {
            return res;
        }
        return res;
    }

    //endregion

    //region <Methods that check valid of parameter>


    /**
     * Check if String is valid according be EnumValid
     * @param e
     * @param s
     * @return
     */
    public boolean checkValid(EnumValid e, String s) {
        switch (e) {
            case year:
                return checkIsValidYear(s);
            case timeShift:
                return checkIsValidTimeShift(s);
            case jobs:
                return checkIsValidJob(s);
            case ID:
                return checkIsValidID(s);
            case salary:
                return checkIsValidSalary(s);
            case termsOfEmployment:
                return checkIsValidTerms(s);
            case shiftsTimes:
                return checkIsValidShiftTimes(s);
            case numberPositvie:
                return checkIsPositiveNumber(s);
            case license:
                return checkLicense(s);
        }
        return false;
    }

    private boolean checkLicense(String s) {
        if (checkIsInteger(s)) {
            int num = Integer.parseInt(s);
            int size = License.values().length;
            return num <= size && num >= 1;
        }
        return false;
    }

    /**
     * check if the string is integer  s>=1 && s<=size
     * @param size
     * @param s
     * @return
     */
    public boolean checkIsValidRange(int size, String s) {
        if (checkIsInteger(s)) {
            int num = Integer.parseInt(s);
            return num <= size && num >= 1;
        }
        return false;
    }

    /**
     * check if is positive number
     * @param s
     * @return
     */
    private boolean checkIsPositiveNumber(String s) {
        if (this.checkIsInteger(s)) {
            return s.length() > 0;
        }
        return false;
    }



    /**
     * check if string he in the range of shift times
     * @param s
     * @return
     */
    private boolean checkIsValidShiftTimes(String s) {
        if (this.checkIsInteger(s)) {
            int num = Integer.parseInt(s);
            return EnumShiftsTimes.values().length >= num;
        }
        return false;
    }

    /**
     * check if is valid terms of employee
     * @param s
     * @return
     */
    private boolean checkIsValidTerms(String s) {
        return true;
    }

    /**
     * check is valid constriants
     * @param id
     * @param day
     * @param start
     * @param end
     * @return
     */
    public boolean checkIsValidConstraints(String id, Integer day, Integer start, Integer end) {
        if (checkIsValidWeekDay(day) && checkIsValidHours(start, end) ) {
            return true;
        }
        return false;
    }

    /**
     * check if exist constraints in specif day
     * @param id
     * @param day
     * @return
     */
    @SuppressWarnings("Duplicates")
    public boolean checkIfExistConstraintsAtThisDay(String id, Integer day) {
        boolean result = false;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, id);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeConstraints> criteriaQuery = cb.createQuery(EmployeeConstraints.class);
            Root<EmployeeConstraints> root = criteriaQuery.from(EmployeeConstraints.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.and(cb.equal((root.get("day")), day),cb.equal(root.get("ID"),choosenEmployeeProfile)));
            Query<EmployeeConstraints> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<EmployeeConstraints> list = query.getResultList();
            result=list!=null && !list.isEmpty();
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    /**
     * check if The input is a valid day
     * @param day
     * @return
     */
    private boolean checkIsValidWeekDay(Integer day) {
        return day >= 1 && day <= 7;
    }

    /**
     * check if is valid hours
     * @param start
     * @param end
     * @return
     */
    private boolean checkIsValidHours(Integer start, Integer end) {
        if (start >= 0 && end >= 0 && start <= 24 && end <= 24 && start < end) {
            return true;
        }
        return false;
    }

    /**
     * check if is valid salary
     * @param s
     * @return
     */
    private boolean checkIsValidSalary(String s) {
        return this.checkIsInteger(s);
    }

    /**
     * check if is valid job
     * @param s
     * @return
     */
    private boolean checkIsValidJob(String s) {
        if (checkIsInteger(s)) {
            Integer num = Integer.parseInt(s);
            return EnumTypeJob.values().length >= num;
        }
        return false;
    }

    /**
     * check if is valid ID
     * @param s
     * @return
     */
    private boolean checkIsValidID(String s) {
        //check if its number
        if (this.checkIsInteger(s)) {
            //check is ID is exist
            return !this.checkIfUserExist(s);
        }
        return false;
    }

    /**
     * check is valid year
     * @param year
     * @return
     */
    private boolean checkIsValidYear(String year) {
        if (checkIsInteger(year)) {
            int num = Integer.parseInt(year);
            int thatYear = Calendar.getInstance().get(Calendar.YEAR);
            return num >= thatYear;
        }
        return false;
    }

    /**
     * check is valid month
     * @param month
     * @return
     */
    public boolean checkIsValidMonth(String month,String year) {
        if (checkIsInteger(month)) {
            int num = Integer.parseInt(month);
            int thatYear = Calendar.getInstance().get(Calendar.YEAR);
            int thatMounth = Calendar.getInstance().get(Calendar.MONTH)+1;
            boolean checkMounthInThisYear=true;
            if(thatYear == Integer.parseInt(year)){
                checkMounthInThisYear=thatMounth<=num;
            }
            return num >= 1 && num <= 12  && checkMounthInThisYear;
        }
        return false;
    }

    /**
     * check is valid day
     * @param day
     * @return
     */
    public boolean checkIsValidDay(String day,String year,String month) {
        if (checkIsInteger(day)) {
            int num = Integer.parseInt(day);
            YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
            int thatDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1;
            int thatYear = Calendar.getInstance().get(Calendar.YEAR);
            int thatMonth = Calendar.getInstance().get(Calendar.MONTH);
            boolean ThatMounthAndYear=true;
            if(thatYear==Integer.parseInt(year) && thatMonth == Integer.parseInt(month)){
                ThatMounthAndYear = thatDay<=num;
            }
            int daysInMonth = yearMonthObject.lengthOfMonth();
            return num >= 1 && num <= daysInMonth && ThatMounthAndYear;
        }
        return false;
    }

    /**
     * check is Integer
     * @param str
     * @return
     */
    public boolean checkIsInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
            // is an integer!
        } catch (NumberFormatException e) {
            // not an integer!
            return false;
        }
    }

    /**
     *check if is valid time shift
     * @param s
     * @return
     */
    private boolean checkIsValidTimeShift(String s) {
        return s.equals("1") || s.equals("2");
    }

    /**
     * check if the shift exist
     * @param branch
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @return
     */
    public boolean checkIfShiftExist(String branch, String year, String month, String day, String timeShift) {
        return getSpecificShift(year, month, day, timeShift, branch) != null;
    }

    //endregion


    //region <Method for shift>

    /**
     * get employee that can work on the job and not work in other place in this time according by constraints can work
     *
     * @param e
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @return
     */
    public List<EmployeeJobs> getListEmployeesOnJob(EnumTypeJob e, String year, String month, String day, String timeShift) {
        List<ShiftsEmployees> employeeCantWorking = getEmployeeWorkOnDate(year, month, day, timeShift);
        List<EmployeeJobs> employeesWorkInTheJob = getListEmployeeKnowDoTheJob(e);
        List<EmployeeJobs> result = new ArrayList<>();
        //+1 for do be like in Israel
        int dayInWeek=(getTheDayOfWeekAccordingByDate(year,month,day)+1)%8+1;
        if (employeesWorkInTheJob != null && !employeesWorkInTheJob.isEmpty()) {
            for (EmployeeJobs employeeJobs : employeesWorkInTheJob) {
                boolean flag = true;
                List<EmployeeConstraints> employeeConstraints = getListConstraintsEmployee(employeeJobs.geteID().getID());
                for (int i = 0; flag && i < employeeConstraints.size(); i++) {
                    int start = -1;
                    int end = -1;
                    if (timeShift.equals(EnumShiftsTimes.Evening.toString())) {
                        start = START_EVENING_SHIFT;
                        end = END_EVENING_SHIFT;
                    }
                    if (timeShift.equals(EnumShiftsTimes.Morning.toString())) {
                        start = START_MORNING_SHIFT;
                        end = END_MORNING_SHIFT;
                    }
                    if (employeeConstraints.get(i).checkIfCanWork(dayInWeek,start, end)) {
                        boolean canWork = true;
                        for (ShiftsEmployees shiftsEmployees : employeeCantWorking) {
                            if (shiftsEmployees.getEmployeeProfile().getID().equals(employeeJobs.geteID().getID())) {
                                canWork = false;
                            }
                        }
                        if (canWork) {
                            result.add(employeeJobs);
                            flag = false;
                        }
                    }
                }
            }

        }

        return result;
    }

    /**
     * return the day in week according by date
     * @param year
     * @param month
     * @param day
     * @return
     */
    private int getTheDayOfWeekAccordingByDate(String year, String month, String day){
        String input = day+"/"+month+"/"+year;
        return LocalDate.parse(input,DateTimeFormatter.ofPattern("d/M/yyyy")).getDayOfWeek().getValue();
    }

    public Shifts getShiftInPlace(Integer choose){
        List<Shifts> list=this.getShifts();
        return list.get(choose);
    }

    public List<String> convertShiftToListOfEmployee(Shifts shifts){
        List<ShiftsEmployees> list=shifts.shiftsEmployees();
        List<String> result=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            result.add(list.toString());
        }
        return result;
    }
    public List<String> convertListShiftToString(List<Shifts> list){
        List<String> result=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            result.add(list.get(i).toString());
        }

        return result;
    }

    /**
     * Returns ths shifts of the @branchProfile branch
     * @param shifts - The list where the shifts are stored
     * @param branchProfile - The branch to get his shifts
     * @return - List of Strings represent the shifts of all @branchProfile shifts
     */
    public List<String> convertBranchShiftsToString(List<Shifts> shifts, BranchProfile branchProfile){
        List<String> result=new ArrayList<>();
        for(int i=0;i<shifts.size();i++){
            if(shifts.get(i).getNameBranch().getBranchName().equals(branchProfile.getBranchName()))
                result.add(shifts.get(i).toString());
        }

        return result;
    }

    /**
     * return a list of EmployeeJobs that know do the job
     * @param enumTypeJob
     * @return
     */
    private List<EmployeeJobs> getListEmployeeKnowDoTheJob(EnumTypeJob enumTypeJob) {
        List<EmployeeJobs> result = null;
        try {
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeJobs> criteriaQuery = cb.createQuery(EmployeeJobs.class);
            Root<EmployeeJobs> root = criteriaQuery.from(EmployeeJobs.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.equal((root.get("job")), enumTypeJob.toString()));
            Query<EmployeeJobs> query = databaseManager.getSession().createQuery(criteriaQuery);
            result = query.getResultList();

        } catch (Exception e) {
            return result;
        }
        return result;
    }

    /**
     * get all the employee that work in this date
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @return
     */
    public List<ShiftsEmployees> getEmployeeWorkOnDate(String year, String month, String day, String timeShift) {
        List<ShiftsEmployees> result = new ArrayList<>();
        try {
            Date date = getDateOfShift(year, month, day, timeShift);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<ShiftsEmployees> criteriaQuery = cb.createQuery(ShiftsEmployees.class);
            Root<ShiftsEmployees> root = criteriaQuery.from(ShiftsEmployees.class);
            criteriaQuery.select(root);
            Query<ShiftsEmployees> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<ShiftsEmployees> list = query.getResultList();
            if (list != null && !list.isEmpty()) {
                for (ShiftsEmployees shiftsEmployees : list) {
                    if (shiftsEmployees.getShift().getDate().compareTo(date) == 0) {
                        result.add(shiftsEmployees);
                    }
                }
            }
        } catch (Exception e) {
            return result;
        }
        return result;

    }

    public boolean createShift(String year, String month, String day, String timeShift, String branch, String shiftManagerId) {
        try {
            Date date = getDateOfShift(year, month, day, timeShift);
            BranchProfile b = databaseManager.getSession().get(BranchProfile.class, branch);
            EmployeeProfile mangerEmployee = databaseManager.getSession().get(EmployeeProfile.class, shiftManagerId);
            Shifts newShifts = new Shifts(date, b, mangerEmployee, timeShift);
            databaseManager.getSession().save(newShifts);
            databaseManager.commitAndFLush();
            addEmployeeToShift(shiftManagerId, year, month, day, timeShift, branch, EnumTypeJob.shiftManager.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Date getDateOfShift(String year, String month, String day, String timeShift) {
        String pattern = "yyyy-M-d";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = null;
        try {
            date = simpleDateFormat.parse(year+"-"+month+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        Date date = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)).getTime();
        if (timeShift.equals(EnumShiftsTimes.Morning.toString())) {
            date.setMinutes(0);
            date.setHours(START_MORNING_SHIFT);
            date.setSeconds(0);
        }
        if (timeShift.equals(EnumShiftsTimes.Evening.toString())) {
            date.setMinutes(0);
            date.setHours(START_EVENING_SHIFT);
            date.setSeconds(0);
        }

        return date;
    }

    public boolean removeEmployeeFromShift(String eId, String year, String month, String day, String timeShift, String branch) {
        try {
            EmployeeProfile employeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            Date date = getDateOfShift(year, month, day, timeShift);
            BranchProfile branchProfile = databaseManager.getSession().get(BranchProfile.class, branch);
            Shifts shifts = new Shifts(date, branchProfile, timeShift);
            ShiftsEmployees toDelete = new ShiftsEmployees(shifts, employeeProfile);
            if (toDelete.getJob().equals(EnumTypeJob.shiftManager)) {
                return false;
            }
            this.databaseManager.getSession().delete(toDelete);
            this.databaseManager.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<ShiftsEmployees> getListEmployeesWorking(String job, String year, String month, String day, String timeShift, BranchProfile branch) {
        Date date = getDateOfShift(year, month, day, timeShift);
        CriteriaBuilder cb = databaseManager.getCb();
        CriteriaQuery<ShiftsEmployees> criteriaQuery = cb.createQuery(ShiftsEmployees.class);
        Root<ShiftsEmployees> root = criteriaQuery.from(ShiftsEmployees.class);
        criteriaQuery.select(root);
        criteriaQuery.where(cb.and(cb.and(cb.equal(cb.equal(root.get("date"), date), cb.equal(root.get("timeOfShift"), timeShift)))),
                cb.and(cb.equal((root.get("job")), job)), cb.equal(root.get("nameBranch"), branch));
        Query<ShiftsEmployees> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<ShiftsEmployees> shiftsEmployees = query.getResultList();

        return shiftsEmployees;
    }

    public boolean addEmployeeToShift(String eId, String year, String month, String day, String timeShift, String branch, String job) {
        try {
            Date date = getDateOfShift(year, month, day, timeShift);
            BranchProfile b = databaseManager.getSession().get(BranchProfile.class, branch);

            EmployeeProfile employeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            CriteriaBuilder cb = databaseManager.getCb();

            CriteriaQuery<Shifts> query = cb.createQuery(Shifts.class);

            Root<Shifts> root = query.from(Shifts.class);

            query = query.select(root)
                    .where(cb.and(cb.and(cb.equal(root.get("date"), date), (cb.equal(root.get("nameBranch"), b)), cb.equal(root.get("timeOfShift"), timeShift))));

            try {
                Shifts shifts = databaseManager.getSession().createQuery(query).getSingleResult();
                if (shifts.getShiftManager() != null && job.equals(EnumTypeJob.shiftManager)) {
                    return false;
                }
                ShiftsEmployees newEmployeeToShift = new ShiftsEmployees(shifts, employeeProfile, job);
                shifts.addShiftsEmployee(newEmployeeToShift);
                databaseManager.getSession().save(newEmployeeToShift);
                databaseManager.commitAndFLush();
            } catch (NoResultException nre) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean logout() {
        this.employeeProfileLogin = null;
        return true;
    }

    public boolean checkIfUserExist(String eId) {
        EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
        return choosenEmployeeProfile != null;
    }

    public boolean changeFirstName(String firstName, String eId) {
        EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
        choosenEmployeeProfile.setFirstName(firstName);
        databaseManager.commitAndFLush();
        return true;
    }

    public boolean changeLastName(String lastName, String eId) {
        EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
        choosenEmployeeProfile.setLastName(lastName);
        databaseManager.commitAndFLush();
        return true;
    }

    public boolean changeTermsOfEmployment(String termsOfEmployment, String eId) {
        EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
        choosenEmployeeProfile.setTermsOfEmployment(termsOfEmployment);
        databaseManager.commitAndFLush();
        return true;
    }

    public boolean changeSalary(String salary, String eId) {
        Integer num;
        if (checkIsInteger(salary) && this.employeeProfileLogin.getIsManager()==1) {
            num = Integer.parseInt(salary);
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            choosenEmployeeProfile.setSalary(num);
            databaseManager.commitAndFLush();
            return true;
        }
        return false;
    }

    public boolean changeBankAccount(String bankAccount, String eId) {
        EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
        choosenEmployeeProfile.setBankAccount(bankAccount);
        databaseManager.commitAndFLush();
        return true;
    }

    public boolean removeEmployee(String eId) {
        if (this.employeeProfileLogin != null && employeeProfileLogin.getIsManager() == 1 && ! employeeProfileLogin.getID().equals(eId)) {
            try {
                if( removeAllEmployeeFromAllTables(eId)){
                    EmployeeProfile toDelete = databaseManager.getSession().get(EmployeeProfile.class, eId);
                    this.databaseManager.getSession().delete(toDelete);
                    this.databaseManager.commitAndFLush();
                }
                else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean removeAllEmployeeFromAllTables(String eId){
        try {
            EmployeeProfile employeeProfile = getEmployeeProfile(eId);
            List<Shifts> shiftsEmployees = getShifts();
            for(Shifts shifts :shiftsEmployees){
               if( shifts.shiftsEmployees().contains(employeeProfile) ){
                   return false;
               }
            }

            List<EmployeeConstraints> employeeConstraints =  getListConstraintsEmployee(eId);
            for(EmployeeConstraints employeeConstraints1: employeeConstraints){
                this.databaseManager.getSession().delete(employeeConstraints1);
                this.databaseManager.commitAndFLush();
            }
            List<EmployeeJobs> employeeJobs =getEmployeeJobs(eId);
            for(EmployeeJobs employeeJobs1: employeeJobs){
                this.databaseManager.getSession().delete(employeeJobs1);
                this.databaseManager.commitAndFLush();
            }

        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public boolean addConstraints(Integer day,Integer startConstriants, Integer endConstriants, String eId) {
        try {
            EmployeeProfile employeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);
            List<EmployeeConstraints> employeeConstraints = getListConstraintsEmployee(eId);
            EmployeeConstraints newEmployeeCon = new EmployeeConstraints(day,startConstriants, endConstriants, employeeProfile);
            databaseManager.getSession().save(newEmployeeCon);
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public EmployeeProfile addJob(String job, String eId) {
        try {
            EmployeeProfile employeeProfile = databaseManager.getSession().get(EmployeeProfile.class, eId);

            EmployeeJobs newEmployeeJob = new EmployeeJobs(employeeProfile, job);
            databaseManager.getSession().save(newEmployeeJob);
            databaseManager.commitAndFLush();

            return employeeProfile;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * get constraints of employee
     *
     * @param id
     * @return
     */
    @SuppressWarnings("Duplicates")
    public List<EmployeeConstraints> getListConstraintsEmployee(String id) {
        List<EmployeeConstraints> employeeConstraints = null;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, id);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeConstraints> criteriaQuery = cb.createQuery(EmployeeConstraints.class);
            Root<EmployeeConstraints> root = criteriaQuery.from(EmployeeConstraints.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.equal(root.get("ID"), choosenEmployeeProfile));
            Query<EmployeeConstraints> query = databaseManager.getSession().createQuery(criteriaQuery);
            employeeConstraints = query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();

        }
        return employeeConstraints;
    }

    /**
     * @param id
     * @return
     */
    @SuppressWarnings("Duplicates")
    public List<EmployeeJobs> getEmployeeJobs(String id) {
        List<EmployeeJobs> result = null;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, id);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeJobs> criteriaQuery = cb.createQuery(EmployeeJobs.class);
            Root<EmployeeJobs> root = criteriaQuery.from(EmployeeJobs.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.equal(root.get("ID"), choosenEmployeeProfile));
            Query<EmployeeJobs> query = databaseManager.getSession().createQuery(criteriaQuery);
            result = query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();

        }
        return result;
    }

    /**
     * remove Constraints of employee
     * @param id
     * @return
     */
    public boolean removeConstraint(String id, Integer day) {
        EmployeeConstraints employeeConstraints = getSpecficListConstraintsOfEmployee(id, day);
        if (employeeConstraints != null) {
            this.databaseManager.getSession().delete(employeeConstraints);
            this.databaseManager.commitAndFLush();
            return true;
        }
        return false;
    }


    /**
     * get specif constraint of employee
     *
     * @param id
     * @return
     */
    public EmployeeConstraints getSpecficListConstraintsOfEmployee(String id, Integer day) {
        EmployeeConstraints result = null;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, id);

            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeConstraints> criteriaQuery = cb.createQuery(EmployeeConstraints.class);
            Root<EmployeeConstraints> root = criteriaQuery.from(EmployeeConstraints.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.and(cb.and(cb.equal(root.get("ID"), choosenEmployeeProfile), (cb.equal(root.get("day"), day)))));
            Query<EmployeeConstraints> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<EmployeeConstraints> employeeConstraints = query.getResultList();
            if (!employeeConstraints.isEmpty()) {
                result = employeeConstraints.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * get specif constraint of employee
     *
     * @param id
     * @param startConstriants
     * @param endConstriants
     * @param
     * @return
     */
    public EmployeeConstraints getSpecficListConstraintsRangeOfEmployee(String id, Integer day, Integer startConstriants, Integer endConstriants) {
        EmployeeConstraints result = null;
        try {
            EmployeeProfile choosenEmployeeProfile = databaseManager.getSession().get(EmployeeProfile.class, id);

            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeConstraints> criteriaQuery = cb.createQuery(EmployeeConstraints.class);
            Root<EmployeeConstraints> root = criteriaQuery.from(EmployeeConstraints.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.and(cb.and(cb.or
                            (cb.lessThan(root.get("endConstriants"), endConstriants)
                                    , (cb.greaterThan(root.get("startConstriants"), startConstriants))),
                    cb.equal(root.get("ID"), choosenEmployeeProfile))
                    , cb.equal(root.get("day"), day)));
            Query<EmployeeConstraints> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<EmployeeConstraints> employeeConstraints = query.getResultList();
            if (!employeeConstraints.isEmpty()) {
                result = employeeConstraints.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * get specif constraint of employee
     *
     * @param startConstriants
     * @param endConstriants
     * @return
     */
    public List<EmployeeConstraints> getRangeListConstraintsOfEmployee(Integer startConstriants, Integer endConstriants) {
        List<EmployeeConstraints> result = null;
        try {

            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeConstraints> criteriaQuery = cb.createQuery(EmployeeConstraints.class);
            Root<EmployeeConstraints> root = criteriaQuery.from(EmployeeConstraints.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.and(cb.greaterThan(root.get("startConstriants"), startConstriants), (cb.lessThan(root.get("endConstriants"), endConstriants))));
            Query<EmployeeConstraints> query = databaseManager.getSession().createQuery(criteriaQuery);
            result = query.getResultList();

        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public List<String> createListStringBranch(List<BranchProfile> branches) {
        List<String> result = new ArrayList<>();
        for (BranchProfile b : branches) {
            result.add(b.getBranchName());
        }
        return result;
    }

    public List<String> createListStringEmployeesOnJob(List<EmployeeJobs> employeeJobsList) {
        List<String> result = new ArrayList<>();
        for (EmployeeJobs employeeJobs : employeeJobsList) {
            result.add(employeeJobs.geteID().getFirstName() + " " + employeeJobs.geteID().getLastName() + ": " + employeeJobs.geteID().getID());
        }
        return result;
    }

    public List<String> createListStringEmployeesWorking(List<ShiftsEmployees> shiftsEmployees) {
        List<String> result = new ArrayList<>();
        for (ShiftsEmployees s : shiftsEmployees) {
            result.add(" Full Name :" + s.getEmployeeProfile().getFirstName() + " " + s.getEmployeeProfile().getLastName()
                    + " \n\t ID: " + s.getEmployeeProfile().getID());
        }
        return result;
    }

    public int addNumOfEmployeeOnJob(int numOfEmployees, String year, String month, String day, String timeShift, String branch, EnumTypeJob enumTypeJob) {
        int counter = 0;
        List<EmployeeJobs> employeeJobsList = getListEmployeesOnJob(enumTypeJob, year, month, day, timeShift);
        if (employeeJobsList != null && !employeeJobsList.isEmpty()) {
            for (; counter < employeeJobsList.size() && counter < numOfEmployees; counter++) {
                addEmployeeToShift(employeeJobsList.get(counter).geteID().getID(), year, month, day, timeShift, branch, enumTypeJob.toString());
            }

        }
        return counter;
    }

    public String getIdOfMangerShiftCanWork(String year, String month, String day, String timeShift, String branch, String toString) {
        List<EmployeeJobs> employeeJobsList = getListEmployeesOnJob(EnumTypeJob.shiftManager, year, month, day, timeShift);
        if (employeeJobsList != null && !employeeJobsList.isEmpty()) {
            return employeeJobsList.get(0).geteID().getID();
        }
        return null;
    }

    public List<EmployeeJobs> getEmployeeOnShift(String year, String month, String day, String timeShift, String branch) {
        List<EmployeeJobs> result = null;
        try {
            Shifts shifts = getSpecificShift(year, month, day, timeShift, branch);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<EmployeeJobs> criteriaQuery = cb.createQuery(EmployeeJobs.class);
            Root<EmployeeJobs> root = criteriaQuery.from(EmployeeJobs.class);
            criteriaQuery.select(root);
            Query<EmployeeJobs> query = databaseManager.getSession().createQuery(criteriaQuery);
            result = query.getResultList();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public Shifts getSpecificShift(String year, String month, String day, String timeShift, String branch) {
        Shifts result = null;
        try {
            Date date = getDateOfShift(year, month, day, timeShift);
            BranchProfile branchProfile = databaseManager.getSession().get(BranchProfile.class, branch);
            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<Shifts> criteriaQuery = cb.createQuery(Shifts.class);
            Root<Shifts> root = criteriaQuery.from(Shifts.class);
            criteriaQuery.select(root);
            criteriaQuery.where(cb.and(cb.and(cb.equal(root.get("date"), date), (cb.equal(root.get("timeOfShift"), timeShift))), cb.equal(root.get("nameBranch"), branch)));
            Query<Shifts> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Shifts> shifts = query.getResultList();
            if (shifts != null) {
                result = shifts.get(0);
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public List<Shifts> getShifts() {
        List<Shifts> result = new ArrayList<>();
        try {

            CriteriaBuilder cb = databaseManager.getCb();
            CriteriaQuery<Shifts> criteriaQuery = cb.createQuery(Shifts.class);
            Root<Shifts> root = criteriaQuery.from(Shifts.class);
            criteriaQuery.select(root);
            Query<Shifts> query = databaseManager.getSession().createQuery(criteriaQuery);
            result = query.getResultList();

        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public List<Shifts> getShiftFromThisDate() {
        List<Shifts> shifts = getShifts();
        List<Shifts> result = new ArrayList<>();
        Date date = new Date();
        for (Shifts s : shifts) {
            if (s.getDate().after(date)) {
                result.add(s);
            }
        }
        return result;

    }

    public void close() {
        this.employeeProfileLogin = null;
    }

    public List<ShiftsEmployees> getListEmployeeInJobAccordingByOrder(Integer chooseShift, String chooseJob) {
        List<Shifts> listShift=this.getShifts();
        Shifts shifts=listShift.get(chooseShift);
        return shifts.getEmployeeOnJob(chooseJob);
    }

    public boolean removeEmployeeFromShift(Integer chooseShift, String chooseJob, Integer chooseEmployeeInt) {
        List<ShiftsEmployees> list=getListEmployeeInJobAccordingByOrder(chooseShift,chooseJob);
        ShiftsEmployees shiftsEmployees=list.get(chooseEmployeeInt);
        if(removeEmployeeFromShift(shiftsEmployees)) {
            return true;
        }
        return false;
    }

    private boolean removeEmployeeFromShift(ShiftsEmployees shiftsEmployees) {
        try {
            EmployeeProfile employeeProfile = shiftsEmployees.getEmployeeProfile();
            Shifts shifts =shiftsEmployees.getShift();
            shifts.removeShiftsEmployee(shiftsEmployees);
            this.databaseManager.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String[] getArrOfShift(Integer choose) {
        Shifts shifts=getShiftInPlace(choose);
        String [] arr=new String[5];
        Date date=shifts.getDate();
        arr[0]= getDatePart(date,"yyyy");
        arr[1]= getDatePart(date,"M");
        arr[2]= getDatePart(date,"d");
        arr[3] = shifts.getTimeOfShift();
        arr[4] =shifts.getNameBranch().getBranchName();
        return arr;
    }

    private String getDatePart(Date date,String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public String getDetailsOfShift(Integer choose) {
        Shifts shifts=getShiftInPlace(choose);
        List<ShiftsEmployees> employeeJobsList=shifts.shiftsEmployees();
        String result=shifts.toString();
        if(employeeJobsList!=null && !employeeJobsList.isEmpty()){
            result=result+"\n"+ "Employees in the shift: \n\n";
            for(ShiftsEmployees shiftsEmployees : employeeJobsList){
                result = result + "Full Name : "+shiftsEmployees.getEmployeeProfile().getFirstName()+" "+shiftsEmployees.getEmployeeProfile().getLastName()+"\njob : "+shiftsEmployees.getJob()+"\n\n";
            }
        }
        return result;
    }

    public EmployeeProfile getEmployeeProfile(String eId){
        try{
            return databaseManager.getSession().get(EmployeeProfile.class, eId);
        }
        catch (Exception e){
            return null;
        }
    }

    public boolean removeShift(Shifts shifts){
        try {

            this.databaseManager.getSession().delete(shifts);
            this.databaseManager.commitAndFLush();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean removeEmployeeJobCanDo(String id, String job) {
        List<EmployeeJobs> list = getEmployeeJobs(id);
        EmployeeJobs delete =null;
        if (list != null) {
            for (EmployeeJobs employeeJobs : list) {
                if (employeeJobs.getJob().equals(job)) {
                    delete = employeeJobs;
                }
            }
        }
        if(delete!=null){
            try {

                this.databaseManager.getSession().delete(delete);
                this.databaseManager.commitAndFLush();
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }
}

package studyone.ksy.study.model;

public class User {
    private String userGrade, userType;
    private double userSavingRateOfGrade, userSavingRateOfType;
    private int userSavingCost;

    public User() {
    }

    public User(String userGrade, String userType, double userSavingRateOfGrade, double userSavingRateOfType, int userSavingCost) {
        this.userGrade = userGrade;
        this.userType = userType;
        this.userSavingRateOfGrade = userSavingRateOfGrade;
        this.userSavingRateOfType = userSavingRateOfType;
        this.userSavingCost = userSavingCost;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(String userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getUserSavingRateOfGrade() {
        return userSavingRateOfGrade;
    }

    public void setUserSavingRateOfGrade(String userGrade) {
        switch (userGrade) {
            case "silver" : userSavingRateOfGrade = 0.003;
            case "gold" : userSavingRateOfGrade = 0.005;
            case "platinum" : userSavingRateOfGrade = 0.01;
        }
        this.userSavingRateOfGrade = userSavingRateOfGrade;
    }

//    public void setUserSavingRateOfGrade(double userSavingRateOfGrade) {
//        switch (userGrade) {
//            case "silver" : userSavingRateOfGrade = 0.003;
//            case "gold" : userSavingRateOfGrade = 0.005;
//            case "platinum" : userSavingRateOfGrade = 0.01;
//        }
//        this.userSavingRateOfGrade = userSavingRateOfGrade;
//    }

    public double getUserSavingRateOfType() {
        return userSavingRateOfType;
    }

//    public void setUserSavingRateOfType(double userSavingRateOfType) {
//        switch (userType) {
//            case "general" : userSavingRateOfType = 0.00;
//            case "staff" : userSavingRateOfType = 0.01;
//            case "veterans" : userSavingRateOfType = 0.02;
//        }
//        this.userSavingRateOfType = userSavingRateOfType;
//    }

    public void setUserSavingRateOfType(String userType) {
        switch (userType) {
            case "general" : userSavingRateOfType = 0.00;
            case "staff" : userSavingRateOfType = 0.01;
            case "veterans" : userSavingRateOfType = 0.02;
        }
        this.userSavingRateOfType = userSavingRateOfType;
    }

    public int getUserSavingCost() {
        return userSavingCost;
    }

    public void setUserSavingCost(int userSavingCost) {
        this.userSavingCost = userSavingCost;
    }
}

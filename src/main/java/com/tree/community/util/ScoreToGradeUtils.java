package com.tree.community.util;

public class ScoreToGradeUtils {

    public static int scoreToGrade(int score){
        if(score<200){
            return 1;
        }else if(200<=score && score<1500){
            return 2;
        }else if(1500<=score && score<4500){
            return 3;
        }else if(4500<=score && score<10800){
            return 4;
        }else if(10800<=score && score<30000){
            return 5;
        }else{
            return 6;
        }
    }
}

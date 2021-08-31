package com.example.mytodo.AddTaskDialog;

import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_LOW;

public class MyDialogHelper {

    public static int getImportanceInt(String importanceText) {
        int imp = IMPORTANCE_NORMAL;
        switch (importanceText) {

            case "اولویت بالا":
                imp = IMPORTANCE_HIGH;
                break;
            case "اولویت متوسط":
                imp = IMPORTANCE_NORMAL;

                break;
            case "اولویت پایین":
                imp = IMPORTANCE_LOW;

                break;

        }
        return imp;
    }
}
